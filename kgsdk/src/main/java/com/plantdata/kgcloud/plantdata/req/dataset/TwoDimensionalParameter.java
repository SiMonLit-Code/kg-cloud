package com.plantdata.kgcloud.plantdata.req.dataset;

import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TwoDimensionalParameter {
    @NotBlank
    private String dataName;
    private String query;
    @NotBlank
    private String aggs;
    private Integer pageSize = 10;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
}
