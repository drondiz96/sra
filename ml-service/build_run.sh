#!/usr/bin/env bash
set -euxo pipefail

###############################################################################
#  Скрипт «run.sh» для пересборки образа и запуска контейнера reviews-api     #
###############################################################################

# 1) Путь к каталогу проекта (где лежат Dockerfile, .env, trusted_sources.json и т.д.)
PROJECT_DIR="$HOME/Desktop/reviews-api-up"

# 2) Имя/тег Docker‐образа и контейнера
IMAGE_TAG="reviews-api:latest"
CONTAINER_NAME="reviews-api"

# 3) Порт, на котором API будет доступен
PORT="8000"

# 4) Имя файла с переменными окружения (должен лежать в PROJECT_DIR)
ENV_FILE=".env"


###############################################################################
#  Функция: проверить, установлен ли Docker; если нет – установить его
ensure_docker() {
  if ! command -v docker &>/dev/null; then
    echo ">>> Docker не найден. Устанавливаю…"
    sudo apt-get update
    sudo apt-get install -y \
         ca-certificates curl gnupg lsb-release
    sudo install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/debian/gpg \
      | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
      https://download.docker.com/linux/debian $(lsb_release -cs) stable" \
      | sudo tee /etc/apt/sources.list.d/docker.list

    sudo apt-get update
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io
    echo ">>> Docker установлен."
  else
    echo ">>> Docker уже установлен."
  fi
}

# 5) Перейти в каталог проекта
cd "$PROJECT_DIR"

# 6) Убедиться, что Docker есть
ensure_docker

# 7) Пересобрать образ
echo ">>> Собираем Docker‐образ: $IMAGE_TAG"
docker build -t "$IMAGE_TAG" .

# 8) Остановить и удалить старый контейнер, если он существует
if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  echo ">>> Останавливаю и удаляю контейнер $CONTAINER_NAME"
  docker rm -f "$CONTAINER_NAME"
fi

# 9) Запустить новый контейнер
RUN_CMD=(
  docker run -d
    --name "$CONTAINER_NAME"
    -p "$PORT":8000
)

# Если .env есть, подключаем его внутрь контейнера
if [[ -f "$PROJECT_DIR/$ENV_FILE" ]]; then
  RUN_CMD+=(--env-file "$PROJECT_DIR/$ENV_FILE")
fi

# Добавляем образ
RUN_CMD+=("$IMAGE_TAG")

echo ">>> Запускаю контейнер $CONTAINER_NAME"
"${RUN_CMD[@]}"

echo "=== Готово! API доступен по адресу: http://localhost:$PORT ==="
