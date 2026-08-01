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

#[test]
fn get_deployment_returns_none_when_missing() {
    let (env, client) = setup();

    let deployment_id = String::from_str(&env, "deploy-does-not-exist");

    assert_eq!(client.get_deployment(&deployment_id), None);
}

#[test]
fn store_deployment_overwrites_existing_record() {
    let (env, client) = setup();

    let deployment_id = String::from_str(&env, "deploy-002");
    let first_project = String::from_str(&env, "first-project");
    let first_hash = String::from_str(&env, "hash-one");
    let second_project = String::from_str(&env, "second-project");
    let second_hash = String::from_str(&env, "hash-two");

    client.store_deployment(&deployment_id, &first_project, &first_hash);
    client.store_deployment(&deployment_id, &second_project, &second_hash);

    let record = client.get_deployment(&deployment_id).unwrap();

    assert_eq!(record.project_name, second_project);
    assert_eq!(record.hash, second_hash);
}

#[test]
fn distinct_deployment_ids_are_stored_independently() {
    let (env, client) = setup();

    let first_id = String::from_str(&env, "deploy-001");
    let second_id = String::from_str(&env, "deploy-002");
    let project_name = String::from_str(&env, "example-contract");
    let first_hash = String::from_str(&env, "hash-one");
    let second_hash = String::from_str(&env, "hash-two");

    client.store_deployment(&first_id, &project_name, &first_hash);
    client.store_deployment(&second_id, &project_name, &second_hash);

    assert_eq!(client.get_deployment(&first_id).unwrap().hash, first_hash);
    assert_eq!(client.get_deployment(&second_id).unwrap().hash, second_hash);
}
