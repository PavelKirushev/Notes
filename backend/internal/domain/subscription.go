package domain

import (
	"errors"
	"time"
)

var (
	ErrSubscriptionNotFound = errors.New("subscription not found")
)

// SubscriptionStatus — возможные статусы подписки.
type SubscriptionStatus string

const (
	SubscriptionActive    SubscriptionStatus = "active"
	SubscriptionInactive  SubscriptionStatus = "inactive"
	SubscriptionExpired   SubscriptionStatus = "expired"
	SubscriptionCancelled SubscriptionStatus = "cancelled"
)

// Subscription — доменная сущность подписки пользователя.
type Subscription struct {
	ID        int64              `json:"id"`
	UserID    int64              `json:"user_id"`
	Status    SubscriptionStatus `json:"status"`
	Plan      string             `json:"plan"`
	StartedAt *time.Time         `json:"started_at"`
	ExpiresAt *time.Time         `json:"expires_at"`
	CreatedAt time.Time          `json:"created_at"`
	UpdatedAt time.Time          `json:"updated_at"`
}

// IsActive возвращает true если подписка активна и не истекла.
func (s *Subscription) IsActive() bool {
	if s == nil {
		return false
	}
	return s.Status == SubscriptionActive &&
		s.ExpiresAt != nil &&
		s.ExpiresAt.After(time.Now())
}

// SetSubscriptionInput — входные данные для выставления подписки (суперпользователь).
type SetSubscriptionInput struct {
	UserID    int64      `json:"user_id"`
	Plan      string     `json:"plan"`
	ExpiresAt time.Time  `json:"expires_at"`
}
