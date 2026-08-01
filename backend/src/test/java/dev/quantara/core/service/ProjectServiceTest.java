package dev.quantara.core.service;

import dev.quantara.core.dto.CreateProjectRequest;
import dev.quantara.core.dto.ProjectResponse;
import dev.quantara.core.entity.Project;
import dev.quantara.core.exception.ResourceNotFoundException;
import dev.quantara.core.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProjectSavesAndReturnsResponse() {
        var request = new CreateProjectRequest("example-contract", "Sample Soroban application");
        var saved = new Project("example-contract", "Sample Soroban application");
        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        ProjectResponse response = projectService.createProject(request);

        assertThat(response.name()).isEqualTo("example-contract");
        assertThat(response.description()).isEqualTo("Sample Soroban application");
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void listProjectsReturnsAllMapped() {
        var project = new Project("example-contract", "Sample Soroban application");
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectResponse> responses = projectService.listProjects();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("example-contract");
    }

    @Test
    void getProjectThrowsWhenNotFound() {
        when(projectRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProject(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("42");
    }
}
