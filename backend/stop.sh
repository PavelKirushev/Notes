#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo ""
echo "================================================"
echo "  Остановка контейнеров: postgres + backend"
echo "================================================"
echo ""

docker-compose down

echo ""
echo "  Контейнеры остановлены."
echo ""
