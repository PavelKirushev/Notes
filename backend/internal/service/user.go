package service

import (
	"context"
	"fmt"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/repository"
)

var _ UserService = (*userService)(nil)

type userService struct {
	userRepo repository.UserRepository
}

// NewUserService создаёт сервис для работы с пользователями.
func NewUserService(userRepo repository.UserRepository) *userService {
	return &userService{userRepo: userRepo}
}

// ListAll возвращает всех пользователей из БД.
func (s *userService) ListAll(ctx context.Context) ([]*domain.User, error) {
	users, err := s.userRepo.GetAll(ctx)
	if err != nil {
		return nil, fmt.Errorf("user service: list all: %w", err)
	}
	return users, nil
}

// ListAllWithSubscriptions возвращает всех пользователей с их подписками.
func (s *userService) ListAllWithSubscriptions(ctx context.Context) ([]*domain.UserWithSubscription, error) {
	users, err := s.userRepo.GetAllWithSubscriptions(ctx)
	if err != nil {
		return nil, fmt.Errorf("user service: list all with subscriptions: %w", err)
	}
	return users, nil
}
