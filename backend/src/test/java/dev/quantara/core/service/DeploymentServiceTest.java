package dev.quantara.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.DeployResponse;
import dev.quantara.core.entity.Deployment;
import dev.quantara.core.entity.DeploymentStatus;
import dev.quantara.core.entity.Project;
import dev.quantara.core.exception.ResourceNotFoundException;
import dev.quantara.core.repository.DeploymentRepository;
import dev.quantara.core.repository.ProjectRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeploymentServiceTest {

  @Mock private DeploymentRepository deploymentRepository;

  @Mock private ProjectRepository projectRepository;

  @Mock private ContractService contractService;

  @InjectMocks private DeploymentService deploymentService;

  @Test
  void deploySavesDeploymentAndReturnsSuccess() {
    var project = new Project("example-contract", "Sample Soroban application");
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(deploymentRepository.count()).thenReturn(0L);

    DeployResponse response = deploymentService.deploy(new DeployRequest(1L, "example"));

    assertThat(response.status()).isEqualTo("SUCCESS");
    assertThat(response.deploymentId()).isEqualTo("deploy-001");

    ArgumentCaptor<Deployment> captor = ArgumentCaptor.forClass(Deployment.class);
    verify(deploymentRepository).save(captor.capture());
    assertThat(captor.getValue().getStatus()).isEqualTo(DeploymentStatus.SUCCESS);
    assertThat(captor.getValue().getContractName()).isEqualTo("example");
  }

  @Test
  void deployThrowsWhenProjectMissing() {
    when(projectRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> deploymentService.deploy(new DeployRequest(99L, "example")))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void deploymentIdIncrementsWithExistingCount() {
    var project = new Project("example-contract", null);
    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(deploymentRepository.count()).thenReturn(4L);

    DeployResponse response = deploymentService.deploy(new DeployRequest(1L, "example"));

    assertThat(response.deploymentId()).isEqualTo("deploy-005");
  }
}
