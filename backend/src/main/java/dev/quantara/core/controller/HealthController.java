package dev.quantara.core.controller;

import dev.quantara.core.dto.HealthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Service health check")
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse health() {
        return HealthResponse.up();
    }
}
