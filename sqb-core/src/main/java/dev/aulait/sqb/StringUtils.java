package dev.aulait.sqb;

public class StringUtils {

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static boolean equalsIgnoreCase(String str1, String str2) {
    if (str1 == null) {
      return str2 == null;
    }
    return str1.equalsIgnoreCase(str2);
  }

  public static boolean isEmpty(String source) {
    return source == null || source.isEmpty();
  }
}
