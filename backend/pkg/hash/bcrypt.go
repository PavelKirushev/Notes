// Package hash предоставляет интерфейс и реализации хэширования паролей.
package hash

import (
	"fmt"

	"golang.org/x/crypto/bcrypt"
)

// Hasher — контракт для хэширования и сравнения паролей.
// Позволяет легко заменить bcrypt на другой алгоритм в тестах или будущих версиях.
type Hasher interface {
	Hash(password string) (string, error)
	Compare(hash, password string) bool
}

// BcryptHasher — реализация Hasher через bcrypt.
type BcryptHasher struct {
	cost int
}

// compile-time проверка интерфейса
var _ Hasher = (*BcryptHasher)(nil)

// NewBcrypt создаёт BcryptHasher с указанной стоимостью (cost).
// Рекомендуется cost >= 12 для продакшна.
func NewBcrypt(cost int) *BcryptHasher {
	if cost < bcrypt.MinCost || cost > bcrypt.MaxCost {
		cost = bcrypt.DefaultCost
	}
	return &BcryptHasher{cost: cost}
}

// Hash возвращает bcrypt-хэш пароля.
func (h *BcryptHasher) Hash(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), h.cost)
	if err != nil {
		return "", fmt.Errorf("bcrypt: hash password: %w", err)
	}
	return string(bytes), nil
}

// Compare проверяет соответствие пароля хэшу.
// Возвращает true если совпадают, false в любом другом случае (константное время).
func (h *BcryptHasher) Compare(hash, password string) bool {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}
