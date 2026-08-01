package dev.quantara.core.service;

import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.DeployResponse;
import dev.quantara.core.dto.DeploymentResponse;
import dev.quantara.core.entity.Deployment;
import dev.quantara.core.entity.DeploymentStatus;
import dev.quantara.core.entity.Project;
import dev.quantara.core.exception.ResourceNotFoundException;
import dev.quantara.core.repository.DeploymentRepository;
import dev.quantara.core.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simulates a Soroban contract deployment workflow. The MVP does not talk to real deployment
 * infrastructure — it records a deployment attempt so the rest of the platform (dashboard, contract
 * registry) has something real to display and build on.
 */
@Service
public class DeploymentService {

  private final DeploymentRepository deploymentRepository;
  private final ProjectRepository projectRepository;
  private final ContractService contractService;

  public DeploymentService(
      DeploymentRepository deploymentRepository,
      ProjectRepository projectRepository,
      ContractService contractService) {
    this.deploymentRepository = deploymentRepository;
    this.projectRepository = projectRepository;
    this.contractService = contractService;
  }

  @Transactional
  public DeployResponse deploy(DeployRequest request) {
    Project project =
        projectRepository
            .findById(request.projectId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Project not found: " + request.projectId()));

    String deploymentId = nextDeploymentId();
    Deployment deployment =
        new Deployment(deploymentId, project, request.contract(), DeploymentStatus.SUCCESS);
    deploymentRepository.save(deployment);

    contractService.registerContract(deployment);

    return new DeployResponse(deployment.getStatus().name(), deployment.getDeploymentId());
  }

  @Transactional(readOnly = true)
  public List<DeploymentResponse> listByProject(Long projectId) {
    return deploymentRepository.findByProjectId(projectId).stream()
        .map(DeploymentResponse::from)
        .toList();
  }

  private String nextDeploymentId() {
    long sequence = deploymentRepository.count() + 1;
    return "deploy-%03d".formatted(sequence);
  }
}
