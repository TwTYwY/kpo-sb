# Этап сборки
FROM gradle:7.5-jdk11 AS builder
WORKDIR /app

# Копировать исходники api-gateway
COPY api-gateway/build.gradle .
COPY api-gateway/src ./src

# Собрать приложение
RUN gradle clean build -x test

# Этап запуска
FROM eclipse-temurin:11-jre-focal
WORKDIR /app

# Скопировать собранный jar
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]