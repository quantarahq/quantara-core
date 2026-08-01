package dev.quantara.core.repository;

import dev.quantara.core.entity.Contract;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {

  List<Contract> findByProjectId(Long projectId);
}
