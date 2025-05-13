"""
main.py  —  FastAPI + GZip + streaming / авто-язык / очистка кэша
"""
from __future__ import annotations

import asyncio, json, logging, re
from datetime import datetime, timezone
from typing import AsyncGenerator

from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.gzip import GZipMiddleware
from fastapi.responses import JSONResponse, PlainTextResponse, StreamingResponse

from api import ReviewsAPI, purge_cache
from processors import ArticleProcessor, Review, ReviewsResponse

logging.basicConfig(level=logging.INFO, format="%(asctime)s | %(levelname)s | %(message)s")
for noisy in ("httpx", "urllib3", "openai"):
    logging.getLogger(noisy).setLevel(logging.WARNING)

app = FastAPI(title="Reviews API (Google + YouTube)", version="2.3.0")
app.add_middleware(GZipMiddleware, minimum_size=512)   # ⭑ gzip

YOUTUBE_RE = re.compile(r"(?:v=|be/)([A-Za-z0-9_-]{11})")

# ── reviews ────────────────────────────────────────────────────────────
@app.get("/api/reviews", summary="Получить обзоры")
async def get_reviews(
    brand: str = Query(..., min_length=2),
    model: str = Query(..., min_length=1),
    language: str = Query("auto"),
    limit: int = Query(6, ge=1, le=20),
    stream: bool = Query(False, description="chunk-stream вывод"),
):
    core = f"{brand} {model}"
    q_article = f"{core} {'обзор' if language.startswith('ru') else 'review'}"
    q_yt = f"{core} обзор site:youtube.com/watch"

    art_links, yt_links = await asyncio.gather(
        ReviewsAPI.search_links(q_article, lang="ru" if language.startswith("ru") else "en",
                                n=max(limit * 3, 30)),
        ReviewsAPI.search_links(q_yt, lang="ru" if language.startswith("ru") else "en",
                                n=max(limit * 2, 20)),
    )
    if not (art_links or yt_links):
        raise HTTPException(404, "Ничего не найдено")

    async def _producer() -> AsyncGenerator[bytes, None]:
        # открываем JSON
        yield b'{"status":"success","data":['
        first = True

        async def _emit(review: Review):
            nonlocal first
            if not first:
                yield b","                       # запятая между объектами
            else:
                first = False
            yield json.dumps(review.model_dump(), ensure_ascii=False).encode()

        # — статьи —
        html_tasks = {u: asyncio.create_task(ReviewsAPI.fetch_html(u)) for u in art_links}
        html_results = await asyncio.gather(*html_tasks.values(), return_exceptions=True)

        count = 0
        for url, html in zip(html_tasks.keys(), html_results):
            if count >= limit:
                break
            if not html or isinstance(html, Exception):
                continue
            art = ArticleProcessor.extract_article(html)
            if len(art["text"]) < 500:
                continue
            summ, pros, cons = await ArticleProcessor.summarize(art["text"], language)
            dom = re.search(r"https?://(?:www\\.)?([^/]+)", url)
            review = Review(
                title=art["title"], content=summ, pros=pros, cons=cons,
                author=art["author"], date=art["date"],
                source=dom.group(1) if dom else url,
                url=url, retrieved_at=datetime.now(timezone.utc).isoformat()
            )
            async for chunk in _emit(review):
                yield chunk
            count += 1

        # — YouTube —
        if count < limit:
            for url in yt_links:
                if count >= limit:
                    break
                vid = YOUTUBE_RE.search(url)
                if not vid:
                    continue
                meta = ArticleProcessor.extract_youtube(vid.group(1))
                if not meta:
                    continue
                summ, pros, cons = await ArticleProcessor.summarize(meta["text"], language)
                review = Review(
                    title=meta["title"], content=summ, pros=pros, cons=cons,
                    author=meta["author"], date=meta["date"],
                    source="youtube.com", url=url,
                    retrieved_at=datetime.now(timezone.utc).isoformat()
                )
                async for chunk in _emit(review):
                    yield chunk
                count += 1

        # закрываем JSON-массив + объект
        yield b"]}"

    if stream:
        return StreamingResponse(_producer(), media_type="application/json")
    # иначе – старый способ
    reviews_json = b"".join([chunk async for chunk in _producer()])
    return JSONResponse(content=json.loads(reviews_json))   # type: ignore[arg-type]

# ── очистить кэш ───────────────────────────────────────────────────────
@app.post("/api/cache/clear", summary="Очистить кэш")
async def clear_cache():
    removed = purge_cache()
    return {"status": "cleared" if removed else "noop", "items_removed": removed}

# ── health ─────────────────────────────────────────────────────────────
@app.get("/healthz", response_class=PlainTextResponse)
async def health():
    await ReviewsAPI.search_links("test phone review", lang="en", n=2)
    return "ok"

if __name__ == "__main__":
    import uvicorn

    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
