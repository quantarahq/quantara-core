#!/usr/bin/env bash
# Stops the local dev stack started by dev-up.sh.
set -euo pipefail

cd "$(dirname "$0")/.."
docker compose down
