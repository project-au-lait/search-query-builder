package dev.aulait.sqb.jpa;

import static dev.aulait.sqb.ComparisonOperator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.sqb.LikePattern;
import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.PageResult;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class JpaSearchQueryExecutorTests {

  @Test
  void testExample() {
    EmployeeSearchCriteria form = new EmployeeSearchCriteria();
    form.setIdLowerLimit(3);
    form.setIdUpperLimit(5);

    log.info("{}", form);

    SearchResult<EmployeeEntity> result = new EmployeeSearch().search(form);

    log.info("{}", result);

    assertEquals(3, result.getList().size());

    int i = 3;
    for (EmployeeEntity employee : result.getList()) {
      assertEquals(i++, employee.getId());
    }
  }

  @Test
  void testOptionalInput() {
    EmployeeSearchCriteria form = new EmployeeSearchCriteria();
    form.setIdUpperLimit(5);

    log.info("{}", form);

    SearchResult<EmployeeEntity> result = new EmployeeSearch().search(form);

    log.info("{}", result);

    assertEquals(5, result.getList().size());

    int i = 1;
    for (EmployeeEntity employee : result.getList()) {
      assertEquals(i++, employee.getId());
    }
  }

  @Test
  void testPagenation() {
    EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
    criteria.setIdLowerLimit(1);
    criteria.setIdUpperLimit(55);

    log.info("{}", criteria);

    PageControl pageControl = PageControl.builder().pageNumber(3).pageSize(10).build();

    log.info("{}", pageControl);

    SearchResult<EmployeeEntity> result = new EmployeeSearch().search(criteria, pageControl);

    PageResult pageResult = result.getPageResult();

    log.info("{}", pageResult);

    assertEquals(55, pageResult.getCount());
  }

  @Nested
  class OneToManyTests {
    @Test
    void testOneToManyCount() {
      DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
      PageControl pageControl = PageControl.builder().pageNumber(1).pageSize(1).build();

      SearchResult<DepartmentEntity> result = new DepartmentSearch().search(criteria, pageControl);

      assertEquals(10, result.getPageResult().getCount());
    }

    @Test
    void testOneToManyList() {
      DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();

      SearchResult<DepartmentEntity> result = new DepartmentSearch().search(criteria);

      assertEquals(10, result.getList().size());
    }
  }

  @Nested
  class LikeTests {
    @Test
    void testLike() {
      SearchCriteria criteria =
          new SearchCriteriaBuilder()
              .select("SELECT e FROM EmployeeEntity e")
              .where("e.name", LIKE, "employee\\__")
              .where("e.name", LIKE, "%\\_1")
              .build();

      EntityManager em = JpaUtils.em();
      JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
      SearchResult<EmployeeEntity> result = executor.search(em, criteria);

      assertEquals(1, result.getList().size());
      assertEquals(1, result.getList().get(0).getId());
    }

    @Test
    void testLikePattern() {
      SearchCriteria criteria =
          new SearchCriteriaBuilder()
              .select("SELECT e FROM EmployeeEntity e")
              .where("e.name", LIKE, LikePattern.endsWith("\\_1"))
              .build();

      EntityManager em = JpaUtils.em();
      JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
      SearchResult<EmployeeEntity> result = executor.search(em, criteria);

      assertEquals(1, result.getList().size());
      assertEquals(1, result.getList().get(0).getId());
    }

    @Test
    void testLikePatternEmpty() {
      SearchCriteria criteria =
          new SearchCriteriaBuilder()
              .select("SELECT e FROM EmployeeEntity e")
              .where("e.name", LIKE, LikePattern.endsWith(null))
              .build();

      EntityManager em = JpaUtils.em();
      JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
      SearchResult<EmployeeEntity> result = executor.search(em, criteria);

      assertEquals(100, result.getList().size());
    }
  }

  @Nested
  class GroupByTests {
    @Test
    void testGroupBy() {
      SearchCriteria criteria =
          new SearchCriteriaBuilder()
              .select(
                  "SELECT NEW dev.aulait.sqb.jpa.DepartmentAggregate(d, COUNT(e))"
                      + " FROM DepartmentEntity d LEFT JOIN d.employees e")
              .groupBy("d")
              .defaultOrderBy("d.id", true)
              .build(PageControl.builder().pageNumber(1).pageSize(100).build());

      EntityManager em = JpaUtils.em();
      JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
      SearchResult<DepartmentAggregate> result = executor.search(em, criteria);

      assertEquals(10, result.getList().size());
      assertEquals(10, result.getPageResult().getCount());
      assertEquals(1, result.getList().get(0).getDepartment().getId());
      assertEquals(10L, result.getList().get(0).getEmployeeCount());
    }
  }
}
