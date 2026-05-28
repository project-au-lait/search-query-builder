package dev.aulait.sqb.jpa;

import static dev.aulait.sqb.ComparisonOperator.*;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import jakarta.persistence.EntityManager;

public class DepartmentSearch {

  public SearchResult<DepartmentEntity> search(DepartmentSearchCriteria input) {
    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT d FROM DepartmentEntity d LEFT JOIN FETCH d.employees")
            .where("d.id", LE, input.getIdUpperLimit())
            .build();

    EntityManager em = JpaUtils.em();

    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    return executor.search(em, criteria);
  }

  public SearchResult<DepartmentEntity> search(
      DepartmentSearchCriteria input, PageControl pageControl) {
    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT d FROM DepartmentEntity d LEFT JOIN FETCH d.employees")
            .where("d.id", LE, input.getIdUpperLimit())
            .build(pageControl);

    EntityManager em = JpaUtils.em();

    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    return executor.search(em, criteria);
  }
}
