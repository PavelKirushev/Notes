// Package repository определяет контракты доступа к данным.
// Конкретные реализации (postgres, mock и т.д.) живут в подпакетах.
package repository

import (
	"context"

	"github.com/onweg/backend/internal/domain"
)

// SubscriptionRepository — контракт для работы с подписками.
type SubscriptionRepository interface {
	GetByUserID(ctx context.Context, userID int64) (*domain.Subscription, error)
	Upsert(ctx context.Context, sub *domain.Subscription) (*domain.Subscription, error)
	Cancel(ctx context.Context, userID int64) error
}

// UserRepository — контракт для работы с пользователями в хранилище.
type UserRepository interface {
	// Create сохраняет нового пользователя и возвращает его с заполненными полями.
	Create(ctx context.Context, user *domain.User) (*domain.User, error)

	// GetByEmail ищет пользователя по email; возвращает domain.ErrUserNotFound если не найден.
	GetByEmail(ctx context.Context, email string) (*domain.User, error)

	// GetByID ищет пользователя по ID; возвращает domain.ErrUserNotFound если не найден.
	GetByID(ctx context.Context, id int64) (*domain.User, error)

	GetAll(ctx context.Context) ([]*domain.User, error)
	GetAllWithSubscriptions(ctx context.Context) ([]*domain.UserWithSubscription, error)
}
