package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaBuilder {

  private StringBuilder select = new StringBuilder();
  private String selectCount;
  private List<FieldCriteria> fields = new ArrayList<>();
  private List<SortOrder> sortOrders = new ArrayList<>();

  public SearchCriteriaBuilder select(String select) {
    if (this.select.length() > 0) {
      this.select.append(" ");
    }
    this.select.append(select);
    return this;
  }

  public SearchCriteriaBuilder selectCount(String selectCount) {
    this.selectCount = selectCount;
    return this;
  }

  public SearchCriteriaBuilder where(String field, Object value) {
    return where(field, ComparisonOperator.EQ, value);
  }

  public SearchCriteriaBuilder where(
      String field, ComparisonOperator comparisonOperator, Object value) {
    return where(LogicalOperator.AND, field, comparisonOperator, value);
  }

  public SearchCriteriaBuilder where(
      LogicalOperator logicalOperator,
      String field,
      ComparisonOperator comparisonOperator,
      Object value) {

    fields.add(
        FieldCriteria.builder()
            .logicalOperator(logicalOperator)
            .field(field)
            .comparisonOperator(comparisonOperator)
            .value(value)
            .build());

    return this;
  }

  public SearchCriteriaBuilder defaultOrderBy(String field, boolean asc) {
    sortOrders.add(SortOrder.builder().field(field).asc(asc).build());
    return this;
  }

  public SearchCriteriaBuilder orderBy(List<SortOrder> sortOrders) {
    if (sortOrders != null && !sortOrders.isEmpty()) {
      this.sortOrders = sortOrders;
    }
    return this;
  }

  public SearchCriteria build(PageControl pageControl) {
    return SearchCriteria.builder()
        .select(select.toString())
        .selectCount(selectCount)
        .fieldCriteria(fields)
        .pageControl(pageControl)
        .sortOrders(sortOrders)
        .build();
  }

  public SearchCriteria build() {
    return build(null);
  }
}
