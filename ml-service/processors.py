"""
processors.py  —  HTML/YT → текст  → summary (OpenAI | fallback)
"""
from __future__ import annotations
import json, logging, re
from typing import List, Literal, Tuple

import openai
from openai import AsyncOpenAI
from bs4 import BeautifulSoup
from readability import Document
from pydantic import BaseModel
from langdetect import detect            # ⭑ авто-язык

from api import OPENAI_KEY

# тихие логи
for noisy in ("openai",):
    logging.getLogger(noisy).setLevel(logging.WARNING)

try:
    from youtube_transcript_api import YouTubeTranscriptApi, TranscriptsDisabled, NoTranscriptFound
    YT_AVAILABLE = True
except ModuleNotFoundError:
    logging.warning("youtube-transcript-api не установлен — видео пропускаются")
    YT_AVAILABLE = False


class Review(BaseModel):
    title: str
    content: str
    pros: str
    cons: str
    author: str
    date: str
    source: str
    url: str
    retrieved_at: str


class ReviewsResponse(BaseModel):
    status: Literal["success"] = "success"
    data: List[Review]


class ArticleProcessor:
    # HTML
    @staticmethod
    def extract_article(html: str) -> dict[str, str]:
        doc = Document(html)
        soup = BeautifulSoup(html, "lxml")
        text = BeautifulSoup(doc.summary(), "lxml").get_text(" ", strip=True)
        text = re.sub(r"\s{2,}", " ", text)
        return {
            "title": doc.title() or "Без названия",
            "text": text,
            "author": "Автор не указан",
            "date": "Дата не указана",
        }

    # YouTube
    @staticmethod
    def extract_youtube(video_id: str) -> dict[str, str] | None:
        if not YT_AVAILABLE:
            return None
        try:
            trs = YouTubeTranscriptApi.get_transcript(video_id, languages=["ru", "en"])
            text = " ".join(t["text"] for t in trs)
            text = re.sub(r"\s{2,}", " ", text)
            return {
                "title": f"YouTube video {video_id}",
                "text": text,
                "author": "YouTube",
                "date": "Дата не указана",
            }
        except (TranscriptsDisabled, NoTranscriptFound):
            return None
        except Exception as exc:
            logging.debug("YT transcript err %s", exc)
            return None

    # fallback
    @staticmethod
    def _fallback(txt: str) -> Tuple[str, str, str]:
        prev = " ".join(txt.split()[:60]) + ("…" if len(txt.split()) > 60 else "")
        return prev, "Не указаны", "Не указаны"

    # summarize
    @staticmethod
    async def summarize(text: str, lang: str | None) -> Tuple[str, str, str]:
        if not OPENAI_KEY or len(text) < 50:
            return ArticleProcessor._fallback(text)

        if lang in (None, "auto"):
            try:
                lang = detect(text[:1000])
            except Exception:
                lang = "en"

        prompt = (
            f"Проанализируй обзор. Верни JSON: summary (2-4 предлож.), "
            f"pros/cons (3-5 пунктов через ';'). Язык ответа — {lang}.\n\n{text[:8000]}"
        )
        try:
            client = AsyncOpenAI(api_key=OPENAI_KEY, max_retries=0)
            resp = await client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[{"role": "user", "content": prompt}],
                temperature=0.1, max_tokens=400,
                response_format={"type": "json_object"},
                timeout=20,
            )
            data = json.loads(resp.choices[0].message.content)
            return (
                data.get("summary", "Нет краткого содержания"),
                data.get("pros", "Не указаны"),
                data.get("cons", "Не указаны"),
            )
        except Exception:
            logging.debug("LLM fallback")
            return ArticleProcessor._fallback(text)
