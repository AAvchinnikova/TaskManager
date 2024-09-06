### Hexlet tests and linter status:
[![Actions Status](https://github.com/AAvchinnikova/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/AAvchinnikova/java-project-99/actions)

### Maintainability Badge
<a href="https://codeclimate.com/github/AAvchinnikova/java-project-99/maintainability"><img src="https://api.codeclimate.com/v1/badges/db69c484598d69fadf80/maintainability" /></a>

### Test Coverage Badge
<a href="https://codeclimate.com/github/AAvchinnikova/java-project-99/test_coverage"><img src="https://api.codeclimate.com/v1/badges/db69c484598d69fadf80/test_coverage" /></a>

### Менеджер задач
- представляет собой приложение для создания, отслеживания и манипуляции задачами - назначения на них разных исполнителей, изменения статуса задач и добавления к задачам разных множественных лейблов(аналог категорий).
Приложение можно использовать, как локально, так и в production-среде.

### Реализовано
Создание нескольких сущностей и полного цикла CRUD-операций для них.
Реализована связь между полями таблиц по внешнему ключу, как связями OneToMany, так и ManyToMany.
Возможность и необходимость первичный аутентификации(большинство путей не будут доступны без неё).
Начальная инцииализация пользователя, нескольких статусов и лейблов для оперативной работы.
Добавлена фильтрация по параметрам - имя задачи, исполнитель, статусы и лейблы задачи.

### Использующиеся технологии
Фреймворк: Spring Boot
Аутентификация: Spring Security
Автоматический маппинг: Mapstruct
Шаблон проектирования: DTO
Внешнее отслеживание ошибок: Sentry
Документация по API-приложения: Springdoc Openapi, Swagger
Тесты: JUnit 5, Mockwebserver, Datafaker
Отчет о тестах: Jacoco
Линтер: Checkstyle
Базы данных: H2 (внутренняя), PostgreSQL (в production)
Развертывание в production: Docker
Задеплоено на бесплатный сервер от Render: https://java-project-99-b3db.onrender.com

### Создание пользователя
![image](https://github.com/user-attachments/assets/1e8531a5-d886-43a5-a71f-a0a827cb16fb)

![image](https://github.com/user-attachments/assets/010ecc93-df21-4c38-b9a6-064917423c04)

### Создание задачи



