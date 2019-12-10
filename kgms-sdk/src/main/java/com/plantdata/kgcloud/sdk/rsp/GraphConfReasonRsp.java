package com.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱推理模型")
public class GraphConfReasonRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "关系名称")
    private String ruleName;

    @ApiModelProperty(value = "关系配置")
    private JsonNode ruleConfig;
}
