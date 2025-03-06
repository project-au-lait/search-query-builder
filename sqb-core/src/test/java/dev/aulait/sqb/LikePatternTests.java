package dev.aulait.sqb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LikePatternTests {
  @Test
  void testContains() {
    assertEquals("%a%", LikePattern.contains("a").toString());
  }

  @Test
  void testEndsWith() {
    assertEquals("%a", LikePattern.endsWith("a").toString());
  }

  @Test
  void testFormat() {
    assertEquals("%a_b_c%", LikePattern.format("%{0}_{1}_{2}%", "a", "b", "c").toString());
  }

  @Test
  void testStartsWith() {
    assertEquals("a%", LikePattern.startsWith("a").toString());
  }

  @Test
  void testEmpty() {
    assertEquals("", LikePattern.contains(null).toString());
    assertEquals("", LikePattern.format("%s", new Object[] {null}).toString());
  }
}
