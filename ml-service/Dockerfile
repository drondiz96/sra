###############################################################################
#  Dockerfile для “reviews-api” под Python 3.10                                #
###############################################################################

# 1) Базовый слой: Debian-slim + Python 3.10
FROM python:3.13-slim

# 2) Переменные окружения
ENV PYTHONUNBUFFERED=1 \
    PIP_NO_CACHE_DIR=on

# 3) Системные библиотеки (нужны lxml / readability-lxml)
RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      build-essential \
      libxml2-dev \
      libxslt1-dev \
 && rm -rf /var/lib/apt/lists/*

# 4) Устанавливаем зависимости Python
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt

# 5) Копируем конфиг с доверенными доменами
COPY trusted_sources.json .

# 6) Копируем исходники приложения
COPY . .

# 7) Открываем порт API
EXPOSE 8000

# 8) Команда запуска (production-режим)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
