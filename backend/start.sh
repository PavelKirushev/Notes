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

echo "building backend..."
DOCKER_BUILDKIT=0 docker-compose build -q backend

echo "starting..."
docker-compose up -d

echo "backend  → http://localhost:8080"
echo "postgres → localhost:5432"
