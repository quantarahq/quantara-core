package dev.quantara.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeployRequest(
    @NotNull(message = "projectId is required") Long projectId,
    @NotBlank(message = "contract is required") String contract) {}
