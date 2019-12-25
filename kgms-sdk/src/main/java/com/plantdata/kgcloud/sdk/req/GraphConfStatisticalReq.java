package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
@ApiModel("图统计")
@Data
public class GraphConfStatisticalReq {



    @ApiModelProperty(value = "kgName" ,required = true)
    private String kgName;

    @ApiModelProperty(value = "统计类型")
    private String statisType;

    @ApiModelProperty(value = "统计规则")
    private Map<String,Object> statisRule;
}
