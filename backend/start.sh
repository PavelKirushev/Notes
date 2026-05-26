#!/bin/bash
cd "$(dirname "$0")"

POSTGRES_RUNNING=$(docker ps -q --filter "name=app_postgres" --filter "status=running")
BACKEND_RUNNING=$(docker ps -q --filter "name=app_backend" --filter "status=running")

if [ -n "$POSTGRES_RUNNING" ] && [ -n "$BACKEND_RUNNING" ]; then
  echo "already running"
  echo "backend  → http://localhost:8080"
  echo "postgres → localhost:5432"
  exit 0
fi

BACKEND_IMAGE=$(docker images -q backend-backend 2>/dev/null)

if [ -z "$BACKEND_IMAGE" ]; then
  echo "building image..."
  DOCKER_BUILDKIT=0 docker-compose build -q
fi

echo "starting..."
docker-compose up -d

echo "backend  → http://localhost:8080"
echo "postgres → localhost:5432"
