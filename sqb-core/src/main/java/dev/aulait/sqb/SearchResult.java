package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult<T> {

  @Builder.Default private List<T> list = new ArrayList<>();

  private PageResult pageResult;
}
