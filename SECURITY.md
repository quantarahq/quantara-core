# Security Policy

Quantara is an early-stage MVP, not a production platform (see the README) — please
keep that in mind when assessing severity.

## Reporting a vulnerability

If you find a security issue, please **do not open a public issue**. Instead, report
it privately via GitHub's [private vulnerability reporting](https://github.com/quantarahq/quantara-core/security/advisories/new)
for this repo, or contact the org owners at github.com/quantarahq.

Please include:

- A description of the issue and its impact
- Steps to reproduce
- Affected version/commit

We'll acknowledge reports as quickly as we can and follow up once a fix is available.

## Scope

Known limitations that are **not** considered vulnerabilities, because they're
intentional MVP scope decisions documented in the README and architecture docs:

- No authentication/authorization on the API (`SecurityConfig` permits all requests)
- The Soroban contract has no access control on `store_deployment`

Both are called out as future work, not oversights.
