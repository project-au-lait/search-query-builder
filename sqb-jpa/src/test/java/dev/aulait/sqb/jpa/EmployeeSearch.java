package dev.aulait.sqb.jpa;

import static dev.aulait.sqb.ComparisonOperator.*;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import jakarta.persistence.EntityManager;

public class EmployeeSearch {

  public SearchResult<EmployeeEntity> search(EmployeeSearchCriteria input) {
    // Build search criteria from user input
    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT e FROM EmployeeEntity e")
            .where("e.id", LE, input.getIdUpperLimit())
            .where("e.id", GE, input.getIdLowerLimit())
            .build();

    // Create EntityManager according to your runtime environment
    EntityManager em = JpaUtils.em();

    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    // Execute search
    return executor.search(em, criteria);
  }

  public SearchResult<EmployeeEntity> search(
      EmployeeSearchCriteria input, PageControl pageControl) {
    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT e FROM EmployeeEntity e")
            .where("e.id", LE, input.getIdUpperLimit())
            .where("e.id", GE, input.getIdLowerLimit())
            .build(pageControl);

    EntityManager em = JpaUtils.em();

    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    return executor.search(em, criteria);
  }
}
