package dev.quantara.core.repository;

import dev.quantara.core.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    Optional<Deployment> findByDeploymentId(String deploymentId);
}
