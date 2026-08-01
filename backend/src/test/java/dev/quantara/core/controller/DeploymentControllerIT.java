package dev.quantara.core.controller;

import static org.assertj.core.api.Assertions.assertThat;

import dev.quantara.core.AbstractIntegrationTest;
import dev.quantara.core.dto.CreateProjectRequest;
import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.DeploymentResponse;
import dev.quantara.core.dto.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class DeploymentControllerIT extends AbstractIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void deployingAddsToTheProjectsDeploymentHistory() {
    ProjectResponse project =
        restTemplate
            .postForEntity(
                "/api/projects",
                new CreateProjectRequest("deployment-history-project", null),
                ProjectResponse.class)
            .getBody();

    restTemplate.postForEntity(
        "/api/deploy", new DeployRequest(project.id(), "example"), Void.class);

    ResponseEntity<DeploymentResponse[]> deployments =
        restTemplate.getForEntity(
            "/api/projects/" + project.id() + "/deployments", DeploymentResponse[].class);

    assertThat(deployments.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(deployments.getBody()).hasSize(1);
    assertThat(deployments.getBody()[0].status()).isEqualTo("SUCCESS");
    assertThat(deployments.getBody()[0].deploymentId()).startsWith("deploy-");
  }

  @Test
  void listDeploymentsForProjectWithNoneReturnsEmptyList() {
    ProjectResponse project =
        restTemplate
            .postForEntity(
                "/api/projects", new CreateProjectRequest("no-deployments-project", null),
                ProjectResponse.class)
            .getBody();

    ResponseEntity<DeploymentResponse[]> deployments =
        restTemplate.getForEntity(
            "/api/projects/" + project.id() + "/deployments", DeploymentResponse[].class);

    assertThat(deployments.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(deployments.getBody()).isEmpty();
  }
}
