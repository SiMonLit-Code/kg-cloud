package com.plantdata.kgcloud.plantdata.req.app;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ModelStatParameter {
    @NotBlank
    private String kgName;
    private Boolean isDisplay = true;
    @NotNull
    private long conceptId = 0L;


}
