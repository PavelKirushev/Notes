package handler

import (
	"encoding/json"
	"log/slog"
	"net/http"
)

// Response — стандартная обёртка для всех ответов API.
type Response struct {
	Success bool        `json:"success"`
	Data    interface{} `json:"data,omitempty"`
	Error   string      `json:"error,omitempty"`
}

// writeJSON пишет JSON-ответ с нужным HTTP-кодом.
func writeJSON(w http.ResponseWriter, status int, body interface{}) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(body); err != nil {
		slog.Error("failed to encode response", "error", err)
	}
}

// writeSuccess оборачивает данные в успешный ответ.
func writeSuccess(w http.ResponseWriter, status int, data interface{}) {
	writeJSON(w, status, Response{Success: true, Data: data})
}

// writeError оборачивает сообщение об ошибке в стандартный ответ.
func writeError(w http.ResponseWriter, status int, message string) {
	writeJSON(w, status, Response{Success: false, Error: message})
}
