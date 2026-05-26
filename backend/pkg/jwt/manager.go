// Package jwt предоставляет генерацию и валидацию JWT-токенов.
package jwt

import (
	"errors"
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

// Manager управляет жизненным циклом JWT-токенов.
type Manager struct {
	secretKey []byte
	tokenTTL  time.Duration
}

// claims — приватная структура с полезной нагрузкой токена.
type claims struct {
	jwt.RegisteredClaims
	UserID int64 `json:"uid"`
}

// NewManager создаёт новый JWT-менеджер.
func NewManager(secretKey string, tokenTTL time.Duration) *Manager {
	return &Manager{
		secretKey: []byte(secretKey),
		tokenTTL:  tokenTTL,
	}
}

// Generate создаёт подписанный JWT для указанного userID.
func (m *Manager) Generate(userID int64) (string, error) {
	now := time.Now()
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, &claims{
		RegisteredClaims: jwt.RegisteredClaims{
			IssuedAt:  jwt.NewNumericDate(now),
			ExpiresAt: jwt.NewNumericDate(now.Add(m.tokenTTL)),
		},
		UserID: userID,
	})

	signed, err := token.SignedString(m.secretKey)
	if err != nil {
		return "", fmt.Errorf("jwt: sign token: %w", err)
	}

	return signed, nil
}

// Validate парсит и проверяет токен. Возвращает userID или ошибку.
func (m *Manager) Validate(tokenString string) (int64, error) {
	token, err := jwt.ParseWithClaims(tokenString, &claims{}, func(t *jwt.Token) (interface{}, error) {
		if _, ok := t.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("jwt: unexpected signing method: %v", t.Header["alg"])
		}
		return m.secretKey, nil
	})
	if err != nil {
		return 0, fmt.Errorf("jwt: parse: %w", err)
	}

	c, ok := token.Claims.(*claims)
	if !ok || !token.Valid {
		return 0, errors.New("jwt: invalid token claims")
	}

	return c.UserID, nil
}
