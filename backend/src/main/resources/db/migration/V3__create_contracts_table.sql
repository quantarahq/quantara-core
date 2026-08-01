CREATE TABLE contracts (
    id                BIGSERIAL PRIMARY KEY,
    project_id        BIGINT       NOT NULL REFERENCES projects (id),
    deployment_id     VARCHAR(64)  NOT NULL REFERENCES deployments (deployment_id),
    contract_address  VARCHAR(255) NOT NULL,
    deployment_hash   VARCHAR(255) NOT NULL,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_contracts_project_id ON contracts (project_id);
