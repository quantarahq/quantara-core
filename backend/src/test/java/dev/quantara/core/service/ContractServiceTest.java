package dev.quantara.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.quantara.core.dto.ContractResponse;
import dev.quantara.core.entity.Contract;
import dev.quantara.core.entity.Deployment;
import dev.quantara.core.entity.DeploymentStatus;
import dev.quantara.core.entity.Project;
import dev.quantara.core.repository.ContractRepository;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

  @Mock private ContractRepository contractRepository;

  @InjectMocks private ContractService contractService;

  @Test
  void registerContractPersistsSimulatedMetadata() throws Exception {
    var project = new Project("example-contract", null);
    setId(project, 1L);
    var deployment = new Deployment("deploy-001", project, "example", DeploymentStatus.SUCCESS);

    when(contractRepository.save(any(Contract.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ContractResponse response = contractService.registerContract(deployment);

    assertThat(response.deploymentId()).isEqualTo("deploy-001");
    assertThat(response.contractAddress()).startsWith("C");
    assertThat(response.deploymentHash()).hasSize(64); // sha-256 hex digest

    ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
    verify(contractRepository).save(captor.capture());
    assertThat(captor.getValue().getProject()).isEqualTo(project);
  }

  private void setId(Project project, Long id) throws Exception {
    Field idField = Project.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(project, id);
  }
}
