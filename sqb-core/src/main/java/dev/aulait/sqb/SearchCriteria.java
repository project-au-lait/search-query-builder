package dev.aulait.sqb;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchCriteria {
  private String select;
  private String selectCount;
  @Builder.Default private List<FieldCriteria> fieldCriteria = new ArrayList<>();
  private PageControl pageControl;
  @Builder.Default private List<SortOrder> sortOrders = new ArrayList<>();
}
