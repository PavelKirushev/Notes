// Package database предоставляет утилиты для подключения к БД.
package database

import (
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	// Регистрируем pgx-драйвер под именем "pgx" для database/sql
	_ "github.com/jackc/pgx/v5/stdlib"
)

// Options — параметры подключения к PostgreSQL.
type Options struct {
	Host     string
	Port     string
	User     string
	Password string
	Name     string
	SSLMode  string
	MaxConns int
}

// NewPostgres создаёт пул соединений к PostgreSQL.
// Повторяет попытку подключения до maxRetries раз (для Docker-среды, где
// БД может стартовать чуть позже бэкенда).
func NewPostgres(opts Options) (*sql.DB, error) {
	dsn := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		opts.Host, opts.Port, opts.User, opts.Password, opts.Name, opts.SSLMode,
	)

	db, err := sql.Open("pgx", dsn)
	if err != nil {
		return nil, fmt.Errorf("open db: %w", err)
	}

	// Настройка пула соединений
	maxConns := opts.MaxConns
	if maxConns <= 0 {
		maxConns = 25
	}
	db.SetMaxOpenConns(maxConns)
	db.SetMaxIdleConns(maxConns / 2)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(2 * time.Minute)

	// Ждём готовности БД (особенно важно при старте в Docker)
	if err := pingWithRetry(db, 10, 2*time.Second); err != nil {
		_ = db.Close()
		return nil, err
	}

	slog.Info("database connected", "host", opts.Host, "name", opts.Name)
	return db, nil
}

func pingWithRetry(db *sql.DB, attempts int, delay time.Duration) error {
	for i := 1; i <= attempts; i++ {
		if err := db.Ping(); err == nil {
			return nil
		}
		slog.Info("waiting for database...", "attempt", i, "max", attempts)
		time.Sleep(delay)
	}
	return fmt.Errorf("database not ready after %d attempts", attempts)
}
