package config

import (
	"os"
	"strconv"
	"time"
)

// Config — корневая конфигурация приложения, загружается из env-переменных.
type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
	Auth     AuthConfig
}

type ServerConfig struct {
	Port            string
	ReadTimeout     time.Duration
	WriteTimeout    time.Duration
	ShutdownTimeout time.Duration
}

type DatabaseConfig struct {
	Host     string
	Port     string
	Name     string
	User     string
	Password string
	SSLMode  string
	MaxConns int
}

type AuthConfig struct {
	JWTSecret  string
	TokenTTL   time.Duration
	BcryptCost int
}

// Load читает конфигурацию из переменных окружения с дефолтными значениями.
func Load() *Config {
	tokenTTL, _ := time.ParseDuration(getEnv("JWT_TOKEN_TTL", "24h"))
	bcryptCost, _ := strconv.Atoi(getEnv("BCRYPT_COST", "12"))
	maxConns, _ := strconv.Atoi(getEnv("DB_MAX_CONNS", "25"))

	return &Config{
		Server: ServerConfig{
			Port:            getEnv("SERVER_PORT", "8080"),
			ReadTimeout:     10 * time.Second,
			WriteTimeout:    10 * time.Second,
			ShutdownTimeout: 30 * time.Second,
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			Name:     getEnv("DB_NAME", "appdb"),
			User:     getEnv("DB_USER", "postgres"),
			Password: getEnv("DB_PASSWORD", "postgres"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			MaxConns: maxConns,
		},
		Auth: AuthConfig{
			JWTSecret:  getEnv("JWT_SECRET", "change-me-in-production"),
			TokenTTL:   tokenTTL,
			BcryptCost: bcryptCost,
		},
	}
}

func getEnv(key, defaultValue string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return defaultValue
}
