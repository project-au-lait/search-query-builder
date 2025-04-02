package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for creating search criteria to construct a SELECT statement with JOIN, WHERE, and
 * ORDER BY clauses. Refer to <a
 * href="https://github.com/project-au-lait/search-query-builder?tab=readme-ov-file#usage">the
 * product documentation</a> for more details.
 */
public class SearchCriteriaBuilder {

  private StringBuilder select = new StringBuilder();
  private String selectCount;
  private List<FieldCriteria> fields = new ArrayList<>();
  private List<SortOrder> sortOrders = new ArrayList<>();

  /**
   * Append a part of the SELECT clause of the SQL/JPQL.
   *
   * @param select the part of the SELECT clause to append
   * @return this builder instance
   */
  public SearchCriteriaBuilder select(String select) {
    if (this.select.length() > 0) {
      this.select.append(" ");
    }
    this.select.append(select);
    return this;
  }

  /**
   * Set the SELECT COUNT clause of the SQL. If you're using SQB with JPA, this method is optional
   * because the SELECT COUNT clause is generated automatically.
   *
   * @param selectCount the SELECT COUNT clause to set
   * @return this builder instance
   */
  public SearchCriteriaBuilder selectCount(String selectCount) {
    this.selectCount = selectCount;
    return this;
  }

  /**
   * Add a field to the WHERE clause of the SQL/JPQL using the AND and EQUALS (=) operators. If the
   * field is not set, the criterion added by this method will be ignored.
   *
   * <p>If you're using SQB with JPA, the value is converted to the type of the field in the entity.
   *
   * @param field the field to add to the WHERE clause
   * @param value the value to compare the field with
   * @return this builder instance
   * @see {@link #where(LogicalOperator, String, ComparisonOperator, Object)}
   */
  public SearchCriteriaBuilder where(String field, Object value) {
    return where(field, ComparisonOperator.EQ, value);
  }

  /**
   * Add a field to the WHERE clause of the SQL/JPQL using the AND operator and the specified
   * comparison operator.
   *
   * <p>If you're using SQB with JPA, the value is converted to the type of the field in the entity.
   *
   * @param field the field to add to the WHERE clause
   * @param comparisonOperator the comparison operator to use
   * @param value the value to compare the field with
   * @return this builder instance
   * @see {@link #where(LogicalOperator, String, ComparisonOperator, Object)}
   */
  public SearchCriteriaBuilder where(
      String field, ComparisonOperator comparisonOperator, Object value) {
    return where(LogicalOperator.AND, field, comparisonOperator, value);
  }

  /**
   * Add a field to the WHERE clause of the SQL/JPQL using the specified logical and comparison
   * operators. If the value is 'ignorable', the criterion added by this method will be ignored.
   * 'Ignorable' means the value is null or empty (in the case of String or Collection values).
   *
   * <p>If you're using SQB with JPA, the value is converted to the type of the field in the entity.
   *
   * @param logicalOperator the logical operator to use
   * @param field the field to add to the WHERE clause
   * @param comparisonOperator the comparison operator to use
   * @param value the value to compare the field with
   * @return this builder instance
   */
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

  /**
   * Add a field to the ORDER BY clause of the SQL/JPQL. This clause is evaluated if {@link
   * #orderBy(List)} is not called or is called with an empty list.
   *
   * @param field the field to add to the ORDER BY clause
   * @param asc true if the field should be sorted in ascending order, false for descending order
   * @return this builder instance
   * @see {@link #orderBy(List)}
   */
  public SearchCriteriaBuilder defaultOrderBy(String field, boolean asc) {
    sortOrders.add(SortOrder.builder().field(field).asc(asc).build());
    return this;
  }

  /**
   * Add SortOrders to the ORDER BY clause of the SQL/JPQL.
   *
   * @param sortOrders the list of SortOrder to add
   * @return this builder instance
   */
  public SearchCriteriaBuilder orderBy(List<SortOrder> sortOrders) {
    if (sortOrders != null && !sortOrders.isEmpty()) {
      this.sortOrders = sortOrders;
    }
    return this;
  }

  /**
   * Build the SearchCriteria object. This method is used to create a SearchCriteria object that
   * contains the SQL/JPQL SELECT statement, the WHERE clause, and the ORDER BY clause.
   *
   * <p>If you're using SQB with JPA, the SearchCriteria object is executed by the {@link
   * JpaSearchQueryExecutor}.
   *
   * @param pageControl the PageControl object that contains the pagination information.
   * @return the SearchCriteria object
   */
  public SearchCriteria build(PageControl pageControl) {
    return SearchCriteria.builder()
        .select(select.toString())
        .selectCount(selectCount)
        .fieldCriteria(fields)
        .pageControl(pageControl)
        .sortOrders(sortOrders)
        .build();
  }

  /**
   * Build the SearchCriteria object with no PageControl.
   *
   * @return the SearchCriteria object
   * @see {@link #build(PageControl)}
   */
  public SearchCriteria build() {
    return build(null);
  }
}
