Запуск проекта

Проект состоит из нескольких микросервисов (API Gateway, Orders, Payments) и фронтенда.

1️. Собрать jar-файлы сервисов

В корне проекта выполните:

./gradlew clean build

(если Windows — используйте gradlew.bat)

2️. Запустить всё через Docker Compose

docker compose up --build

После сборки и запуска:
	•	API Gateway будет доступен на:
http://localhost:8080
	•	Фронтенд — на:
http://localhost:3000

3️. Остановить

docker compose down