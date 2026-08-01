package dev.quantara.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "contracts")
public class Contract {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Column(name = "deployment_id", nullable = false)
  private String deploymentId;

  @Column(name = "contract_address", nullable = false)
  private String contractAddress;

  @Column(name = "deployment_hash", nullable = false)
  private String deploymentHash;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  protected Contract() {
    // required by JPA
  }

  public Contract(
      Project project, String deploymentId, String contractAddress, String deploymentHash) {
    this.project = project;
    this.deploymentId = deploymentId;
    this.contractAddress = contractAddress;
    this.deploymentHash = deploymentHash;
    this.createdAt = OffsetDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Project getProject() {
    return project;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public String getDeploymentHash() {
    return deploymentHash;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
