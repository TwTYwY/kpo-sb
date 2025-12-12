#!/bin/bash
echo "=== Запуск тестов системы антиплагиата ==="

BASE_URL="http://localhost:8080"

echo "1. Проверка здоровья сервисов..."
echo "API Gateway: $(curl -s "$BASE_URL/fallback/health")"
echo "File Storing: $(curl -s "$BASE_URL/works/health")"
echo "File Analysis: $(curl -s "$BASE_URL/reports/health")"

echo -e "\n2. Загрузка тестовых работ..."
echo "Создание тестовых файлов..."
echo "Решение задачи по КПО" > test1.txt
echo "Решение задачи по КПО" > test2.txt

echo -e "\n3. Тестирование загрузки..."
echo "Первая работа (Student1):"
response1=$(curl -s -X POST "$BASE_URL/works" \
  -F "studentName=Student1" \
  -F "taskId=HW3_TEST" \
  -F "file=@test1.txt")
echo $response1

# Извлекаем ID первой работы
workId1=$(echo $response1 | grep -o '"id":[0-9]*' | cut -d: -f2)

echo -e "\nВторая работа (Student2 - плагиат):"
response2=$(curl -s -X POST "$BASE_URL/works" \
  -F "studentName=Student2" \
  -F "taskId=HW3_TEST" \
  -F "file=@test2.txt")
echo $response2

# Извлекаем ID второй работы
workId2=$(echo $response2 | grep -o '"id":[0-9]*' | cut -d: -f2)

echo -e "\n4. Проверка отчетов (ждем 2 секунды)..."
sleep 2

echo "Отчет по работе $workId1:"
curl -s "$BASE_URL/reports/work/$workId1" | jq '.'

echo -e "\nОтчет по работе $workId2 (должен показать плагиат):"
curl -s "$BASE_URL/reports/work/$workId2" | jq '.'

echo -e "\n5. Проверка информации о работах..."
echo "Информация о работе $workId1:"
curl -s "$BASE_URL/works/$workId1" | jq '.'

echo -e "\nИнформация о работе $workId2:"
curl -s "$BASE_URL/works/$workId2" | jq '.'

echo -e "\n6. Очистка тестовых файлов..."
rm -f test1.txt test2.txt

echo -e "\n=== Тестирование завершено ==="
echo "ID работ для Postman коллекции:"
echo "workId1 = $workId1"
echo "workId2 = $workId2"