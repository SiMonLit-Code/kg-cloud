package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 18:17
 */
@ApiModel
@Getter
@Setter
public class DataSetTwoDimStatisticReq {
    @NotBlank
    private String dataName;
    private String query;
    @NotBlank
    private String aggregation;
    private Integer pageSize = 10;
    //    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
}
