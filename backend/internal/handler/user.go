package handler

import (
	"net/http"

	"github.com/onweg/backend/internal/service"
)

// UserHandler обрабатывает запросы, доступные только суперпользователю.
type UserHandler struct {
	userService service.UserService
}

func NewUserHandler(userService service.UserService) *UserHandler {
	return &UserHandler{userService: userService}
}

// List godoc
// GET /api/v1/users
// Header: Authorization: Bearer <superuser-token>
// Возвращает список всех пользователей. Доступно только lena (is_super=true).
func (h *UserHandler) List(w http.ResponseWriter, r *http.Request) {
	users, err := h.userService.ListAll(r.Context())
	if err != nil {
		writeError(w, http.StatusInternalServerError, "internal server error")
		return
	}

	writeSuccess(w, http.StatusOK, users)
}
