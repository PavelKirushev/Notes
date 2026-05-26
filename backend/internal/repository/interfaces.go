// Package repository определяет контракты доступа к данным.
// Конкретные реализации (postgres, mock и т.д.) живут в подпакетах.
package repository

import (
	"context"

	"github.com/onweg/backend/internal/domain"
)

// UserRepository — контракт для работы с пользователями в хранилище.
type UserRepository interface {
	// Create сохраняет нового пользователя и возвращает его с заполненными полями.
	Create(ctx context.Context, user *domain.User) (*domain.User, error)

	// GetByEmail ищет пользователя по email; возвращает domain.ErrUserNotFound если не найден.
	GetByEmail(ctx context.Context, email string) (*domain.User, error)

	// GetByID ищет пользователя по ID; возвращает domain.ErrUserNotFound если не найден.
	GetByID(ctx context.Context, id int64) (*domain.User, error)

	// GetAll возвращает всех пользователей (только для суперпользователя).
	GetAll(ctx context.Context) ([]*domain.User, error)
}
