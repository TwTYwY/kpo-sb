# Архитектура системы и пользовательские сценарии

## i. Краткое описание архитектуры системы

Система построена по микросервисной архитектуре и состоит из трех основных компонентов:

### **1. API Gateway (порт 8080)**
- **Роль**: Единая точка входа для всех клиентских запросов
- **Технологии**: Spring Cloud Gateway, WebFlux, Resilience4j
- **Функции**:
    - Маршрутизация запросов к соответствующим микросервисам
    - Реализация Circuit Breaker для отказоустойчивости
    - Retry механизм (3 попытки для GET/POST запросов)
    - CORS обработка для крос-доменных запросов
    - Fallback обработка при недоступности сервисов

### **2. File Storing Service (порт 8081)**
- **Роль**: Хранение студенческих работ и их метаданных
- **Технологии**: Spring Boot, Spring Web, Spring Data JPA
- **Функции**:
    - Прием файлов через multipart/form-data
    - Сохранение файлов на файловую систему
    - Хранение метаданных в PostgreSQL (filestoredb)
    - Асинхронный запуск анализа работ
    - Ограничение размера файлов (10MB)

### **3. File Analysis Service (порт 8082)**
- **Роль**: Анализ работ на наличие плагиата
- **Технологии**: Spring Boot, Spring Web, Spring Data JPA
- **Функции**:
    - Проверка работ на плагиат
    - Генерация отчетов о проверке
    - Хранение результатов в PostgreSQL (analysisdb)
    - Предоставление отчетов по запросу

---

## ii. Пользовательские сценарии микросервисов

### **Сценарий 1: Студент загружает работу**

**Техническая последовательность:**
```
1. Клиент → API Gateway (POST /works)
   Content-Type: multipart/form-data
   Параметры: studentName, taskId, file

2. API Gateway → File Storing Service
   Проксирование запроса с Circuit Breaker проверкой

3. File Storing Service:
   - Валидация размера файла (max 10MB)
   - Сохранение файла в /uploads/
   - Запись метаданных в БД (таблица works)
   - Генерация Work ID

4. File Storing Service → File Analysis Service (асинхронно)
   POST /reports/analyze
   Body: {"workId": 123, "taskId": "HW3"}

5. File Analysis Service:
   - Проверка существующих работ по taskId
   - Определение плагиата (если есть другие работы по тому же taskId)
   - Создание записи в БД (таблица reports)
   - Установка plagiarismFlag

6. File Storing Service → Клиент
   HTTP 200 OK с JSON объекта Work
```

### **Сценарий 2: Преподаватель запрашивает отчет**

**Техническая последовательность:**
```
1. Клиент → API Gateway (GET /reports/work/{id})

2. API Gateway → File Analysis Service
   Маршрутизация с Path предикатом (/reports/**)
   Circuit Breaker проверка доступности

3. File Analysis Service:
   - Поиск отчетов по workId в БД
   - Формирование списка отчетов

4. File Analysis Service → Клиент
   HTTP 200 OK с массивом Report объектов
   [
     {
       "workId": 123,
       "status": "COMPLETED",
       "plagiarismFlag": true/false,
       "reportPath": "/reports/report_123.json"
     }
   ]
```

### **Сценарий 3: Получение информации о работе**

**Техническая последовательность:**
```
1. Клиент → API Gateway (GET /works/{id})

2. API Gateway → File Storing Service
   Маршрутизация с Path предикатом (/works/**)
   Retry механизм: 3 попытки при BAD_GATEWAY/SERVICE_UNAVAILABLE

3. File Storing Service:
   - Поиск работы по ID в БД
   - Проверка существования записи

4. File Storing Service → Клиент
   Если найдено: HTTP 200 OK с Work объектом
   Если не найдено: HTTP 404 Not Found
```

### **Сценарий 4: Health Check мониторинг**

**Техническая последовательность:**
```
1. Мониторинг → API Gateway (GET /fallback/health)
   Ответ: "API Gateway is UP"

2. Мониторинг → API Gateway (GET /works/health)
   API Gateway → File Storing Service
   Ответ: "File Storing Service is UP"

3. Мониторинг → API Gateway (GET /reports/health)
   API Gateway → File Analysis Service
   Ответ: "File Analysis Service is UP"
```

### **Сценарий 5: Обработка недоступности сервиса (Circuit Breaker)**

**Техническая последовательность:**
```
1. Клиент → API Gateway (GET /reports/work/1)

2. API Gateway проверяет доступность File Analysis Service
   Если сервис недоступен (timeout/error):

3. API Gateway → Fallback Controller
   GET /fallback/file-analysis

4. Fallback Controller → Клиент
   HTTP 503 Service Unavailable
   Body: "File Analysis Service is unavailable. Please try again later."

5. Circuit Breaker запоминает состояние и временно перенаправляет
   все запросы на fallback до восстановления сервиса
```

### **Сценарий 6: Обработка ошибки размера файла**

**Техническая последовательность:**
```
1. Клиент → API Gateway (POST /works с файлом >10MB)

2. API Gateway → File Storing Service

3. File Storing Service:
   - Spring выбрасывает MaxUploadSizeExceededException
   - GlobalExceptionHandler перехватывает исключение

4. GlobalExceptionHandler → Клиент
   HTTP 413 Payload Too Large
   Body: {
     "timestamp": "2024-01-15T10:30:00",
     "message": "File size exceeds the limit (10MB)",
     "status": 413
   }
```

### **Сценарий 7: Асинхронный анализ работы**

**Техническая последовательность:**
```
1. После успешного сохранения работы в File Storing Service:

2. File Storing Service создает новый поток:
   Thread thread = new Thread(() -> {
     // Асинхронный вызов анализа
   });

3. Внутри потока:
   - Формирование HTTP запроса к File Analysis Service
   - POST http://file-analysis-service:8082/reports/analyze
   - Body: {"workId": 123, "taskId": "HW3"}

4. File Analysis Service:
   - Принимает запрос в ReportController.analyzeWork()
   - Вызывает ReportService.analyzeWork()
   - Проверяет наличие других работ по taskId
   - Создает и сохраняет отчет

5. Клиент НЕ ждет завершения анализа
   Ответ с Work объектом отправляется сразу после сохранения файла
```