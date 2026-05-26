package service

import (
	"context"
	"errors"
	"fmt"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/repository"
)

var _ SubscriptionService = (*subscriptionService)(nil)

type subscriptionService struct {
	subRepo repository.SubscriptionRepository
}

func NewSubscriptionService(subRepo repository.SubscriptionRepository) *subscriptionService {
	return &subscriptionService{subRepo: subRepo}
}

// GetMySubscription возвращает подписку пользователя.
// Если подписки нет — возвращает nil, nil (не ошибка, просто нет подписки).
func (s *subscriptionService) GetMySubscription(ctx context.Context, userID int64) (*domain.Subscription, error) {
	sub, err := s.subRepo.GetByUserID(ctx, userID)
	if err != nil {
		if errors.Is(err, domain.ErrSubscriptionNotFound) {
			return nil, nil // нет подписки — это нормально
		}
		return nil, fmt.Errorf("subscription service: get: %w", err)
	}
	return sub, nil
}

// SetSubscription создаёт или обновляет подписку пользователя.
// Сейчас вызывается суперпользователем вручную.
// В будущем — платёжной системой через webhook после успешной оплаты.
func (s *subscriptionService) SetSubscription(ctx context.Context, input domain.SetSubscriptionInput) (*domain.Subscription, error) {
	sub := &domain.Subscription{
		UserID:    input.UserID,
		Status:    domain.SubscriptionActive,
		Plan:      input.Plan,
		ExpiresAt: &input.ExpiresAt,
	}

	result, err := s.subRepo.Upsert(ctx, sub)
	if err != nil {
		return nil, fmt.Errorf("subscription service: set: %w", err)
	}

	return result, nil
}
