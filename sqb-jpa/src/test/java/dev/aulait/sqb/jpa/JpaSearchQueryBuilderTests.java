package dev.aulait.sqb.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchQueryBuilder;
import org.junit.jupiter.api.Test;

class JpaSearchQueryBuilderTests {

  SearchQueryBuilder builder = new JpaSearchQueryBuilder();

  @Test
  void test() {
    SearchCriteriaBuilder cBuilder = new SearchCriteriaBuilder();
    cBuilder
        .select("SELECT t FROM TestEntity t")
        .select("JOIN FETCH t.join j")
        .where("t.field", "a")
        .where("j.field", "b");

    builder.buildQuery(cBuilder.build(new PageControl()));

    assertEquals(
        "SELECT COUNT(t) FROM TestEntity t JOIN t.join j WHERE t.field = ?1 AND j.field = ?2",
        builder.getCountQuery());

    assertEquals(
        "SELECT t FROM TestEntity t JOIN FETCH t.join j WHERE t.field = ?1 AND j.field = ?2",
        builder.getSearchQuery());
  }
}
