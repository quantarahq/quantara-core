# backend

The Spring Boot REST API for Quantara. See the [root README](../README.md) for the
overall architecture and [../docs/architecture.md](../docs/architecture.md) for how the
modules fit together.

## Running locally

```bash
# via Docker Compose (backend + Postgres)
cd ../infra && docker compose up -d

# or directly against a local Postgres
mvn spring-boot:run
```

Swagger UI: `http://localhost:8080/api/docs/ui`.

## API examples

```bash
# health
curl http://localhost:8080/api/health

# create a project
curl -X POST http://localhost:8080/api/projects \
  -H 'Content-Type: application/json' \
  -d '{"name":"example-contract","description":"Sample Soroban application"}'

# list projects
curl http://localhost:8080/api/projects

# get one project
curl http://localhost:8080/api/projects/1

# simulate a deployment (also registers a contract record)
curl -X POST http://localhost:8080/api/deploy \
  -H 'Content-Type: application/json' \
  -d '{"projectId":1,"contract":"example"}'

# deployment history for a project
curl http://localhost:8080/api/projects/1/deployments

# contract registry for a project
curl http://localhost:8080/api/projects/1/contracts
```

## Testing

```bash
mvn clean verify
```

Runs unit tests (JUnit 5 + Mockito), Testcontainers-backed integration tests against a
real Postgres, Checkstyle, and Spotless formatting checks.
