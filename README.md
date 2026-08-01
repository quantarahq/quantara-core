# quantara-core

The backend and blockchain integration layer for **Quantara** — an open-source developer
infrastructure platform for the Soroban smart contract ecosystem.

This repository is the heart of Quantara: REST APIs, project management, deployment
workflows, contract registry, and the on-chain `DeploymentRegistry` Soroban contract.

> This is an MVP foundation, not a production-ready platform. It exists to validate the
> idea, attract contributors, and provide a clean base for future development. See
> [quantarahq/quantara-web](https://github.com/quantarahq/quantara-web) for the dashboard
> and [quantarahq/quantara-toolkit](https://github.com/quantarahq/quantara-toolkit) for
> future CLI/runtime tooling.

## Architecture

```
Developer
    |
Quantara Web  (quantara-web)
    |
Quantara Core (this repo)
    |
Soroban Contract (contracts/deployment-registry)
```

`quantara-core` exposes a REST API for managing projects, simulating deployments, and
recording contract metadata, and it owns the one on-chain artifact in the MVP: a minimal
Soroban contract that stores deployment verification records.

## Repository layout

```
quantara-core/
├── backend/              Spring Boot REST API
├── contracts/
│   └── deployment-registry/   Soroban smart contract (Rust)
├── infra/                Docker Compose, Dockerfiles, scripts
├── docs/                 Architecture and contributor docs
├── README.md
└── CONTRIBUTING.md
```

## Tech stack

| Layer | Technology |
|---|---|
| Backend framework | Spring Boot |
| Backend language | Java 21 |
| Database | PostgreSQL |
| DB migrations | Flyway |
| API docs | OpenAPI / Swagger |
| Smart contract | Rust + Soroban SDK |
| Containerization | Docker / Docker Compose |
| CI/CD | GitHub Actions |

## Getting started

Requirements: Java 21+, Maven 3.9+, Docker, Rust + `wasm32-unknown-unknown` target.

```bash
# start Postgres + backend
cd infra
docker compose up -d

# or run the backend directly against a local Postgres
cd backend
mvn spring-boot:run
```

The API is served at `http://localhost:8080`. See [docs/architecture.md](docs/architecture.md)
for a deeper look at the modules, and `backend/README.md` (coming in this phase) for API
details once the endpoints land.

## Status

This repo is under active MVP development. See open issues labeled `MVP` for what's
in scope right now, and `future` for what's intentionally deferred.

## License

[MIT](LICENSE)
