package postgres

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"time"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/repository"
)

var _ repository.SubscriptionRepository = (*SubscriptionRepository)(nil)

type SubscriptionRepository struct {
	db *sql.DB
}

func NewSubscriptionRepository(db *sql.DB) *SubscriptionRepository {
	return &SubscriptionRepository{db: db}
}

// GetByUserID возвращает подписку пользователя.
func (r *SubscriptionRepository) GetByUserID(ctx context.Context, userID int64) (*domain.Subscription, error) {
	const query = `
		SELECT id, user_id, status, plan, started_at, expires_at, created_at, updated_at
		FROM subscriptions
		WHERE user_id = $1
	`

	sub := &domain.Subscription{}
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&sub.ID,
		&sub.UserID,
		&sub.Status,
		&sub.Plan,
		&sub.StartedAt,
		&sub.ExpiresAt,
		&sub.CreatedAt,
		&sub.UpdatedAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.ErrSubscriptionNotFound
		}
		return nil, fmt.Errorf("postgres: get subscription: %w", err)
	}

	return sub, nil
}

// Upsert создаёт подписку если её нет, или обновляет существующую.
// Когда подключишь платёжную систему — она будет вызывать именно этот метод.
func (r *SubscriptionRepository) Upsert(ctx context.Context, sub *domain.Subscription) (*domain.Subscription, error) {
	const query = `
		INSERT INTO subscriptions (user_id, status, plan, started_at, expires_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, NOW(), NOW())
		ON CONFLICT (user_id) DO UPDATE SET
			status     = EXCLUDED.status,
			plan       = EXCLUDED.plan,
			started_at = EXCLUDED.started_at,
			expires_at = EXCLUDED.expires_at,
			updated_at = NOW()
		RETURNING id, user_id, status, plan, started_at, expires_at, created_at, updated_at
	`

	now := time.Now()
	result := &domain.Subscription{}
	err := r.db.QueryRowContext(ctx, query,
		sub.UserID,
		sub.Status,
		sub.Plan,
		now,
		sub.ExpiresAt,
	).Scan(
		&result.ID,
		&result.UserID,
		&result.Status,
		&result.Plan,
		&result.StartedAt,
		&result.ExpiresAt,
		&result.CreatedAt,
		&result.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("postgres: upsert subscription: %w", err)
	}

	return result, nil
}
