#![no_std]

use soroban_sdk::{contract, contractimpl, Env};

/// Records deployment verification metadata for Quantara projects on-chain.
///
/// This is intentionally minimal: no access control, no token logic, no DAO
/// governance. It exists to demonstrate a real Soroban integration for the
/// Quantara MVP, not to be a production registry.
#[contract]
pub struct DeploymentRegistry;

#[contractimpl]
impl DeploymentRegistry {}
