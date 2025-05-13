#!/usr/bin/env bash
set -euxo pipefail 

### Настройки #############################################################
PROJECT_DIR="$HOME/Desktop/reviews-api"    # путь к каталогу проекта
IMAGE_TAG="reviews-api:latest"             # имя/тег docker-образа
CONTAINER_NAME="reviews-api"               # имя контейнера
PORT="8000"                                # внешний порт
ENV_FILE=".env"                            # если .env лежит в проекте

###########################################################################

# ── 1. Проверка Docker ───────────────────────────────────────────────────
if ! command -v docker &>/dev/null; then
  echo ">>> Docker не найден. Устанавливаю…"
  sudo apt-get update
  sudo apt-get install -y \
       ca-certificates curl gnupg lsb-release
  sudo install -m 0755 -d /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/debian/gpg |
       sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
    https://download.docker.com/linux/debian $(lsb_release -cs) stable" |
    sudo tee /etc/apt/sources.list.d/docker.list

  sudo apt-get update
  sudo apt-get install -y docker-ce docker-ce-cli containerd.io
  echo ">>> Docker установлен."
fi

# ── 2. Сборка образа ─────────────────────────────────────────────────────
echo ">>> Переходим в каталог проекта: $PROJECT_DIR"
cd "$PROJECT_DIR"

echo ">>> Собираю образ $IMAGE_TAG …"
docker build -t "$IMAGE_TAG" .

# ── 3. Остановка старого контейнера (если был) ───────────────────────────
if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME" ; then
  echo ">>> Останавливаю существующий контейнер $CONTAINER_NAME"
  docker rm -f "$CONTAINER_NAME"
fi

# ── 4. Запуск нового контейнера ─────────────────────────────────────────
RUN_CMD=(docker run -d --name "$CONTAINER_NAME" -p "$PORT":8000)

# если .env существует — монтируем
if [[ -f "$ENV_FILE" ]]; then
  RUN_CMD+=(--env-file "$ENV_FILE")
fi
RUN_CMD+=("$IMAGE_TAG")

echo ">>> Запуск контейнера…"
"${RUN_CMD[@]}"

echo "=== Готово! Swagger UI: http://localhost:$PORT/docs ==="
