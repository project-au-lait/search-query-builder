package dev.aulait.sqb;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

class PageResultTests {

  @ParameterizedTest
  @CsvSource({
    // lower bound cases
    "1, 0, 0, 0, 0, ''", // no items
    "1, 1, 1, 1, 1, ''", // only one item
    "1, 10, 1, 10, 1, ''", // only one page
    "1, 11, 1, 10, 2, '1,2'", // two pages

    // pageNumber variation cases
    "1, 100, 1, 10, 10, '1,2,3'", // first page
    "5, 100, 41, 50, 10, '3,4,5,6,7'", // middle page
    "10, 100, 91, 100, 10, '8,9,10'", // last page
  })
  void buildTest(
      int pageNumber,
      int totalItems,
      int expectedStart,
      int expectedEnd,
      int expectedLastPageNum,
      @ConvertWith(StringToIntArrayConverter.class) int[] expectedPageNums) {

    PageControl pageControl = new PageControl();
    pageControl.setPageNumber(pageNumber);

    PageResult result = new PageResult().build(totalItems, pageControl);

    assertEquals(expectedStart, result.getStart());
    assertEquals(expectedEnd, result.getEnd());
    assertEquals(expectedLastPageNum, result.getLastPageNum());
    assertArrayEquals(expectedPageNums, result.getPageNums());
  }
}
