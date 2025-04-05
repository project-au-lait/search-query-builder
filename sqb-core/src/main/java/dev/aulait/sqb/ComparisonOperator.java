package dev.aulait.sqb;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** ComparisonOperator is used to specify the comparison operator in a search criteria. */
@AllArgsConstructor
public enum ComparisonOperator {
  /** Comparison operator for equality. This operator is evaluated to "=" in SQL/JPQL. */
  EQ("="),
  /** Comparison operator for not equal. This operator is evaluated to "&lt;&gt;" in SQL/JPQL. */
  NE("<>"),
  /** Comparison operator for greater than. This operator is evaluated to "&gt;" in SQL/JPQL. */
  GT(">"),
  /**
   * Comparison operator for greater than or equal. This operator is evaluated to "&gt;=" in
   * SQL/JPQL.
   */
  GE(">="),
  /** Comparison operator for less than. This operator is evaluated to "&lt;" in SQL/JPQL. */
  LT("<"),
  /**
   * Comparison operator for less than or equal. This operator is evaluated to "&lt;=" in SQL/JPQL.
   */
  LE("<="),
  /**
   * Comparison operator for "like". This operator is evaluated to "LIKE" in SQL/JPQL. When using
   * this operator, the value should be a {@link LikePattern} object.
   *
   * @see LikePattern
   */
  LIKE("LIKE"),
  /**
   * Comparison operator for "is not null". This operator is evaluated to "IS NOT NULL" in SQL/JPQL.
   */
  IN("IN");

  @Getter private String value;

  public static ComparisonOperator parse(String operator) {
    for (ComparisonOperator cd : values()) {
      if (StringUtils.equalsIgnoreCase(cd.name(), operator)) {
        return cd;
      }
    }

    throw new IllegalArgumentException(
        operator + " is not parsed to " + ComparisonOperator.class.getSimpleName());
  }
}
