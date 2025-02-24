package dev.aulait.sqb.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.PageResult;
import dev.aulait.sqb.SearchResult;
import lombok.extern.slf4j.Slf4j;
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
}
