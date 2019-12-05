package com.plantdata.kgcloud.domain.graph.config.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
@ApiModel("图统计")
@Data
public class GraphConfStatisticalReq {

    @ApiModelProperty(value = "统计类型")
    private String statisType;

    @ApiModelProperty(value = "统计规则")
    private String statisRule;
}
