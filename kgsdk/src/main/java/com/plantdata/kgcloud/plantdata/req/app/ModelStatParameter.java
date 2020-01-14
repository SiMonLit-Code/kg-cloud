package com.plantdata.kgcloud.plantdata.req.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long conceptId;


}
