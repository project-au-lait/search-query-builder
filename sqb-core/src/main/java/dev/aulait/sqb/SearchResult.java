package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchResult<T> {

  @Builder.Default private List<T> list = new ArrayList<>();

  private PageResult pageResult;
}
