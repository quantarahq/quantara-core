package dev.quantara.core.controller;

import dev.quantara.core.dto.DeployRequest;
import dev.quantara.core.dto.DeployResponse;
import dev.quantara.core.service.DeploymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deploy")
@Tag(name = "Deployments", description = "Simulated Soroban contract deployment workflow")
public class DeployController {

    private final DeploymentService deploymentService;

    public DeployController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    public DeployResponse deploy(@Valid @RequestBody DeployRequest request) {
        return deploymentService.deploy(request);
    }
}
