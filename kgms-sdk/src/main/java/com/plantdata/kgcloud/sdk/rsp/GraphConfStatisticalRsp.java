package com.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱统计模型")
public class GraphConfStatisticalRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "统计类型")
    private String statisType;

    @ApiModelProperty(value = "统计规则")
    private JsonNode statisRule;


}
