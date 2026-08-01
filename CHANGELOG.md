# Changelog

All notable changes to `quantara-core` are documented here. Format loosely follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

- Project management API: `POST/GET /api/projects`, `GET /api/projects/{id}`
- Simulated deployment workflow: `POST /api/deploy`
- Contract registry: `GET /api/projects/{id}/contracts`, populated automatically on deploy
- Deployment history: `GET /api/projects/{id}/deployments`
- `GET /api/health`
- `DeploymentRegistry` Soroban contract (`contracts/deployment-registry`)
- Docker Compose local dev stack, GitHub Actions CI (backend + contract)
