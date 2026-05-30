package handler

import (
	"net/http"

	"github.com/onweg/backend/internal/service"
)

type UserHandler struct {
	userService service.UserService
}

func NewUserHandler(userService service.UserService) *UserHandler {
	return &UserHandler{userService: userService}
}

// List godoc
// GET /api/v1/admin/users
// Возвращает всех пользователей с информацией об их подписках.
func (h *UserHandler) List(w http.ResponseWriter, r *http.Request) {
	users, err := h.userService.ListAllWithSubscriptions(r.Context())
	if err != nil {
		writeError(w, http.StatusInternalServerError, "internal server error")
		return
	}
	writeSuccess(w, http.StatusOK, users)
}
