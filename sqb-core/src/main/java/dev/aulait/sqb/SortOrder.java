package dev.aulait.sqb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortOrder {
  @Schema(required = true)
  private boolean asc;

  @Schema(required = true)
  private String field;
}
