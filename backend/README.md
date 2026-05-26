# Backend — Go + PostgreSQL + Docker

Чистый Go-бэкенд с JWT-аутентификацией и bcrypt-шифрованием паролей.

## Стек

| Слой | Технология |
|---|---|
| Язык | Go 1.21 |
| HTTP-роутер | [chi v5](https://github.com/go-chi/chi) |
| БД | PostgreSQL 16 |
| Драйвер БД | pgx v5 |
| JWT | golang-jwt/jwt v5 |
| Пароли | bcrypt (golang.org/x/crypto) |
| Контейнеры | Docker + docker-compose |

## Архитектура

```
cmd/server/main.go          — точка входа, DI, graceful shutdown
internal/
  config/                   — загрузка конфига из ENV
  domain/                   — сущности (User) и sentinel errors
  repository/               — интерфейс UserRepository
  repository/postgres/      — SQL-реализация
  service/                  — бизнес-логика (Register, Login, GetUser)
  handler/                  — HTTP-обработчики (Controller в MVC)
  middleware/                — JWT-аутентификация
pkg/
  database/                 — пул соединений postgres (с retry)
  jwt/                      — генерация/валидация токенов
  hash/                     — bcrypt-хэшер
migrations/
  000001_init.up.sql        — создание таблицы users
```

**Поток запроса:**
```
HTTP Request → chi Router → Middleware → Handler → Service → Repository → PostgreSQL
```

## Быстрый старт

```bash
# 1. Клонируй / перейди в папку
cd backend

# 2. Поднять оба контейнера (первый раз ~1 мин — скачает образы)
docker-compose up -d --build

# 3. Проверить статус
docker-compose ps

# 4. Логи бэкенда
docker-compose logs -f backend
```

## API

Base URL: `http://localhost:8080`

### Health check
```
GET /health
→ {"status":"ok"}
```

### Регистрация
```
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "secret123"
}

→ 201 Created
{
  "success": true,
  "data": {
    "token": "<JWT>",
    "user": { "id": 1, "email": "user@example.com", "created_at": "..." }
  }
}
```

### Логин
```
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "secret123"
}

→ 200 OK
{
  "success": true,
  "data": { "token": "<JWT>", "user": {...} }
}
```

### Профиль (защищённый эндпоинт)
```
GET /api/v1/auth/me
Authorization: Bearer <JWT>

→ 200 OK
{
  "success": true,
  "data": { "id": 1, "email": "user@example.com", ... }
}
```

## Переменные окружения

| Переменная | Дефолт | Описание |
|---|---|---|
| SERVER_PORT | 8080 | Порт сервера |
| DB_HOST | localhost | Хост БД |
| DB_PORT | 5432 | Порт БД |
| DB_NAME | appdb | Имя БД |
| DB_USER | postgres | Пользователь БД |
| DB_PASSWORD | postgres | Пароль БД |
| JWT_SECRET | change-me | Секрет для подписи JWT |
| JWT_TOKEN_TTL | 24h | Время жизни токена |
| BCRYPT_COST | 12 | Стоимость bcrypt (10-14) |

## Локальная разработка

```bash
# Только БД в Docker, Go-сервер локально
docker-compose up -d postgres

cp .env.example .env
# Отредактируй .env (DB_HOST=localhost)

go run ./cmd/server
```

## Расширение

Добавить новый ресурс (например, Posts):
1. `internal/domain/post.go` — сущность
2. `internal/repository/interfaces.go` — добавить `PostRepository`
3. `internal/repository/postgres/post.go` — SQL-реализация
4. `internal/service/` — бизнес-логика
5. `internal/handler/post.go` — HTTP-обработчики
6. `cmd/server/main.go` — зарегистрировать маршруты
7. `migrations/` — новый SQL-файл
