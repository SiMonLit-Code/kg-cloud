package com.plantdata.kgcloud.domain.graph.config.rsp;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.domain.common.converter.JsonNodeConcerter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱推理模型")
public class GraphConfReasoningRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "关系名称")
    private String ruleName;

    @ApiModelProperty(value = "关系配置")
    private JsonNode ruleConfig;
}
