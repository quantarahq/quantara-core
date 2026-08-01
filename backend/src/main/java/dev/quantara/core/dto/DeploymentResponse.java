package dev.quantara.core.dto;

import dev.quantara.core.entity.Deployment;
import java.time.OffsetDateTime;

public record DeploymentResponse(
    Long id,
    String deploymentId,
    Long projectId,
    String contractName,
    String status,
    OffsetDateTime createdAt) {

  public static DeploymentResponse from(Deployment deployment) {
    return new DeploymentResponse(
        deployment.getId(),
        deployment.getDeploymentId(),
        deployment.getProject().getId(),
        deployment.getContractName(),
        deployment.getStatus().name(),
        deployment.getCreatedAt());
  }
}
