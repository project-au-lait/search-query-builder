package dev.aulait.sqb.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Generated("dev.aulait.jeg:jpa-entity-generator")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "depertment")
public class DepertmentEntity implements java.io.Serializable {

  @EqualsAndHashCode.Include
  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "depertment")
  private Set<EmployeeEntity> employees;
}
