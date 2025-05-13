"""
api.py  —  Google-поиск, кэш (diskcache), скачивание HTML
"""
from __future__ import annotations
import asyncio, logging, os, re, time
from typing import Any, Dict, List, Optional

import aiohttp, backoff, diskcache
from dotenv import load_dotenv
from googlesearch import search as google_search

# ── env ───────────────────────────────────────────────────────────────
load_dotenv(".env")
OPENAI_KEY: Optional[str] = os.getenv("OPENAI_API_KEY")

HEADERS: Dict[str, str] = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
    ),
    "Accept-Language": "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
}

# ── cache ─────────────────────────────────────────────────────────────
try:
    CACHE: diskcache.Cache | Dict[str, Any] = diskcache.Cache(".reviews_cache")
except ModuleNotFoundError:
    logging.warning("diskcache не найден — кэширование выключено")

    class _Dummy(dict):
        def clear(self):                       # type: ignore[override]
            n = len(self)
            super().clear()
            return n

    CACHE = _Dummy()                           # type: ignore[assignment]

def purge_cache() -> int:
    try:
        return CACHE.clear() or 0
    except Exception:
        return 0

# ── aiohttp singleton ────────────────────────────────────────────────
async def _session() -> aiohttp.ClientSession:
    if not hasattr(_session, "_s") or _session._s.closed:            # type: ignore[attr-defined]
        _session._s = aiohttp.ClientSession(                         # type: ignore[attr-defined]
            headers=HEADERS,
            connector=aiohttp.TCPConnector(limit_per_host=4),
        )
    return _session._s                                               # type: ignore[attr-defined]

# ── ReviewsAPI ───────────────────────────────────────────────────────
class ReviewsAPI:
    """Google-поиск ссылок + кэш + скачивание HTML"""

    @staticmethod
    def _norm(url: str) -> str:
        return re.sub(r"[?#].*$", "", url).rstrip("/")

    @staticmethod
    def _google_sync(q: str, lang: str, n: int) -> List[str]:
        try:
            out = list(google_search(q, lang=lang, num_results=n))
            time.sleep(2.0)
            return out
        except TypeError:
            logging.warning("googlesearch: fallback без num_results")
            return list(google_search(q))[:n]

    @staticmethod
    @backoff.on_exception(backoff.expo, (Exception,), max_tries=3, base=2, factor=2)
    async def search_links(q: str, *, lang: str = "en", n: int = 30) -> List[str]:
        ck = f"google:{lang}:{q}"
        if (c := CACHE.get(ck)):
            logging.info("Google cache hit %s", q)
            return c                                               # type: ignore[return-value]

        raw = await asyncio.to_thread(ReviewsAPI._google_sync, q, lang, n)
        raw = [ReviewsAPI._norm(u) for u in raw if not u.lower().endswith(".pdf")]

        seen, uniq = set(), []
        for u in raw:
            dom = re.search(r"https?://(?:www\\.)?([^/]+)", u)
            d = dom.group(1) if dom else u
            if d not in seen:
                seen.add(d)
                uniq.append(u)

        CACHE.set(ck, uniq, expire=86_400)
        return uniq

    # HTML
    @staticmethod
    @backoff.on_exception(backoff.expo,
                          (aiohttp.ClientError, asyncio.TimeoutError),
                          max_tries=3, base=1, factor=2)
    async def fetch_html(url: str) -> Optional[str]:
        ck = f"html:{url}"
        if (c := CACHE.get(ck)):
            return c                                               # type: ignore[return-value]

        sess = await _session()
        try:
            async with sess.get(url, timeout=20, ssl=False) as r:
                r.raise_for_status()
                if "text/html" in r.headers.get("Content-Type", ""):
                    txt = await r.text()
                    CACHE.set(ck, txt, expire=86_400)
                    return txt
        except Exception as exc:
            logging.debug("fetch_html %s %s", url, exc)
        return None
