package dev.quantara.core.repository;

import dev.quantara.core.entity.Deployment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

  Optional<Deployment> findByDeploymentId(String deploymentId);
}
