package com.plantdata.kgcloud.plantdata.req.data;


import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ConceptParameter {
  @NotBlank
  private String kgName;
  private Long conceptId;
  private String conceptIdKey;

}
