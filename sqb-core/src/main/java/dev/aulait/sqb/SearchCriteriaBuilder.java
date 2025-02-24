package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaBuilder {

  private StringBuilder select = new StringBuilder();
  private String selectCount;
  private List<FieldCriteria> fields = new ArrayList<>();
  private SortOrder defaultOrder;

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
    this.defaultOrder = SortOrder.builder().field(field).asc(asc).build();
    return this;
  }

  public SearchCriteria build(PageControl pageControl, List<SortOrder> sortOrders) {
    List<SortOrder> so = new ArrayList<>();
    if (defaultOrder != null && sortOrders == null) {
      so.add(defaultOrder);
    }

    return SearchCriteria.builder()
        .select(select.toString())
        .selectCount(selectCount)
        .fieldCriteria(fields)
        .pageControl(pageControl)
        .sortOrders(so)
        .build();
  }

  public SearchCriteria build(PageControl pageControl) {
    return build(pageControl, null);
  }

  public SearchCriteria build() {
    return build(null, null);
  }
}
