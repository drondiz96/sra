# Этап сборки
FROM node:16 as build-stage

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Этап продакшена
FROM nginx:alpine as production-stage

# Генерируем SSL-сертификат во время билда
RUN apk add --no-cache openssl && \
    mkdir -p /etc/nginx/ssl && \
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
        -keyout /etc/nginx/ssl/selfsigned.key \
        -out /etc/nginx/ssl/selfsigned.crt \
        -subj "/CN=localhost"

# Копируем собранное Vue-приложение
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Копируем конфиг NGINX
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]
