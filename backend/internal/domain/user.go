package domain

import (
	"errors"
	"time"
)

// Sentinel errors — используются во всех слоях для явной обработки ошибок.
var (
	ErrUserNotFound      = errors.New("user not found")
	ErrUserAlreadyExists = errors.New("user already exists")
	ErrInvalidPassword   = errors.New("invalid credentials")
	ErrInvalidToken      = errors.New("invalid or expired token")
)

// User — доменная сущность пользователя.
type User struct {
	ID           int64     `json:"id"`
	Email        string    `json:"email"`
	PasswordHash string    `json:"-"`       // никогда не отдаём наружу
	IsSuper      bool      `json:"is_super"` // суперпользователь (только lena)
	CreatedAt    time.Time `json:"created_at"`
	UpdatedAt    time.Time `json:"updated_at"`
}

// RegisterInput — входные данные для регистрации.
type RegisterInput struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

// LoginInput — входные данные для логина.
type LoginInput struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

// AuthResponse — ответ после успешной авторизации.
type AuthResponse struct {
	Token string `json:"token"`
	User  *User  `json:"user"`
}
