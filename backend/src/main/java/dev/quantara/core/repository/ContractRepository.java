package dev.quantara.core.repository;

import dev.quantara.core.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByProjectId(Long projectId);
}
