"""
main.py  —  FastAPI + потоковая отдача (только статьи + рекомендация)
"""
from __future__ import annotations
import threading
import sys
if not hasattr(threading.Thread, "isAlive"):
    threading.Thread.isAlive = threading.Thread.is_alive
# ─────────────────────────────────────────────────────────────────────────────────

import asyncio
import json
import logging
import os
import re
from datetime import datetime, timezone
from typing import AsyncGenerator, Dict, Optional

from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.gzip import GZipMiddleware
from fastapi.responses import JSONResponse, PlainTextResponse, StreamingResponse

from api import ReviewsAPI, TRUSTED_SOURCES, purge_cache, close_session
from processors import ArticleProcessor

print("OPENAI_API_KEY =", "OK" if os.getenv("OPENAI_API_KEY") else "MISSING", file=sys.stderr)

logging.getLogger("uvicorn.access").setLevel(logging.WARNING)
log = logging.getLogger(__name__)

app = FastAPI(title="Reviews API (с рекомендацией)", version="4.2.0")
app.add_middleware(GZipMiddleware, minimum_size=512)


# ── graceful shutdown ------------------------------------------------
@app.on_event("shutdown")
async def _shutdown():
    await close_session()


def _trusted(url: str, needle: str) -> bool:
    """
    Проверка двух условий:
      1) домен в TRUSTED_SOURCES,
      2) модель (needle) — plain или с дефисами — встречается в URL.
    """
    dom = re.search(r"https?://(?:www\\.)?([^/]+)", url)
    d = dom.group(1).lower() if dom else ""
    if not any(t in d for t in TRUSTED_SOURCES):
        return False

    normalized_url = url.lower()
    needle_plain = needle
    needle_hyphen = needle.replace(" ", "-")
    return (needle_plain in normalized_url) or (needle_hyphen in normalized_url)


def _priority(pros: list[str], cons: list[str]) -> str:
    if len(pros) >= 3 and len(cons) >= 3:
        return "high"
    if pros or cons:
        return "medium"
    return "low"


@app.get("/api/reviews", summary="Получить обзоры (только статьи + рекомендация)")
async def get_reviews(
    brand: str = Query(..., min_length=2),
    model: str = Query(..., min_length=1),
    language: str = Query("auto"),
    limit: int = Query(6, ge=1, le=20),
    model_filter: Optional[str] = None,
    stream: bool = False,
):
    core = f"{brand} {model}"
    q_art = f"{core} {'обзор' if language.startswith('ru') else 'review'}"

    flt = (model_filter or model).lower()

    # 1) ищем ссылки
    art_links = await ReviewsAPI.search_links(
        q_art,
        lang="ru" if language.startswith("ru") else "en",
        n=limit * 4,
    )

    # 2) фильтруем по доверенному домену и наличию названия модели в URL
    art_links = [u for u in art_links if _trusted(u, flt)]

    if not art_links:
        raise HTTPException(404, "Ничего не найдено по заданным критериям")

    async def _producer() -> AsyncGenerator[bytes, None]:
        first = True
        yield b'{"status":"success","data":['

        async def emit(obj: Dict):
            nonlocal first
            if not first:
                yield b","
            else:
                first = False
            yield json.dumps(obj, ensure_ascii=False).encode()

        count = 0
        html_res = await asyncio.gather(*(ReviewsAPI.fetch_html(u) for u in art_links),
                                        return_exceptions=True)

        for url, html in zip(art_links, html_res):
            if count >= limit:
                break
            if not html or isinstance(html, Exception):
                continue

            meta = ArticleProcessor.extract_article(html)
            title_lower = meta["title"].lower()
            needle_plain = flt
            needle_hyphen = flt.replace(" ", "-")
            if (needle_plain not in title_lower) and (needle_hyphen not in title_lower):
                continue

            words = len(meta["text"].split())
            ratio = 1.0 if words <= 80 else 0.5 if words <= 300 else 0.20
            summ = await ArticleProcessor.summarize(meta["text"], ratio, language)
            prio = _priority(summ["pros"], summ["cons"])

            log.info("POST | %s | pros=%d cons=%d prio=%s",
                     meta["title"][:40], len(summ["pros"]), len(summ["cons"]), prio)

            item = {
                "title": meta["title"],
                "author": meta["author"],
                "date": meta["date"],
                "source": re.search(r"https?://(?:www\\.)?([^/]+)", url).group(1),
                "url": url,
                "retrieved_at": datetime.now(timezone.utc).isoformat(),
                "pros": summ["pros"],
                "cons": summ["cons"],
                "priority": prio,
                "summary": summ["summary"],
                "recommendation": summ["recommendation"],
            }
            async for chunk in emit(item):
                yield chunk
            count += 1

        yield b"]}"

    if stream:
        return StreamingResponse(_producer(), media_type="application/json")
    body = b"".join([c async for c in _producer()])
    return JSONResponse(content=json.loads(body))  # type: ignore[arg-type]


@app.post("/api/cache/clear")
async def clear_cache():
    return {"removed": purge_cache()}


@app.get("/healthz", response_class=PlainTextResponse)
async def health():
    return "ok"


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
