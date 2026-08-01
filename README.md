# quantara-core

[![CI](https://github.com/quantarahq/quantara-core/actions/workflows/ci.yml/badge.svg)](https://github.com/quantarahq/quantara-core/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](backend/pom.xml)
[![Spring Boot 3.5](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F.svg)](backend/pom.xml)
[![Rust](https://img.shields.io/badge/Rust-Soroban%20SDK-black.svg)](contracts/deployment-registry)
[![Good First Issues](https://img.shields.io/github/issues/quantarahq/quantara-core/good-first-issue?color=7057ff)](https://github.com/quantarahq/quantara-core/labels/good-first-issue)

**The backend and blockchain integration layer for Quantara** — an open-source
developer infrastructure platform for the Soroban smart contract ecosystem. This
repository is the heart of the project: a REST API for managing projects and
deployments, a contract registry, and the one real on-chain artifact in the MVP, the
`DeploymentRegistry` Soroban contract.

> **This is an MVP foundation, not a production-ready platform.** It exists to
> validate the idea, attract contributors, and give the project a clean, honest base
> to grow from — not to be feature-complete or hardened. Every corner that's been
> deliberately cut is called out explicitly below rather than hidden.

Part of the Quantara project:

| Repo | What it is |
|---|---|
| **quantara-core** (this repo) | Backend API + Soroban contract |
| [quantara-web](https://github.com/quantarahq/quantara-web) | Next.js dashboard that talks to this API |
| [quantara-toolkit](https://github.com/quantarahq/quantara-toolkit) | Placeholder for a future CLI and runtime |

---

## Table of contents

- [Why Quantara](#why-quantara)
- [Architecture](#architecture)
- [Repository layout](#repository-layout)
- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
- [API reference](#api-reference)
- [Data model](#data-model)
- [The Soroban contract](#the-soroban-contract)
- [Testing](#testing)
- [Code quality tooling](#code-quality-tooling)
- [Security posture — what's deliberately missing](#security-posture--whats-deliberately-missing)
- [Contributing](#contributing)
- [Roadmap](#roadmap)
- [FAQ](#faq)
- [License](#license)

---

## Why Quantara

Soroban gives Stellar a real smart contract platform, but the day-to-day developer
experience around it — managing projects, tracking what you've deployed where, and
verifying what's actually on-chain — is still mostly ad-hoc scripts and manual
bookkeeping. Quantara borrows patterns from modern cloud developer tooling (think:
a lightweight Heroku/Vercel-style dashboard, but for Soroban contracts) so that
workflow has a real home.

This repo is deliberately **narrow and complete** rather than broad and half-built:
one backend, one contract, a handful of endpoints that actually work end-to-end,
instead of stubs for a dozen features. See [ROADMAP](#roadmap) for what's
intentionally not here yet.

## Architecture

```
Developer
    |
Quantara Web  (quantara-web — Next.js dashboard)
    |
Quantara Core (this repo — Spring Boot REST API)
    |
Soroban Contract (contracts/deployment-registry — on-chain)
```

`quantara-core` is a single Spring Boot service backed by PostgreSQL. It owns three
concerns:

1. **Project management** — create and list projects.
2. **A simulated deployment workflow** — `POST /api/deploy` records a deployment
   attempt. The MVP does not talk to a real Soroban network here (see
   [FAQ](#faq)); it's a realistic stand-in for what a real deploy pipeline would do.
3. **A contract registry** — every simulated deployment automatically registers a
   contract record (simulated on-chain address + a real SHA-256 hash) so the rest of
   the platform has something concrete to display, list, and build on.

The `contracts/` directory holds the one real on-chain artifact: a minimal Soroban
contract that mirrors the registry concept on-chain — see
[The Soroban contract](#the-soroban-contract).

## Repository layout

```
quantara-core/
├── backend/                       Spring Boot REST API
│   ├── src/main/java/dev/quantara/core/
│   │   ├── controller/             REST endpoints (thin — validation + delegation)
│   │   ├── service/                 business logic
│   │   ├── repository/               Spring Data JPA repositories
│   │   ├── entity/                    JPA entities
│   │   ├── dto/                        request/response records
│   │   ├── config/                      CORS, OpenAPI
│   │   ├── security/                     SecurityConfig (permit-all for MVP)
│   │   └── exception/                     global error handling
│   ├── src/main/resources/db/migration/  Flyway SQL migrations
│   ├── checkstyle.xml
│   └── README.md                   API curl examples, local run instructions
├── contracts/
│   └── deployment-registry/       Soroban smart contract (Rust)
│       ├── src/lib.rs
│       ├── src/test.rs
│       └── README.md
├── infra/
│   ├── docker-compose.yml         backend + Postgres local dev stack
│   ├── docker/Dockerfile
│   └── scripts/                    dev-up.sh / dev-down.sh
├── docs/architecture.md           deeper dive into module layout and request flow
├── .github/                       CI, issue templates, PR template, Dependabot, CODEOWNERS
├── README.md                      you are here
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── SECURITY.md
├── CHANGELOG.md
└── LICENSE
```

## Tech stack

| Layer | Technology | Notes |
|---|---|---|
| Backend framework | Spring Boot 3.5 | Web, Data JPA, Validation, Security starters |
| Backend language | Java 21 | |
| Database | PostgreSQL 16 | via Flyway-managed schema |
| DB migrations | Flyway | `V1`–`V3`, one table per concern, no premature normalization |
| API docs | OpenAPI / Swagger UI | live at `/api/docs/ui` |
| Smart contract | Rust + Soroban SDK 27 | targets `wasm32v1-none` |
| Containerization | Docker / Docker Compose | backend + Postgres for local dev |
| CI/CD | GitHub Actions | two jobs: backend (`mvn verify`) and contract (`cargo test` + wasm build) |
| Code quality | Checkstyle + Spotless (google-java-format), Rustfmt + Clippy | enforced in CI, not just suggested |
| Dependency hygiene | Dependabot | weekly, scoped per ecosystem (Maven, Cargo, Docker, GitHub Actions) |

## Getting started

**Requirements:** Java 21+, Maven 3.9+, Docker, Rust (stable) with the
`wasm32v1-none` target (`rustup target add wasm32v1-none`).

```bash
git clone https://github.com/quantarahq/quantara-core.git
cd quantara-core

# Option A — full local stack via Docker Compose (backend + Postgres)
cd infra && docker compose up -d

# Option B — backend only, against a Postgres you already have running
cd backend && mvn spring-boot:run
```

The API is served at `http://localhost:8080`, with interactive docs at
`http://localhost:8080/api/docs/ui`. See [backend/README.md](backend/README.md) for
copy-pasteable curl examples for every endpoint, and
[docs/architecture.md](docs/architecture.md) for how the `POST /api/deploy` → contract
registry flow works under the hood.

## API reference

All endpoints are under `/api`. Full curl examples are in
[backend/README.md](backend/README.md); this is the quick-reference table.

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/health` | Liveness check — `{"status":"UP"}` |
| `POST` | `/api/projects` | Create a project (`name`, optional `description`) |
| `GET` | `/api/projects` | List all projects |
| `GET` | `/api/projects/{id}` | Get one project |
| `POST` | `/api/deploy` | Simulate a deployment for a project; also registers a contract record |
| `GET` | `/api/projects/{id}/deployments` | Deployment history for a project |
| `GET` | `/api/projects/{id}/contracts` | Registered contracts for a project |

Errors follow a consistent shape (`GlobalExceptionHandler`): 404s for missing
resources, 400s with per-field messages for validation failures, 500s for anything
unexpected — never a raw stack trace.

## Data model

Three tables, deliberately minimal — see the migrations in
[`backend/src/main/resources/db/migration`](backend/src/main/resources/db/migration):

- **`projects`** — `id`, `name`, `description`, `created_at`
- **`deployments`** — `id`, `deployment_id` (unique, e.g. `deploy-001`), `project_id`
  (FK), `contract_name`, `status`, `created_at`
- **`contracts`** — `id`, `project_id` (FK), `deployment_id`, `contract_address`,
  `deployment_hash`, `created_at`

No premature normalization, no speculative columns for features that don't exist yet.

## The Soroban contract

[`contracts/deployment-registry`](contracts/deployment-registry) is a minimal on-chain
mirror of the same idea: `store_deployment(deployment_id, project_name, hash)` /
`get_deployment(deployment_id)` against Soroban persistent storage. No access control,
no token logic, no DAO — see that directory's [README](contracts/deployment-registry/README.md)
for exactly why, and how to build/test it (`cargo test`, `stellar contract build`).

The backend and the contract are **not wired together yet** — the backend simulates
deployment and registration off-chain; the contract demonstrates the on-chain pattern.
Connecting the two for real is explicitly future work (see [Roadmap](#roadmap)).

## Testing

```bash
cd backend && mvn clean verify   # unit + integration tests + Checkstyle + Spotless
cd contracts/deployment-registry && cargo test   # contract unit tests
```

- **Unit tests** (JUnit 5 + Mockito) exercise every `*Service` in isolation.
- **Integration tests** (`*IT` suffix) spin up a real PostgreSQL container via
  Testcontainers and drive the actual REST endpoints end-to-end through
  `TestRestTemplate`, including real Flyway migrations — not mocked repositories.
- **Contract tests** use `soroban-sdk`'s testutils against a real (simulated) ledger
  environment.

## Code quality tooling

- **Checkstyle** (`backend/checkstyle.xml`) — a deliberately small, non-Javadoc-heavy
  ruleset (unused imports, missing braces, empty blocks) so it catches real mistakes
  without slowing down first-time contributors.
- **Spotless** with `google-java-format` — auto-formats Java; `mvn spotless:apply`
  fixes formatting, `mvn spotless:check` (bound to `verify`) fails CI if you forget.
- **Rustfmt + Clippy** for the contract.
- Both are enforced in CI, not advisory.

## Security posture — what's deliberately missing

There is **no authentication or authorization** on this API — `SecurityConfig`
permits every request and disables CSRF (it's a stateless JSON API). This is an
intentional MVP scope decision, not an oversight: building a real identity system
would be a distraction from demonstrating the core deploy-and-verify workflow. See
[SECURITY.md](SECURITY.md) for the full policy and how to report a real vulnerability.

## Contributing

Start with [CONTRIBUTING.md](CONTRIBUTING.md). The short version:

- Pick up a [`good-first-issue`](https://github.com/quantarahq/quantara-core/labels/good-first-issue)
  or [`help-wanted`](https://github.com/quantarahq/quantara-core/labels/help-wanted).
- Small, focused commits; loose [Conventional Commits](https://www.conventionalcommits.org/)
  style (`feat:`, `fix:`, `docs:`, `test:`, `chore:`).
- `mvn clean verify` (backend) or `cargo test` (contract) must pass before you open a
  PR — CI runs the same checks.
- Labels in use: `backend`, `smart-contract`, `infra`, `documentation`, `MVP`,
  `future`, `good-first-issue`, `help-wanted`.

## Roadmap

What's deliberately **not** here yet, and why:

- **Real authentication/authorization** — see [Security posture](#security-posture--whats-deliberately-missing).
- **Backend ↔ contract wiring** — having `POST /api/deploy` actually invoke the
  on-chain `DeploymentRegistry` contract via the Stellar RPC, instead of simulating.
- **A CLI and execution runtime** — tracked in
  [quantara-toolkit](https://github.com/quantarahq/quantara-toolkit), not this repo.
- **Multi-chain support, hosted/managed Quantara, general-purpose CI/CD** — explicitly
  out of scope; different products, not natural extensions of this one.

## FAQ

**Why does `/api/deploy` not actually deploy anything to Soroban?**
Because the MVP's goal is to demonstrate the *shape* of the workflow — create,
deploy, verify — end to end, without the added complexity (funded testnet accounts,
network flakiness, transaction fees) of a real deployment pipeline. The simulated
response and the real Soroban contract are both real code; they're just not
connected to each other yet. See [Roadmap](#roadmap).

**Why Java/Spring Boot instead of something more "crypto-native"?**
Because it's a REST API and a relational data model — a well-understood, boring,
maintainable stack was the right call for something meant to attract contributors,
not a place to be clever.

**Is this safe to run in production?**
No — see [Security posture](#security-posture--whats-deliberately-missing). This is
explicitly an MVP foundation.

## License

[MIT](LICENSE)
