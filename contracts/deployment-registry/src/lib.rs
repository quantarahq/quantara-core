#![no_std]

use soroban_sdk::{contract, contractimpl, contracttype, Env, String};

/// On-chain record of a single deployment: the project it belongs to and a
/// hash identifying what was deployed.
#[contracttype]
#[derive(Clone, Debug, Eq, PartialEq)]
pub struct DeploymentRecord {
    pub project_name: String,
    pub hash: String,
}

/// Records deployment verification metadata for Quantara projects on-chain.
///
/// This is intentionally minimal: no access control, no token logic, no DAO
/// governance. It exists to demonstrate a real Soroban integration for the
/// Quantara MVP, not to be a production registry.
#[contract]
pub struct DeploymentRegistry;

#[contractimpl]
impl DeploymentRegistry {
    /// Stores a deployment verification record, keyed by `deployment_id`.
    /// Overwrites any existing record for the same `deployment_id`.
    pub fn store_deployment(env: Env, deployment_id: String, project_name: String, hash: String) {
        let record = DeploymentRecord { project_name, hash };
        env.storage().persistent().set(&deployment_id, &record);
    }
}
