package dev.quantara.core.controller;

import dev.quantara.core.dto.ContractResponse;
import dev.quantara.core.service.ContractService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/contracts")
@Tag(name = "Contracts", description = "Deployed contract registry, scoped to a project")
public class ContractController {

  private final ContractService contractService;

  public ContractController(ContractService contractService) {
    this.contractService = contractService;
  }

  @GetMapping
  public List<ContractResponse> listContracts(@PathVariable Long projectId) {
    return contractService.listByProject(projectId);
  }
}
