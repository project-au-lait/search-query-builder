package dev.aulait.sqb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SearchQueryBuilderTests {

  SearchQueryBuilder builder = new SearchQueryBuilder();

  @Nested
  class BuildWhereTests {
    @Test
    void testSingleField() {
      List<FieldCriteria> criteria =
          List.of(FieldCriteria.builder().field("field").value("1").build());

      String where = builder.buildWhere(criteria);

      assertEquals("field = ?", where);
      assertEquals("1", builder.getQueryParams().get(0));
    }

    @Test
    void testMultipleField() {
      List<FieldCriteria> criteria =
          List.of(
              FieldCriteria.builder().field("field1").value("1").build(),
              FieldCriteria.builder().field("field2").value("2").build());

      String where = builder.buildWhere(criteria);

      assertEquals("field1 = ? AND field2 = ?", where);
      assertEquals("1", builder.getQueryParams().get(0));
      assertEquals("2", builder.getQueryParams().get(1));
    }

    @Test
    void testIn() {
      List<FieldCriteria> criteria =
          List.of(
              FieldCriteria.builder()
                  .field("field")
                  .comparisonOperator(ComparisonOperator.IN)
                  .value(List.of("1", "2"))
                  .build());

      String where = builder.buildWhere(criteria);

      assertEquals("field IN ?", where);
      assertEquals(List.of("1", "2"), builder.getQueryParams().get(0));
    }
  }

  @Test
  void testOrderBy() {
    List<SortOrder> orders = List.of(SortOrder.builder().field("field").asc(true).build());
    String orderBy = builder.buildOrderBy(orders);

    assertEquals(" ORDER BY field ASC", orderBy);
  }
}
