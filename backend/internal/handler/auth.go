// Package handler содержит HTTP-обработчики (Controller в MVC).
// Каждый обработчик: парсит запрос → вызывает service → формирует ответ.
package handler

import (
	"encoding/json"
	"errors"
	"net/http"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/middleware"
	"github.com/onweg/backend/internal/service"
)

// AuthHandler обрабатывает запросы аутентификации.
type AuthHandler struct {
	authService service.AuthService
}

func NewAuthHandler(authService service.AuthService) *AuthHandler {
	return &AuthHandler{authService: authService}
}

// Register godoc
// POST /api/v1/auth/register
// Body: {"email": "user@example.com", "password": "secret123"}
func (h *AuthHandler) Register(w http.ResponseWriter, r *http.Request) {
	var input domain.RegisterInput
	if err := json.NewDecoder(r.Body).Decode(&input); err != nil {
		writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := validateRegisterInput(input); err != nil {
		writeError(w, http.StatusUnprocessableEntity, err.Error())
		return
	}

	resp, err := h.authService.Register(r.Context(), input)
	if err != nil {
		switch {
		case errors.Is(err, domain.ErrUserAlreadyExists):
			writeError(w, http.StatusConflict, "email already registered")
		default:
			writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	writeSuccess(w, http.StatusCreated, resp)
}

// Login godoc
// POST /api/v1/auth/login
// Body: {"email": "user@example.com", "password": "secret123"}
func (h *AuthHandler) Login(w http.ResponseWriter, r *http.Request) {
	var input domain.LoginInput
	if err := json.NewDecoder(r.Body).Decode(&input); err != nil {
		writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if input.Email == "" || input.Password == "" {
		writeError(w, http.StatusUnprocessableEntity, "email and password are required")
		return
	}

	resp, err := h.authService.Login(r.Context(), input)
	if err != nil {
		switch {
		case errors.Is(err, domain.ErrInvalidPassword):
			writeError(w, http.StatusUnauthorized, "invalid email or password")
		default:
			writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	writeSuccess(w, http.StatusOK, resp)
}

// Me godoc
// GET /api/v1/auth/me
// Header: Authorization: Bearer <token>
func (h *AuthHandler) Me(w http.ResponseWriter, r *http.Request) {
	userID := middleware.UserIDFromContext(r.Context())

	user, err := h.authService.GetUser(r.Context(), userID)
	if err != nil {
		switch {
		case errors.Is(err, domain.ErrUserNotFound):
			writeError(w, http.StatusNotFound, "user not found")
		default:
			writeError(w, http.StatusInternalServerError, "internal server error")
		}
		return
	}

	writeSuccess(w, http.StatusOK, user)
}

// --- валидация ---

func validateRegisterInput(input domain.RegisterInput) error {
	if input.Email == "" {
		return errors.New("email is required")
	}
	if input.Password == "" {
		return errors.New("password is required")
	}
	if len(input.Password) < 8 {
		return errors.New("password must be at least 8 characters")
	}
	return nil
}
