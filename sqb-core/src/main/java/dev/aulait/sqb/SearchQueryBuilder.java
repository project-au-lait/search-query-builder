package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public class SearchQueryBuilder {

  @Getter private List<Object> queryParams = new ArrayList<>();

  @Getter private String countQuery;

  @Getter private String searchQuery;

  public void buildQuery(SearchCriteria criteria) {
    StringBuilder search = new StringBuilder();
    StringBuilder count = new StringBuilder();

    String select = criteria.getSelect();
    search.append(select);
    count.append(toCountQuery(criteria));

    String where = buildWhere(criteria.getFieldCriteria());
    if (StringUtils.isNotEmpty(where)) {
      search.append(" WHERE ").append(where);
      count.append(" WHERE ").append(where);
    }

    search.append(buildOrderBy(criteria.getSortOrders()));

    searchQuery = search.toString();
    countQuery = count.toString();
  }

  protected String toCountQuery(SearchCriteria criteria) {
    return criteria.getSelectCount();
  }

  String buildWhere(List<FieldCriteria> fieldCriteria) {
    StringBuilder sb = new StringBuilder();

    for (FieldCriteria fieldCriterion : fieldCriteria) {

      if (!fieldCriterion.hasValue()) {
        continue;
      }

      if (sb.length() > 0) {
        sb.append(" " + fieldCriterion.getLogicalOperator() + " ");
      }

      String field = fieldCriterion.getField();
      sb.append(field + " ");

      ComparisonOperator operator = fieldCriterion.getComparisonOperator();

      sb.append(operator.getValue() + " " + toParamName());

      queryParams.add(toValue(operator, fieldCriterion.getValue()));
    }

    return sb.toString();
  }

  String buildOrderBy(List<SortOrder> sortOrders) {
    if (sortOrders == null || sortOrders.isEmpty()) {
      return "";
    }

    return " ORDER BY "
        + sortOrders.stream()
            .map(order -> order.getField() + " " + (order.isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.joining(","));
  }

  Object toValue(ComparisonOperator operator, Object value) {
    switch (operator) {
      case LIKE:
        return "%" + value + "%";

      default:
        return value;
    }
  }

  protected String toParamName() {
    return "?";
  }
}
