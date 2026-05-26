.PHONY: up down build logs ps run tidy lint test

# ── Docker ────────────────────────────────────────────────────────────────────

## Поднять оба контейнера (пересобрать образ если нужно)
up:
	docker-compose up -d --build

## Остановить и удалить контейнеры
down:
	docker-compose down

## Пересобрать образ backend
build:
	docker-compose build backend

## Посмотреть логи
logs:
	docker-compose logs -f

## Статус контейнеров
ps:
	docker-compose ps

# ── Локальная разработка ─────────────────────────────────────────────────────

## Запустить сервер локально (нужна локальная БД или .env с DB_HOST=localhost)
run:
	go run ./cmd/server

## Скачать зависимости
tidy:
	go mod tidy

## Запустить тесты
test:
	go test ./... -v -race

## Линтер (нужен golangci-lint)
lint:
	golangci-lint run ./...
