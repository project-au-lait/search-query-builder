package dev.aulait.sqb.jpa;

import lombok.Data;

@Data
public class EmployeeSearchCriteria {
  private Integer idLowerLimit;
  private Integer idUpperLimit;
}
