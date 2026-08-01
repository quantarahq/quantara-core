use crate::{DeploymentRegistry, DeploymentRegistryClient};
use soroban_sdk::{Env, String};

fn setup() -> (Env, DeploymentRegistryClient<'static>) {
    let env = Env::default();
    let contract_id = env.register(DeploymentRegistry, ());
    let client = DeploymentRegistryClient::new(&env, &contract_id);
    (env, client)
}

#[test]
fn store_and_get_deployment_round_trips() {
    let (env, client) = setup();

    let deployment_id = String::from_str(&env, "deploy-001");
    let project_name = String::from_str(&env, "example-contract");
    let hash = String::from_str(&env, "ed44b5088f867bc4897b6cb03f33e842bfb2da3f8af535b1acbb7b7b38c8dc2");

    client.store_deployment(&deployment_id, &project_name, &hash);
    let record = client.get_deployment(&deployment_id).unwrap();

    assert_eq!(record.project_name, project_name);
    assert_eq!(record.hash, hash);
}
