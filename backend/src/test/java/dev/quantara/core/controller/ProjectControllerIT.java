package dev.quantara.core.controller;

import dev.quantara.core.AbstractIntegrationTest;
import dev.quantara.core.dto.CreateProjectRequest;
import dev.quantara.core.dto.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndFetchProjectRoundTrip() {
        var createRequest = new CreateProjectRequest("example-contract", "Sample Soroban application");

        ResponseEntity<ProjectResponse> createResponse =
                restTemplate.postForEntity("/api/projects", createRequest, ProjectResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long id = createResponse.getBody().id();

        ResponseEntity<ProjectResponse> getResponse =
                restTemplate.getForEntity("/api/projects/" + id, ProjectResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().name()).isEqualTo("example-contract");
    }

    @Test
    void listProjectsIncludesCreatedProject() {
        restTemplate.postForEntity("/api/projects",
                new CreateProjectRequest("listed-project", null), ProjectResponse.class);

        ResponseEntity<ProjectResponse[]> listResponse =
                restTemplate.getForEntity("/api/projects", ProjectResponse[].class);

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).isNotEmpty();
    }
}
