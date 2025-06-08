#!/usr/bin/env bash
set -euxo pipefail

# ── 1. ПУТИ ─────────────────────────────────────────────────────────────────
# SOURCE_DIR   — локальная папка вашего проекта (содержимое которой нужно скопировать)
# TARGET_DIR   — корень клонированного репозитория drondiz96/sra (ветка Ivan)
# SUBFOLDER    — поддиректория внутри TARGET_DIR, куда класть файлы (ml-service)

SOURCE_DIR="$HOME/Desktop/reviews-api-up/"
TARGET_DIR="$HOME/Desktop/sra"
SUBFOLDER="ml-service"

# ── 2. Переходим в TARGET_DIR и убеждаемся, что на ветке Ivan ─────────────────
cd "$TARGET_DIR"

CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$CURRENT_BRANCH" != "Ivan" ]]; then
  echo "⚠️  Текущая ветка — '$CURRENT_BRANCH', переключаюсь на 'Ivan'..."
  git checkout Ivan
fi

# ── 2.1. Обновляем ветку Ivan (забираем свежие коммиты) ──────────────────────
echo "⏳ Обновляем ветку Ivan из origin..."
git pull --ff-only origin Ivan

# ── 2.2. Проверяем, что нет незакоммиченных изменений в TARGET_DIR ────────────
if ! git diff --quiet --ignore-submodules; then
  echo "❗️ У вас есть незакоммиченные изменения в $TARGET_DIR. Пожалуйста, закоммитьте или уберите их перед копированием."
  exit 1
fi

# ── 3. Убеждаемся, что папка ml-service существует, иначе создаём ────────────
if [[ ! -d "$TARGET_DIR/$SUBFOLDER" ]]; then
  echo "ℹ️ Папка '$SUBFOLDER' внутри репозитория не найдена. Создаю..."
  mkdir -p "$TARGET_DIR/$SUBFOLDER"
  git add "$SUBFOLDER"
  git commit -m "Создал папку '$SUBFOLDER' для размещения ml-service"
fi

# ── 4. Копируем файлы из SOURCE_DIR → TARGET_DIR/ml-service ─────────────────
echo "⏳ Копируем содержимое '$SOURCE_DIR' в '$TARGET_DIR/$SUBFOLDER'..."
# Опция --delete удалит из ml-service файлы, которых нет в SOURCE_DIR, 
# чтобы синхронизировать точно содержимое.
rsync -av --delete \
    --exclude='.git/' \
    --exclude='.gitignore' \
    --exclude='.*.sw?' \
    "$SOURCE_DIR" \
    "$TARGET_DIR/$SUBFOLDER/"

# ── 5. Проверяем, появились ли изменения ────────────────────────────────────
git add "$SUBFOLDER"
if git diff --cached --quiet; then
  echo "✅  В папке '$SUBFOLDER' нет новых изменений — нет ничего для коммита."
else
  echo "✅  Есть изменения. Делаю коммит и пушу в ветку Ivan..."
  git commit -m "Обновил содержимое ml-service из ~/Desktop/reviews-api-up"
  git push origin Ivan
fi

echo "🎉 Готово: обновления из '$SOURCE_DIR' скопированы в '$TARGET_DIR/$SUBFOLDER' и запушены в ветку Ivan."

exit 0
