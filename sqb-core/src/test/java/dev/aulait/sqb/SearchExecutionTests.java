package dev.aulait.sqb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.junit.jupiter.api.Test;

class SearchExecutionTests {

  @Test
  void test() throws SQLException {
    ResourceBundle bundle = ResourceBundle.getBundle("connection");

    SearchQueryBuilder builder = new SearchQueryBuilder();
    builder.buildQuery(
        new SearchCriteriaBuilder()
            .select("SELECT * FROM employee")
            .where("id", "1")
            .build(new PageControl()));

    try (var connection =
        DriverManager.getConnection(
            bundle.getString("url"), bundle.getString("user"), bundle.getString("password"))) {

      var stmt = connection.prepareStatement(builder.getSearchQuery());

      stmt.setInt(1, 1);

      var rs = stmt.executeQuery();
      rs.next();
      assertEquals("1", rs.getString("id"));
    }
  }
}
