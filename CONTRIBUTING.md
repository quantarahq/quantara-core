# Contributing to quantara-core

Thanks for your interest in contributing to Quantara! This repo is an early-stage MVP,
so the priority is a clean, understandable foundation over feature count — please keep
that in mind when opening PRs.

## Ways to contribute

- Pick up an issue labeled [`good-first-issue`](https://github.com/quantarahq/quantara-core/labels/good-first-issue)
  or [`help-wanted`](https://github.com/quantarahq/quantara-core/labels/help-wanted).
- Report bugs or propose features via GitHub Issues.
- Improve documentation — architecture docs, API docs, and code comments are all fair game.

## Development setup

Requirements:

- Java 21+
- Maven 3.9+
- Docker (for Postgres via `docker compose` and for Testcontainers-based integration tests)
- Rust (stable) with the `wasm32-unknown-unknown` target, for the Soroban contract

```bash
git clone https://github.com/quantarahq/quantara-core.git
cd quantara-core

# backend
cd backend
mvn clean verify

# contract
cd ../contracts/deployment-registry
cargo test
```

## Project structure

See [README.md](README.md#repository-layout) for the top-level layout, and
[docs/architecture.md](docs/architecture.md) for how the backend modules fit together.

## Commit and PR conventions

- Use small, focused commits with descriptive messages (we follow a loose
  [Conventional Commits](https://www.conventionalcommits.org/) style: `feat:`, `fix:`,
  `docs:`, `test:`, `chore:`, `infra:`).
- Keep PRs scoped to one concern — it's easier to review and easier to revert.
- Add or update tests for any behavior change.
- Make sure `mvn clean verify` (backend) or `cargo test` (contract) passes before
  opening a PR — CI will run the same checks.

## Code style

- Backend: standard Java conventions, enforced by Checkstyle/Spotless (see `backend/pom.xml`).
- Contract: `cargo fmt` and `cargo clippy` should be clean.

## Reporting issues

Please include steps to reproduce, expected vs. actual behavior, and relevant logs.
Use the issue templates when available.

## Code of Conduct

This project follows the [Contributor Covenant](CODE_OF_CONDUCT.md). Be kind.
