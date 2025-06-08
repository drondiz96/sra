"""
processors.py  —  HTML → текст → summary (+ pros / cons + recommendation)
"""
from __future__ import annotations
import json
import logging
import os
import re
from typing import Any, Dict, List

from bs4 import BeautifulSoup
from langdetect import detect
from openai import AsyncOpenAI
from readability.readability import Document

from api import OPENAI_KEY

OPENAI_BASE_URL = os.getenv("OPENAI_BASE_URL")
log = logging.getLogger(__name__)


class ArticleProcessor:
    # ── HTML ----------------------------------------------------------
    @staticmethod
    def extract_article(html: str) -> Dict[str, str]:
        doc = Document(html)
        text = BeautifulSoup(doc.summary(), "lxml").get_text(" ", strip=True)
        text = re.sub(r"\s{2,}", " ", text)
        return {
            "title": doc.title() or "Без названия",
            "text": text,
            "author": "Автор не указан",
            "date": "Дата не указана",
        }

    # ── fallback ------------------------------------------------------
    @staticmethod
    def _fallback(txt: str) -> Dict[str, Any]:
        words = txt.split()
        preview = " ".join(words[:60]) + ("…" if len(words) > 60 else "")
        return {
            "summary": preview,
            "pros": [],
            "cons": [],
            "recommendation": "Недостаточно информации для рекомендации."
        }

    # ── summarizer ----------------------------------------------------
    @staticmethod
    async def summarize(text: str, ratio: float, lang: str | None) -> Dict[str, Any]:
        # Если API-ключ отсутствует или текст очень короткий, возвращаем «фолбэк»
        if not OPENAI_KEY or len(text.split()) < 50:
            return ArticleProcessor._fallback(text)

        # Определяем язык, если «auto»
        if lang in (None, "auto"):
            try:
                lang = detect(text[:1000])
            except Exception:
                lang = "en"

        # Целевое количество слов в summary
        total_words = len(text.split())
        tgt_words = max(1, int(total_words * ratio))

        system_prompt = f"""
You are a concise and precise review-summarization agent optimized for GPT-4o-mini.
When you receive the full text of one product review, you MUST output a single valid JSON object with **exactly** these keys (and no others):
  • "summary" – связное резюме длиной ≈ {tgt_words} слов (±10%).
  • "pros" – массив из 3–5 коротких сильных пунктов (5–10 слов каждый).
  • "cons" – массив из 3–5 коротких слабых пунктов (5–10 слов каждый).
  • "recommendation" – рекомендации по покупке этого телефону, учитывая типичные потребности пользователя
    (не более 20–30 слов, чётко и понятно).

Требования:
  1. Никогда не добавляй дополнительные ключи, текст или комментарии вне JSON.
  2. JSON должен быть строго валидным:
     – двойные кавычки вокруг ключей и строк,
     – без завершающих запятых,
     – без символов «…» в самом JSON.
  3. Не использовать markdown, заголовки вроде "Overall:", "Summary:" и т. д.
  4. Язык ответа: {lang}.

Если текст обзора слишком короткий (< 50 слов), верни:
{{
  "summary": "<первые 30–50 слов или весь текст>",
  "pros": [],
  "cons": [],
  "recommendation": "Недостаточно информации для рекомендации."
}}
""".strip()

        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": text},
        ]

        client = AsyncOpenAI(
            api_key=OPENAI_KEY,
            base_url=OPENAI_BASE_URL or None,
            max_retries=0,
        )

        try:
            resp = await client.chat.completions.create(
                model="gpt-4o-mini",
                messages=messages,
                temperature=0.0,
                max_tokens=1024,
                response_format={"type": "json_object"},
                timeout=120,
            )
            jd = json.loads(resp.choices[0].message.content)
        except Exception as exc:
            log.error("❌ OpenAI error: %s", exc, exc_info=True)
            return ArticleProcessor._fallback(text)

        def _to_list(val: Any) -> List[str]:
            if isinstance(val, list):
                return [v.strip() for v in val if isinstance(v, str)]
            if isinstance(val, str):
                parts = re.split(r"[;•,\.]\s*", val)
                return [p.strip() for p in parts if p.strip()]
            return []

        return {
            "summary": jd.get("summary", "").strip(),
            "pros": _to_list(jd.get("pros", [])),
            "cons": _to_list(jd.get("cons", [])),
            "recommendation": jd.get("recommendation", "").strip()
        }
