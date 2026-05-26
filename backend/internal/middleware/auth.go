// Package middleware содержит HTTP-мидлвари.
package middleware

import (
	"context"
	"net/http"
	"strings"

	"github.com/onweg/backend/pkg/jwt"
)

// contextKey — приватный тип для ключей контекста (исключает коллизии).
type contextKey string

const userIDContextKey contextKey = "user_id"

// JWTMiddleware проверяет Bearer-токен из заголовка Authorization.
type JWTMiddleware struct {
	jwtManager *jwt.Manager
}

func NewJWTMiddleware(jwtManager *jwt.Manager) *JWTMiddleware {
	return &JWTMiddleware{jwtManager: jwtManager}
}

// Authenticate — chi-совместимый middleware, кладёт userID в context.
func (m *JWTMiddleware) Authenticate(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, `{"success":false,"error":"authorization header required"}`, http.StatusUnauthorized)
			return
		}

		parts := strings.SplitN(authHeader, " ", 2)
		if len(parts) != 2 || !strings.EqualFold(parts[0], "bearer") {
			http.Error(w, `{"success":false,"error":"invalid authorization header format"}`, http.StatusUnauthorized)
			return
		}

		userID, err := m.jwtManager.Validate(parts[1])
		if err != nil {
			http.Error(w, `{"success":false,"error":"invalid or expired token"}`, http.StatusUnauthorized)
			return
		}

		ctx := context.WithValue(r.Context(), userIDContextKey, userID)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// UserIDFromContext извлекает userID из context.
// Возвращает 0 если значение не установлено (не должно случаться за Authenticate).
func UserIDFromContext(ctx context.Context) int64 {
	id, _ := ctx.Value(userIDContextKey).(int64)
	return id
}
