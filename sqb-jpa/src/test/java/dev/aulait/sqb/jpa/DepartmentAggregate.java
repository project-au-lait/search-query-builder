package dev.aulait.sqb.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAggregate {
  private DepartmentEntity department;
  private long employeeCount;
}
