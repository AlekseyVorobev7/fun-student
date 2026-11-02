# Fun Student

Учебное приложение для управления студентами и пользователями на Spring Boot 4.
Есть REST API с JWT-авторизацией и веб-интерфейс на Thymeleaf.

## Стек

- Java 21, Spring Boot 4
- Spring Security (JWT для API, form login для веба)
- Spring Data JPA + PostgreSQL 17
- Thymeleaf
- Lombok
- Docker Compose (для базы)

## Быстрый старт

1. Поднять PostgreSQL:

   ```bash
   docker compose up -d
   ```

   База `students_db` будет доступна на `localhost:5433` (логин и пароль `postgres`).

2. Запустить приложение:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Открыть http://localhost:8080.

Если порт 8080 занят:

```bash
SERVER_PORT=8090 ./mvnw spring-boot:run
```

## Настройки

Все параметры задаются переменными окружения:

| Переменная    | По умолчанию                                    |
|---------------|-------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5433/students_db`  |
| `DB_USERNAME` | `postgres`                                      |
| `DB_PASSWORD` | `postgres`                                      |
| `SERVER_PORT` | `8080`                                          |

Таблицы создаются автоматически в схеме `students_schema` (`ddl-auto: update`).

## Тестовые пользователи

Создаются при первом запуске:

| Email                    | Пароль       | Роль        |
|--------------------------|--------------|-------------|
| `superadmin@example.com` | `superAdmin` | SUPER_ADMIN |
| `admin@example.com`      | `admin`      | ADMIN       |
| `teacher@example.com`    | `teacher`    | TEACHER     |

Также добавляются 5 тестовых студентов.

## Роли и доступ

| Действие                          | TEACHER | ADMIN | SUPER_ADMIN |
|-----------------------------------|:-------:|:-----:|:-----------:|
| Просмотр студентов                | ✅      | ✅    |             |
| Создание, изменение, удаление     |         | ✅    |             |
| Управление пользователями         |         |       | ✅          |

## Веб-интерфейс

| URL                    | Описание                         |
|------------------------|----------------------------------|
| `/login`               | Вход                             |
| `/students`            | Список студентов                 |
| `/students/{id}`       | Карточка студента                |
| `/students/new`        | Добавление студента              |
| `/students/{id}/edit`  | Изменение телефона и email       |
| `/users`               | Список пользователей             |
| `/users/new`           | Создание пользователя            |

## REST API

Авторизация:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"admin"}'
```

В ответе приходит `token`. Его нужно передавать в заголовке `Authorization: Bearer <token>`.

| Метод  | URL                          | Роль               |
|--------|------------------------------|--------------------|
| GET    | `/api/v1/student`            | ADMIN, TEACHER     |
| GET    | `/api/v1/student/{id}`       | ADMIN, TEACHER     |
| POST   | `/api/v1/student/save`       | ADMIN              |
| PUT    | `/api/v1/student/{id}`       | ADMIN              |
| DELETE | `/api/v1/student/{id}`       | ADMIN              |
| GET    | `/api/v1/users`              | SUPER_ADMIN        |
| POST   | `/api/v1/users/save`         | SUPER_ADMIN        |
| DELETE | `/api/v1/users/{id}`         | SUPER_ADMIN        |

## Сборка

```bash
./mvnw clean package
java -jar target/student-0.0.1-SNAPSHOT.jar
```
