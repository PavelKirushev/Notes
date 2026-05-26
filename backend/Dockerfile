# ── Stage 1: builder ──────────────────────────────────────────────────────────
FROM golang:1.21-alpine AS builder

# Устанавливаем зависимости для CGO (не нужны, но на всякий случай)
RUN apk add --no-cache git

WORKDIR /app

# Копируем go.mod/go.sum отдельно — Docker кэширует этот слой
# и не перекачивает модули при изменении исходников
COPY go.mod go.sum ./
RUN go mod download

# Копируем исходники
COPY . .

# Собираем бинарник: CGO_ENABLED=0 → статически слинкованный исполняемый файл
# -ldflags="-w -s" → убираем debug-информацию (меньше размер)
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build -ldflags="-w -s" -o /app/server ./cmd/server

# ── Stage 2: runtime ──────────────────────────────────────────────────────────
FROM alpine:3.19

# ca-certificates нужны для HTTPS-запросов; tzdata — для временных зон
RUN apk add --no-cache ca-certificates tzdata && \
    addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Копируем только бинарник из builder-стейджа (финальный образ < 20 МБ)
COPY --from=builder /app/server .

# Запускаем под непривилегированным пользователем
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost:8080/health || exit 1

CMD ["./server"]
