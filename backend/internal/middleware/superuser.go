package middleware

import (
	"net/http"

	"github.com/onweg/backend/internal/repository"
)

// SuperUserMiddleware проверяет, что аутентифицированный пользователь — суперпользователь.
// Должен стоять ПОСЛЕ JWTMiddleware.Authenticate.
type SuperUserMiddleware struct {
	userRepo repository.UserRepository
}

func NewSuperUserMiddleware(userRepo repository.UserRepository) *SuperUserMiddleware {
	return &SuperUserMiddleware{userRepo: userRepo}
}

// RequireSuperUser загружает пользователя из БД и проверяет флаг is_super.
func (m *SuperUserMiddleware) RequireSuperUser(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		userID := UserIDFromContext(r.Context())

		user, err := m.userRepo.GetByID(r.Context(), userID)
		if err != nil || !user.IsSuper {
			w.Header().Set("Content-Type", "application/json; charset=utf-8")
			w.WriteHeader(http.StatusForbidden)
			_, _ = w.Write([]byte(`{"success":false,"error":"forbidden: superuser access required"}`))
			return
		}

		next.ServeHTTP(w, r)
	})
}
