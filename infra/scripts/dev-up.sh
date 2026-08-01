#!/usr/bin/env bash
# Starts the full local dev stack (Postgres + backend) via Docker Compose.
set -euo pipefail

cd "$(dirname "$0")/.."
docker compose up --build -d
echo "quantara-core is starting at http://localhost:8080 (health: /api/health)"
