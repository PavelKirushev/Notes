package handler

import (
	"encoding/json"
	"log/slog"
	"net/http"

	authmw "github.com/onweg/backend/internal/middleware"
	"github.com/onweg/backend/internal/repository"
	"github.com/onweg/backend/pkg/gemini"
)

type SummaryHandler struct {
	gemini   *gemini.Client
	userRepo repository.UserRepository
	subRepo  repository.SubscriptionRepository
}

func NewSummaryHandler(
	gemini *gemini.Client,
	userRepo repository.UserRepository,
	subRepo repository.SubscriptionRepository,
) *SummaryHandler {
	return &SummaryHandler{gemini: gemini, userRepo: userRepo, subRepo: subRepo}
}

type summaryRequest struct {
	Text string `json:"text"`
}

// Summarize godoc
// POST /api/v1/notes/summary
// Доступно только пользователям с активной подпиской (или суперпользователю).
func (h *SummaryHandler) Summarize(w http.ResponseWriter, r *http.Request) {
	userID := authmw.UserIDFromContext(r.Context())

	// Проверяем доступ: суперпользователь ИЛИ активная подписка
	user, err := h.userRepo.GetByID(r.Context(), userID)
	if err != nil {
		writeError(w, http.StatusUnauthorized, "user not found")
		return
	}

	if !user.IsSuper {
		sub, err := h.subRepo.GetByUserID(r.Context(), userID)
		if err != nil || !sub.IsActive() {
			writeError(w, http.StatusForbidden, "subscription required")
			return
		}
	}

	var req summaryRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil || req.Text == "" {
		writeError(w, http.StatusBadRequest, "text is required")
		return
	}

	summary, err := h.gemini.Summarize(r.Context(), req.Text)
	if err != nil {
		slog.Error("gemini error", "error", err)
		writeError(w, http.StatusInternalServerError, "failed to generate summary")
		return
	}

	writeSuccess(w, http.StatusOK, map[string]string{"summary": summary})
}
