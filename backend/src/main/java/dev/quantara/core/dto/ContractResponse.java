package dev.quantara.core.dto;

import dev.quantara.core.entity.Contract;
import java.time.OffsetDateTime;

public record ContractResponse(
    Long id,
    Long projectId,
    String deploymentId,
    String contractAddress,
    String deploymentHash,
    OffsetDateTime timestamp) {

  public static ContractResponse from(Contract contract) {
    return new ContractResponse(
        contract.getId(),
        contract.getProject().getId(),
        contract.getDeploymentId(),
        contract.getContractAddress(),
        contract.getDeploymentHash(),
        contract.getCreatedAt());
  }
}
