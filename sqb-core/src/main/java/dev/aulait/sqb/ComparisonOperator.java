package dev.aulait.sqb;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ComparisonOperator {
  EQ("="),
  NE("<>"),
  GT(">"),
  GE(">="),
  LT("<"),
  LE("<="),
  LIKE("LIKE"),
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
