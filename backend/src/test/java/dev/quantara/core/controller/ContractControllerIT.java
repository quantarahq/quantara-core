package dev.quantara.core.controller;

import static org.assertj.core.api.Assertions.assertThat;

import dev.quantara.core.AbstractIntegrationTest;
import dev.quantara.core.dto.ContractResponse;
import dev.quantara.core.dto.CreateProjectRequest;
import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ContractControllerIT extends AbstractIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void deployingRegistersAContractVisibleOnTheProject() {
    ProjectResponse project =
        restTemplate
            .postForEntity(
                "/api/projects",
                new CreateProjectRequest("contract-project", null),
                ProjectResponse.class)
            .getBody();

    restTemplate.postForEntity(
        "/api/deploy", new DeployRequest(project.id(), "example"), Void.class);

    ResponseEntity<ContractResponse[]> contracts =
        restTemplate.getForEntity(
            "/api/projects/" + project.id() + "/contracts", ContractResponse[].class);

    assertThat(contracts.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(contracts.getBody()).hasSize(1);
    assertThat(contracts.getBody()[0].contractAddress()).startsWith("C");
    assertThat(contracts.getBody()[0].deploymentHash()).isNotBlank();
  }
}
