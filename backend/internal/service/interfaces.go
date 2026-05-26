// Package service содержит бизнес-логику приложения.
package service

import (
	"context"

	"github.com/onweg/backend/internal/domain"
)

// AuthService — контракт сервиса аутентификации.
type AuthService interface {
	// Register создаёт нового пользователя и возвращает JWT-токен.
	Register(ctx context.Context, input domain.RegisterInput) (*domain.AuthResponse, error)

	// Login проверяет credentials и возвращает JWT-токен.
	Login(ctx context.Context, input domain.LoginInput) (*domain.AuthResponse, error)

	// GetUser возвращает пользователя по ID (для /me endpoint).
	GetUser(ctx context.Context, userID int64) (*domain.User, error)
}

// UserService — контракт сервиса управления пользователями.
type UserService interface {
	// ListAll возвращает всех пользователей. Вызывать только из защищённого маршрута.
	ListAll(ctx context.Context) ([]*domain.User, error)
}

// SubscriptionService — контракт сервиса подписок.
type SubscriptionService interface {
	// GetMySubscription возвращает подписку текущего пользователя.
	GetMySubscription(ctx context.Context, userID int64) (*domain.Subscription, error)

	// SetSubscription выставляет или обновляет подписку (вызывается суперпользователем
	// или в будущем — платёжной системой через webhook).
	SetSubscription(ctx context.Context, input domain.SetSubscriptionInput) (*domain.Subscription, error)
}
