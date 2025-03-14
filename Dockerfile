# Этап сборки
FROM node:16 as build-stage

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем package.json и package-lock.json
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем исходный код
COPY . .

# Аргумент для выбора режима сборки
ARG MODE=dev

# Сборка приложения
RUN if [ "$MODE" = "dev" ]; then \
     npm run serve; \
    else \
      npm run build; \
    fi

# Этап запуска
FROM nginx:alpine as production-stage

# Копируем собранные файлы
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Копируем конфигурацию Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Открываем порт 80
EXPOSE 80

# Запускаем Nginx
CMD ["nginx", "-g", "daemon off;"]
