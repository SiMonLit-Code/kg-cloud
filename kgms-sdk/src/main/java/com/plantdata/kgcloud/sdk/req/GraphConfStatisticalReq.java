package com.plantdata.kgcloud.sdk.req;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
@ApiModel("图统计")
@Data
public class GraphConfStatisticalReq {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "统计类型")
    private String statisType;

    @ApiModelProperty(value = "统计规则")
    private JsonNode statisRule;
}
