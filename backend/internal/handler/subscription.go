package handler

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"

	"github.com/onweg/backend/internal/domain"
	"github.com/onweg/backend/internal/middleware"
	"github.com/onweg/backend/internal/service"
)

type SubscriptionHandler struct {
	subService service.SubscriptionService
}

func NewSubscriptionHandler(subService service.SubscriptionService) *SubscriptionHandler {
	return &SubscriptionHandler{subService: subService}
}

// GetMy godoc
// GET /api/v1/subscriptions/me
// Возвращает подписку текущего пользователя.
// Если подписки нет — { active: false, subscription: null }
func (h *SubscriptionHandler) GetMy(w http.ResponseWriter, r *http.Request) {
	userID := middleware.UserIDFromContext(r.Context())

	sub, err := h.subService.GetMySubscription(r.Context(), userID)
	if err != nil {
		writeError(w, http.StatusInternalServerError, "internal server error")
		return
	}

	writeSuccess(w, http.StatusOK, map[string]interface{}{
		"active":       sub.IsActive(),
		"subscription": sub,
	})
}

// Set godoc
// PUT /api/v1/admin/users/{userID}/subscription
// Только суперпользователь. Выставляет подписку пользователю.
// Body: { "plan": "basic", "expires_at": "2026-12-31T00:00:00Z" }
func (h *SubscriptionHandler) Set(w http.ResponseWriter, r *http.Request) {
	userIDStr := chi.URLParam(r, "userID")
	userID, err := strconv.ParseInt(userIDStr, 10, 64)
	if err != nil || userID <= 0 {
		writeError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	var body struct {
		Plan      string    `json:"plan"`
		ExpiresAt time.Time `json:"expires_at"`
	}
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	if body.Plan == "" {
		body.Plan = "basic"
	}
	if body.ExpiresAt.IsZero() {
		writeError(w, http.StatusUnprocessableEntity, "expires_at is required")
		return
	}

	sub, err := h.subService.SetSubscription(r.Context(), domain.SetSubscriptionInput{
		UserID:    userID,
		Plan:      body.Plan,
		ExpiresAt: body.ExpiresAt,
	})
	if err != nil {
		writeError(w, http.StatusInternalServerError, "internal server error")
		return
	}

	writeSuccess(w, http.StatusOK, sub)
}
