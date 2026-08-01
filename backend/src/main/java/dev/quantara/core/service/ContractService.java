package dev.quantara.core.service;

import dev.quantara.core.dto.ContractResponse;
import dev.quantara.core.entity.Contract;
import dev.quantara.core.entity.Deployment;
import dev.quantara.core.repository.ContractRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Records on-chain deployment metadata for a project. The MVP simulates the contract address and
 * deployment hash rather than talking to a live Soroban network — see contracts/deployment-registry
 * for the real on-chain counterpart.
 */
@Service
public class ContractService {

  private final ContractRepository contractRepository;

  public ContractService(ContractRepository contractRepository) {
    this.contractRepository = contractRepository;
  }

  @Transactional
  public ContractResponse registerContract(Deployment deployment) {
    String contractAddress = simulateContractAddress();
    String deploymentHash = simulateDeploymentHash(deployment);

    Contract contract =
        new Contract(
            deployment.getProject(), deployment.getDeploymentId(), contractAddress, deploymentHash);

    return ContractResponse.from(contractRepository.save(contract));
  }

  @Transactional(readOnly = true)
  public List<ContractResponse> listByProject(Long projectId) {
    return contractRepository.findByProjectId(projectId).stream()
        .map(ContractResponse::from)
        .toList();
  }

  private String simulateContractAddress() {
    String raw = UUID.randomUUID().toString().replace("-", "").toUpperCase();
    return "C" + raw.repeat(2).substring(0, 55);
  }

  private String simulateDeploymentHash(Deployment deployment) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      String input =
          deployment.getDeploymentId() + deployment.getContractName() + deployment.getCreatedAt();
      byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return toHex(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 is not available", e);
    }
  }

  private String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
