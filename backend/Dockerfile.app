FROM openjdk:23

# Устанавливаем рабочий каталог внутри контейнера
WORKDIR /bip-project

# Копируем jar-файл вашего Spring Boot приложения в контейнер
COPY target/bip_project-0.0.1-SNAPSHOT.jar app.jar

COPY ./.env .env

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]