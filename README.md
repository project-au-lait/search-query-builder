# Search Query Builder

Search Query Builder (SQB) is a lightweight Java library for constructing and executing SELECT statements for search functions with the following features:

- **Optional Search Conditions**: Only include search conditions in the SELECT statement if they are provided; exclude them if they are not.
- **Pagination**: Retrieve search results divided into multiple pages.
- **Sorting**: Sort search results based on specified conditions.

SQB is intended to be used with JPA in version 0.8.

## Usage

SQB is available on Maven Central Repository.
To use SQB, add the following dependency to your pom.xml:

```xml
    <dependency>
      <groupId>dev.aulait.sqb</groupId>
      <artifactId>sqb-jpa</artifactId>
      <version>0.8</version>
    </dependency>
```

The following example demonstrates a search on an employee table with 100 records.

| id  | name         |
| --- | ------------ |
| 1   | employee_1   |
| 2   | employee_2   |
| 3   | employee_3   |
| ... | ...          |
| 100 | employee_100 |

The corresponding JPA Entity for the employee table is as follows:

```java
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "employee")
public class EmployeeEntity implements java.io.Serializable {

  @EqualsAndHashCode.Include
  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;
}
```

The class that performs the search on the employee table using SQB is as follows:

```java
import static dev.aulait.sqb.ComparisonOperator.*;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import jakarta.persistence.EntityManager;

public class EmployeeSearch {

  public SearchResult<EmployeeEntity> search(EmployeeSearchCriteria input) {
    // Build search criteria from user input
    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT e FROM EmployeeEntity e")
            .where("e.id", LE, input.getIdUpperLimit())
            .where("e.id", GE, input.getIdLowerLimit())
            .build();

    // Create EntityManager according to your runtime environment
    EntityManager em = ...;

    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    // Execute search
    return executor.search(em, criteria);
  }

}
```

If `EmployeeSearchCriteria(idLowerLimit=3, idUpperLimit=5)` is provided, the following SQL will be executed inside `JpaSearchQueryExecutor.search`:

```sql
    select
        ee1_0."id",
        ee1_0."department_id",
        ee1_0."name" 
    from
        "employee" ee1_0 
    where
        ee1_0."id"<=? 
        and ee1_0."id">=? 
```

The result of this search will be `SearchResult(list=[EmployeeEntity(id=3, name=employee_3), EmployeeEntity(id=4, name=employee_4), EmployeeEntity(id=5, name=employee_5)])`.

### Optional Search Conditions

In the above example, if `lowerLimit` is not provided, i.e., `EmployeeSearchCriteria(lowerLimit=null, upperLimit=10)`, the condition `ee1_0."id">=?` will be excluded from the executed SQL as follows:

```sql
    select
        ee1_0."id",
        ee1_0."department_id",
        ee1_0."name" 
    from
        "employee" ee1_0 
    where
        ee1_0."id"<=? 
```

### Pagination

SQB also provides functionality for pagination.
The `EmployeeSearch` class with pagination support is as follows:

```java
import dev.aulait.sqb.PageControl;

public class EmployeeSearch {

  public SearchResult<EmployeeEntity> search(
      EmployeeSearchCriteria input, PageControl pageControl) {

    // ...

    SearchCriteria criteria =
        new SearchCriteriaBuilder()
            .select("SELECT e FROM EmployeeEntity e")
            .where("e.id", LE, input.getUpperLimit())
            .where("e.id", GE, input.getLowerLimit())
            .build(pageControl);

    // ...
  }

}
```

If `EmployeeSearchCriteria(idLowerLimit=1, idUpperLimit=55)` and `PageControl(pageSize=10, pageNumber=3, pageNumsRange=2)` are provided, the following two SQL statements will be executed:

```sql
    select
        count(ee1_0."id") 
    from
        "employee" ee1_0 
    where
        ee1_0."id"<=? 
        and ee1_0."id">=?
```

```sql
    select
        ee1_0."id",
        ee1_0."department_id",
        ee1_0."name" 
    from
        "employee" ee1_0 
    where
        ee1_0."id"<=? 
        and ee1_0."id">=? 
    offset
        ? rows -- ? = 20
    fetch
        first ? rows only  -- ? = 10
```

The result of this search will be `SearchResult(list=[EmployeeEntity(id=1, name=employee_1), ..., EmployeeEntity(id=55, name=employee_55)], pageResult=(count=55, start=21, end=30, lastPageNum=6, pageNums=[1, 2, 3, 4, 5]))`.
