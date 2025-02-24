package dev.aulait.sqb.jpa;

import dev.aulait.sqb.PageResult;
import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaSearchQueryExecutor {

  public <T> SearchResult<T> search(EntityManager em, SearchCriteria criteria) {
    if (criteria.getPageControl() == null) {
      return searchWithoutPageControl(em, criteria);
    } else {
      return searchWithPageControl(em, criteria);
    }
  }

  public <T> SearchResult<T> searchWithPageControl(EntityManager em, SearchCriteria criteria) {

    JpaSearchQueryBuilder builder = new JpaSearchQueryBuilder();
    builder.buildQuery(criteria);

    String countQueryStr = builder.getCountQuery();
    log.debug("Count query: {}", countQueryStr);

    Query countQuery = em.createQuery(countQueryStr);
    setQueryParams(countQuery, builder.getQueryParams());
    long count = (Long) countQuery.getSingleResult();

    if (count == 0) {
      return SearchResult.<T>builder()
          .pageResult(new PageResult().build(count, criteria.getPageControl()))
          .build();
    }

    String searchQueryStr = builder.getSearchQuery();
    log.debug("Search query: {}", searchQueryStr);

    Query searchQuery = em.createQuery(searchQueryStr);
    setQueryParams(searchQuery, builder.getQueryParams());
    searchQuery.setMaxResults(criteria.getPageControl().getPageSize());
    searchQuery.setFirstResult(criteria.getPageControl().getOffset());

    @SuppressWarnings("unchecked")
    List<T> result = searchQuery.getResultList();

    return SearchResult.<T>builder()
        .list(result)
        .pageResult(new PageResult().build(count, criteria.getPageControl()))
        .build();
  }

  public <T> SearchResult<T> searchWithoutPageControl(EntityManager em, SearchCriteria criteria) {
    JpaSearchQueryBuilder builder = new JpaSearchQueryBuilder();
    builder.buildQuery(criteria);

    String searchQueryStr = builder.getSearchQuery();
    log.debug("Search query: {}", searchQueryStr);

    Query searchQuery = em.createQuery(searchQueryStr);
    setQueryParams(searchQuery, builder.getQueryParams());

    @SuppressWarnings("unchecked")
    List<T> result = searchQuery.getResultList();

    return SearchResult.<T>builder().list(result).build();
  }

  private void setQueryParams(Query query, List<Object> params) {
    for (int i = 0; i < params.size(); i++) {
      query.setParameter(i + 1, params.get(i));
    }
  }
}
