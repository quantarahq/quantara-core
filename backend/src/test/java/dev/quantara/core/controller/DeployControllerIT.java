package dev.quantara.core.controller;

import static org.assertj.core.api.Assertions.assertThat;

import dev.quantara.core.AbstractIntegrationTest;
import dev.quantara.core.dto.CreateProjectRequest;
import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.DeployResponse;
import dev.quantara.core.dto.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class DeployControllerIT extends AbstractIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void deployAgainstExistingProjectSucceeds() {
    ProjectResponse project =
        restTemplate
            .postForEntity(
                "/api/projects",
                new CreateProjectRequest("deployable-project", "used for deploy IT"),
                ProjectResponse.class)
            .getBody();

    ResponseEntity<DeployResponse> deployResponse =
        restTemplate.postForEntity(
            "/api/deploy", new DeployRequest(project.id(), "example"), DeployResponse.class);

    assertThat(deployResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(deployResponse.getBody().status()).isEqualTo("SUCCESS");
    assertThat(deployResponse.getBody().deploymentId()).startsWith("deploy-");
  }

  @Test
  void deployAgainstMissingProjectReturnsNotFound() {
    ResponseEntity<String> response =
        restTemplate.postForEntity(
            "/api/deploy", new DeployRequest(999_999L, "example"), String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
