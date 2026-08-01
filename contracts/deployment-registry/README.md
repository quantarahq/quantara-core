# DeploymentRegistry

A minimal Soroban smart contract that records deployment verification metadata
on-chain — the one real Stellar/Soroban integration point in the Quantara MVP.

## What it does

The contract stores a `DeploymentRecord` (project name + hash) keyed by a
`deployment_id` string, and lets anyone read it back. That's it — this mirrors,
on-chain, what `quantara-core`'s backend already does off-chain when it registers
a contract after a simulated deploy (see `dev.quantara.core.service.ContractService`).

```rust
pub struct DeploymentRecord {
    pub project_name: String,
    pub hash: String,
}

fn store_deployment(env: Env, deployment_id: String, project_name: String, hash: String);
fn get_deployment(env: Env, deployment_id: String) -> Option<DeploymentRecord>;
```

## What it deliberately does not do

Per the Quantara MVP spec, this contract must **not** include:

- complex permissions / access control
- token logic
- DAO functionality
- advanced authorization systems

Anyone can call `store_deployment` and overwrite any record. That's fine for an MVP
whose goal is to demonstrate the integration pattern, not to be production-grade —
access control is natural `future`-labeled contribution work.

## Building and testing

Requirements: Rust (stable) with the `wasm32-unknown-unknown` target, and the
[`stellar` CLI](https://developers.stellar.org/docs/tools/stellar-cli) (formerly `soroban-cli`).

```bash
# unit tests (native, fast)
cargo test

# build the deployable wasm
stellar contract build
# or: cargo build --target wasm32-unknown-unknown --release
```

The build produces `target/wasm32-unknown-unknown/release/deployment_registry.wasm`.

## Deploying (manual, for now)

The MVP backend simulates deployment rather than calling this contract automatically
— wiring the two together (backend actually invoking `store_deployment` via the
Stellar RPC/SDK after a deploy) is intentionally left as future work. To try it
manually against a local/testnet network:

```bash
stellar contract deploy \
  --wasm target/wasm32-unknown-unknown/release/deployment_registry.wasm \
  --source <your-identity> \
  --network testnet
```
