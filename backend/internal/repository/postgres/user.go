// Package postgres содержит PostgreSQL-реализацию репозиториев.
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

// compile-time проверка реализации интерфейса
var _ repository.UserRepository = (*UserRepository)(nil)

// UserRepository реализует repository.UserRepository поверх *sql.DB.
type UserRepository struct {
	db *sql.DB
}

func NewUserRepository(db *sql.DB) *UserRepository {
	return &UserRepository{db: db}
}

// Create вставляет нового пользователя и возвращает его с id и временными метками.
func (r *UserRepository) Create(ctx context.Context, user *domain.User) (*domain.User, error) {
	const query = `
		INSERT INTO users (email, password_hash, is_super, created_at, updated_at)
		VALUES ($1, $2, $3, NOW(), NOW())
		RETURNING id, email, password_hash, is_super, created_at, updated_at
	`

	created := &domain.User{}
	err := r.db.QueryRowContext(ctx, query, user.Email, user.PasswordHash, user.IsSuper).Scan(
		&created.ID,
		&created.Email,
		&created.PasswordHash,
		&created.IsSuper,
		&created.CreatedAt,
		&created.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("postgres: create user: %w", err)
	}

	return created, nil
}

// GetByEmail ищет пользователя по email.
func (r *UserRepository) GetByEmail(ctx context.Context, email string) (*domain.User, error) {
	const query = `
		SELECT id, email, password_hash, is_super, created_at, updated_at
		FROM users
		WHERE email = $1
	`

	user := &domain.User{}
	err := r.db.QueryRowContext(ctx, query, email).Scan(
		&user.ID,
		&user.Email,
		&user.PasswordHash,
		&user.IsSuper,
		&user.CreatedAt,
		&user.UpdatedAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.ErrUserNotFound
		}
		return nil, fmt.Errorf("postgres: get user by email: %w", err)
	}

	return user, nil
}

// GetByID ищет пользователя по ID.
func (r *UserRepository) GetByID(ctx context.Context, id int64) (*domain.User, error) {
	const query = `
		SELECT id, email, password_hash, is_super, created_at, updated_at
		FROM users
		WHERE id = $1
	`

	user := &domain.User{}
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&user.ID,
		&user.Email,
		&user.PasswordHash,
		&user.IsSuper,
		&user.CreatedAt,
		&user.UpdatedAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, domain.ErrUserNotFound
		}
		return nil, fmt.Errorf("postgres: get user by id: %w", err)
	}

	return user, nil
}

// GetAllWithSubscriptions возвращает всех пользователей с их подписками (LEFT JOIN).
func (r *UserRepository) GetAllWithSubscriptions(ctx context.Context) ([]*domain.UserWithSubscription, error) {
	const query = `
		SELECT u.id, u.email, u.is_super, u.created_at,
		       s.id, s.status, s.plan, s.started_at, s.expires_at
		FROM users u
		LEFT JOIN subscriptions s ON s.user_id = u.id
		ORDER BY u.id ASC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("postgres: get all with subscriptions: %w", err)
	}
	defer rows.Close()

	var result []*domain.UserWithSubscription
	for rows.Next() {
		u := &domain.UserWithSubscription{}
		var (
			subID        *int64
			subStatus    *string
			subPlan      *string
			subStartedAt *time.Time
			subExpiresAt *time.Time
		)
		if err := rows.Scan(
			&u.ID, &u.Email, &u.IsSuper, &u.CreatedAt,
			&subID, &subStatus, &subPlan, &subStartedAt, &subExpiresAt,
		); err != nil {
			return nil, fmt.Errorf("postgres: scan user with subscription: %w", err)
		}
		if subID != nil {
			u.Subscription = &domain.Subscription{
				ID:        *subID,
				UserID:    u.ID,
				Status:    domain.SubscriptionStatus(*subStatus),
				Plan:      *subPlan,
				StartedAt: subStartedAt,
				ExpiresAt: subExpiresAt,
			}
			u.HasActive = u.Subscription.IsActive()
		}
		result = append(result, u)
	}
	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("postgres: rows error: %w", err)
	}
	return result, nil
}

// GetAll возвращает список всех пользователей (для суперпользователя).
func (r *UserRepository) GetAll(ctx context.Context) ([]*domain.User, error) {
	const query = `
		SELECT id, email, is_super, created_at, updated_at
		FROM users
		ORDER BY id ASC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("postgres: get all users: %w", err)
	}
	defer rows.Close()

	var users []*domain.User
	for rows.Next() {
		u := &domain.User{}
		if err := rows.Scan(&u.ID, &u.Email, &u.IsSuper, &u.CreatedAt, &u.UpdatedAt); err != nil {
			return nil, fmt.Errorf("postgres: scan user: %w", err)
		}
		users = append(users, u)
	}
	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("postgres: rows error: %w", err)
	}

	return users, nil
}
