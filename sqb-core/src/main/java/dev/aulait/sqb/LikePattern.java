package dev.aulait.sqb;

import java.text.MessageFormat;
import java.util.Arrays;
import lombok.Builder;

@Builder(access = lombok.AccessLevel.PRIVATE)
public class LikePattern {
  private Object[] values;
  private String msgFmtPtn;
  private LikeType type;

  /**
   * Create a LikePattern to be evaluated to %value% in LIKE clause.
   *
   * @param value value to be evaluated
   * @return LikePattern
   */
  public static LikePattern contains(Object value) {
    return LikePattern.builder().values(new Object[] {value}).type(LikeType.CONTAINS).build();
  }

  /**
   * Create a LikePattern to be evaluated to value% in LIKE clause.
   *
   * @param value value to be evaluated
   * @return LikePattern
   */
  public static LikePattern startsWith(Object value) {
    return LikePattern.builder().values(new Object[] {value}).type(LikeType.STARTS_WITH).build();
  }

  /**
   * Create a LikePattern to be evaluated to %value in LIKE clause.
   *
   * @param value value to be evaluated
   * @return LikePattern
   */
  public static LikePattern endsWith(Object value) {
    return LikePattern.builder().values(new Object[] {value}).type(LikeType.ENDS_WITH).build();
  }

  /**
   * Create a LikePattern to be evaluated to MessageFormat.format(messageFormatPattern, values) in
   * LIKE clause.
   *
   * @param messageFormatPattern message format pattern e.g. "%{0}_{1}_{2}%"
   * @param value value to be evaluated
   * @return LikePattern
   * @see MessageFormat#format(String, Object...)
   */
  public static LikePattern format(String messageFormatPattern, Object... values) {
    return LikePattern.builder()
        .msgFmtPtn(messageFormatPattern)
        .values(values)
        .type(LikeType.FORMAT)
        .build();
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "";
    }

    switch (type) {
      case CONTAINS:
        return "%" + values[0] + "%";
      case STARTS_WITH:
        return values[0] + "%";
      case ENDS_WITH:
        return "%" + values[0];
      case FORMAT:
        return MessageFormat.format(msgFmtPtn, values);
      default:
        return Arrays.toString(values);
    }
  }

  private boolean isEmpty() {
    return values == null
        || values.length == 0
        || Arrays.stream(values).allMatch(value -> value == null || value.toString().isEmpty());
  }

  private enum LikeType {
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    FORMAT;
  }
}
