package dev.quantara.core.controller;

import dev.quantara.core.dto.DeploymentResponse;
import dev.quantara.core.service.DeploymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/deployments")
@Tag(name = "Deployments", description = "Deployment history, scoped to a project")
public class DeploymentController {

  private final DeploymentService deploymentService;

  public DeploymentController(DeploymentService deploymentService) {
    this.deploymentService = deploymentService;
  }

  @GetMapping
  public List<DeploymentResponse> listDeployments(@PathVariable Long projectId) {
    return deploymentService.listByProject(projectId);
  }
}
