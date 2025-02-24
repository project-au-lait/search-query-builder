package dev.aulait.sqb;

import java.util.Collection;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldCriteria {
  private String field;
  @Builder.Default private ComparisonOperator comparisonOperator = ComparisonOperator.EQ;
  @Builder.Default private LogicalOperator logicalOperator = LogicalOperator.AND;
  private Object value;

  public boolean hasValue() {
    if (value == null) {
      return false;
    }

    if (value.toString().isEmpty()) {
      return false;
    }

    if (value instanceof Collection) {
      return !Collection.class.cast(value).isEmpty();
    }

    return true;
  }
}
