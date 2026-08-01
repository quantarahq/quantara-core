CREATE TABLE deployments (
    id            BIGSERIAL PRIMARY KEY,
    deployment_id VARCHAR(64)  NOT NULL UNIQUE,
    project_id    BIGINT       NOT NULL REFERENCES projects (id),
    contract_name VARCHAR(255) NOT NULL,
    status        VARCHAR(32)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_deployments_project_id ON deployments (project_id);
