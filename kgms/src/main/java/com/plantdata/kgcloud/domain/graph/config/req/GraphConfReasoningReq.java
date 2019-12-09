package com.plantdata.kgcloud.domain.graph.config.req;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by jdm on 2019/12/9 15:38.
 */
@ApiModel("图统计")
@Data
public class GraphConfReasoningReq {

    @ApiModelProperty(value = "关系名称")
    private String ruleName;

    @ApiModelProperty(value = "关系配置")
    private JsonNode ruleConfig;
}
