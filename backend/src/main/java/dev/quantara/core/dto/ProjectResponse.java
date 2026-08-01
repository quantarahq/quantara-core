package dev.quantara.core.dto;

import dev.quantara.core.entity.Project;
import java.time.OffsetDateTime;

public record ProjectResponse(Long id, String name, String description, OffsetDateTime createdAt) {

  public static ProjectResponse from(Project project) {
    return new ProjectResponse(
        project.getId(), project.getName(), project.getDescription(), project.getCreatedAt());
  }
}
