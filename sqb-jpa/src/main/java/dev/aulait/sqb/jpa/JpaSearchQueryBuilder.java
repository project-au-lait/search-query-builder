package dev.aulait.sqb.jpa;

import dev.aulait.sqb.ComparisonOperator;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchQueryBuilder;
import dev.aulait.sqb.StringUtils;

public class JpaSearchQueryBuilder extends SearchQueryBuilder {
  @Override
  protected String toCountQuery(SearchCriteria criteria) {

    if (StringUtils.isNotEmpty(criteria.getSelectCount())) {
      return criteria.getSelectCount();
    }

    return criteria
        .getSelect()
        .replaceFirst("SELECT ", "SELECT COUNT(")
        .replaceFirst(" FROM", ") FROM")
        .replaceAll("FETCH ", "");
  }

  @Override
  protected String getCorrection(ComparisonOperator operator) {
    if (operator == ComparisonOperator.LIKE) {
      return "ESCAPE '\\'";
    }
    return "";
  }

  @Override
  protected String toParamName() {
    return "?" + (getQueryParams().size() + 1);
  }
}
