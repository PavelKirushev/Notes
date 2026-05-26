package service

import (
	"context"
	"errors"
	"fmt"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/repository"
	"github.com/onweg/backend/pkg/hash"
	"github.com/onweg/backend/pkg/jwt"
)

// compile-time проверка реализации интерфейса
var _ AuthService = (*authService)(nil)

type authService struct {
	userRepo   repository.UserRepository
	hasher     hash.Hasher
	jwtManager *jwt.Manager
}

// NewAuthService создаёт сервис аутентификации с внедрёнными зависимостями.
func NewAuthService(
	userRepo repository.UserRepository,
	hasher hash.Hasher,
	jwtManager *jwt.Manager,
) *authService {
	return &authService{
		userRepo:   userRepo,
		hasher:     hasher,
		jwtManager: jwtManager,
	}
}

// Register регистрирует нового пользователя.
// Шифрует пароль bcrypt'ом и выдаёт JWT-токен.
func (s *authService) Register(ctx context.Context, input domain.RegisterInput) (*domain.AuthResponse, error) {
	// Проверяем уникальность email
	_, err := s.userRepo.GetByEmail(ctx, input.Email)
	if err == nil {
		return nil, domain.ErrUserAlreadyExists
	}
	if !errors.Is(err, domain.ErrUserNotFound) {
		return nil, fmt.Errorf("service: check email: %w", err)
	}

	// Хэшируем пароль
	passwordHash, err := s.hasher.Hash(input.Password)
	if err != nil {
		return nil, fmt.Errorf("service: hash password: %w", err)
	}

	// Сохраняем пользователя
	user, err := s.userRepo.Create(ctx, &domain.User{
		Email:        input.Email,
		PasswordHash: passwordHash,
	})
	if err != nil {
		return nil, fmt.Errorf("service: create user: %w", err)
	}

	// Генерируем JWT
	token, err := s.jwtManager.Generate(user.ID)
	if err != nil {
		return nil, fmt.Errorf("service: generate token: %w", err)
	}

	return &domain.AuthResponse{Token: token, User: user}, nil
}

// Login проверяет credentials и возвращает токен.
func (s *authService) Login(ctx context.Context, input domain.LoginInput) (*domain.AuthResponse, error) {
	user, err := s.userRepo.GetByEmail(ctx, input.Email)
	if err != nil {
		// Не раскрываем, что именно не так — email или пароль
		if errors.Is(err, domain.ErrUserNotFound) {
			return nil, domain.ErrInvalidPassword
		}
		return nil, fmt.Errorf("service: get user: %w", err)
	}

	if !s.hasher.Compare(user.PasswordHash, input.Password) {
		return nil, domain.ErrInvalidPassword
	}

	token, err := s.jwtManager.Generate(user.ID)
	if err != nil {
		return nil, fmt.Errorf("service: generate token: %w", err)
	}

	return &domain.AuthResponse{Token: token, User: user}, nil
}

// GetUser возвращает пользователя по ID.
func (s *authService) GetUser(ctx context.Context, userID int64) (*domain.User, error) {
	return s.userRepo.GetByID(ctx, userID)
}
