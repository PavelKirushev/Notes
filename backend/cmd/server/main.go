package main

import (
	"context"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"

	"github.com/onweg/backend/internal/config"
	"github.com/onweg/backend/internal/handler"
	authmw "github.com/onweg/backend/internal/middleware"
	"github.com/onweg/backend/internal/repository/postgres"
	"github.com/onweg/backend/internal/service"
	"github.com/onweg/backend/pkg/database"
	"github.com/onweg/backend/pkg/hash"
	"github.com/onweg/backend/pkg/jwt"
)

func main() {
	slog.SetDefault(slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		Level: slog.LevelInfo,
	})))

	// 1. Конфигурация
	cfg := config.Load()

	// 2. База данных
	db, err := database.NewPostgres(database.Options{
		Host:     cfg.Database.Host,
		Port:     cfg.Database.Port,
		User:     cfg.Database.User,
		Password: cfg.Database.Password,
		Name:     cfg.Database.Name,
		SSLMode:  cfg.Database.SSLMode,
		MaxConns: cfg.Database.MaxConns,
	})
	if err != nil {
		slog.Error("failed to connect to database", "error", err)
		os.Exit(1)
	}
	defer db.Close()

	// 3. Dependency Injection
	hasher := hash.NewBcrypt(cfg.Auth.BcryptCost)
	jwtManager := jwt.NewManager(cfg.Auth.JWTSecret, cfg.Auth.TokenTTL)

	userRepo := postgres.NewUserRepository(db)
	subRepo := postgres.NewSubscriptionRepository(db)

	authSvc := service.NewAuthService(userRepo, hasher, jwtManager)
	userSvc := service.NewUserService(userRepo)
	subSvc := service.NewSubscriptionService(subRepo)

	authHandler := handler.NewAuthHandler(authSvc)
	userHandler := handler.NewUserHandler(userSvc)
	subHandler := handler.NewSubscriptionHandler(subSvc)

	jwtMiddleware := authmw.NewJWTMiddleware(jwtManager)
	superUserMiddleware := authmw.NewSuperUserMiddleware(userRepo)

	// 4. Роутер
	r := chi.NewRouter()
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintln(w, `{"status":"ok"}`)
	})

	r.Route("/api/v1", func(r chi.Router) {

		// ── Аутентификация ────────────────────────────────────────────────────
		r.Route("/auth", func(r chi.Router) {
			r.Post("/register", authHandler.Register)
			r.Post("/login", authHandler.Login)

			r.Group(func(r chi.Router) {
				r.Use(jwtMiddleware.Authenticate)
				r.Get("/me", authHandler.Me)
			})
		})

		// ── Подписка (текущий пользователь) ──────────────────────────────────
		r.Route("/subscriptions", func(r chi.Router) {
			r.Use(jwtMiddleware.Authenticate)
			r.Get("/me", subHandler.GetMy) // проверить свою подписку
		})

		// ── Админ (только суперпользователь) ─────────────────────────────────
		r.Route("/admin", func(r chi.Router) {
			r.Use(jwtMiddleware.Authenticate)
			r.Use(superUserMiddleware.RequireSuperUser)

			r.Get("/users", userHandler.List)
			r.Put("/users/{userID}/subscription", subHandler.Set)
			r.Delete("/users/{userID}/subscription", subHandler.Cancel)
		})
	})

	// 5. HTTP-сервер
	srv := &http.Server{
		Addr:         ":" + cfg.Server.Port,
		Handler:      r,
		ReadTimeout:  cfg.Server.ReadTimeout,
		WriteTimeout: cfg.Server.WriteTimeout,
		IdleTimeout:  60 * time.Second,
	}

	go func() {
		slog.Info("server started", "port", cfg.Server.Port)
		if err := srv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			slog.Error("server error", "error", err)
			os.Exit(1)
		}
	}()

	// 6. Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	slog.Info("shutting down server...")
	ctx, cancel := context.WithTimeout(context.Background(), cfg.Server.ShutdownTimeout)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		slog.Error("forced shutdown", "error", err)
		os.Exit(1)
	}
	slog.Info("server stopped gracefully")
}
