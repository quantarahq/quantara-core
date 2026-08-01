# Architecture

## Overview

```
Developer
    |
Quantara Web  (quantara-web, Next.js dashboard)
    |
Quantara Core (this repo, Spring Boot REST API)
    |
Soroban Contract (contracts/deployment-registry, on-chain)
```

`quantara-core` is a single Spring Boot service backed by PostgreSQL. It owns three
concerns: project management, a simulated deployment workflow, and a contract registry
that records deployment metadata. The `contracts/` directory holds the one real on-chain
artifact in the MVP — a minimal Soroban contract mirroring the registry concept on-chain.

## Backend module layout

```
backend/src/main/java/dev/quantara/core/
├── controller/   REST endpoints (thin — validation + delegation only)
├── service/      business logic
├── repository/   Spring Data JPA repositories
├── entity/       JPA entities
├── dto/          request/response records, decoupled from entities
├── config/       CORS, OpenAPI
├── security/     SecurityConfig (permit-all for MVP, seam for future authN/authZ)
└── exception/    domain exceptions + @RestControllerAdvice global handler
```

## Request flow: `POST /api/deploy`

1. `DeployController` validates the request body (`projectId`, `contract`).
2. `DeploymentService` looks up the `Project`, generates a `deploymentId`
   (`deploy-NNN`), and persists a `Deployment` row with status `SUCCESS` — this is a
   **simulated** deployment; there is no real Soroban network call here.
3. `DeploymentService` calls `ContractService.registerContract(...)`, which persists a
   `Contract` row with a simulated on-chain address and a SHA-256 deployment hash.
4. The response is `{ "status": "SUCCESS", "deploymentId": "deploy-001" }`.

This mirrors, in miniature, what a real deployment pipeline would do: run the deploy,
then record verifiable metadata about what was deployed — which is exactly what the
`DeploymentRegistry` Soroban contract does on-chain.

## Data model

Three tables, deliberately minimal (see `backend/src/main/resources/db/migration`):

- `projects` — `id`, `name`, `description`, `created_at`
- `deployments` — `id`, `deployment_id` (unique, e.g. `deploy-001`), `project_id` (FK),
  `contract_name`, `status`, `created_at`
- `contracts` — `id`, `project_id` (FK), `deployment_id`, `contract_address`,
  `deployment_hash`, `created_at`

## Why permit-all security for the MVP

Spring Security is on the classpath (per the spec's stack requirements) so the seam for
real authentication/authorization exists, but the MVP has no user accounts — the goal is
to demonstrate the developer workflow, not build an identity system. `SecurityConfig`
permits all requests and disables CSRF (this is a stateless JSON API, not a browser
session). Adding real auth is a natural `future`-labeled contribution.

## Testing strategy

- **Unit tests** (JUnit 5 + Mockito) exercise each `*Service` in isolation.
- **Integration tests** (`*IT` suffix) use Testcontainers to spin up a real
  PostgreSQL instance and drive the actual REST endpoints end-to-end via
  `TestRestTemplate`, including Flyway migrations running for real.

Run both with `mvn clean verify` from `backend/`.
