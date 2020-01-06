package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * Created by jdm on 2019/12/9 15:38.
 */
@ApiModel("图推理")
@Data
public class GraphConfReasonReq {

    @ApiModelProperty(value = "关系名称")
    private String ruleName;

    @ApiModelProperty(value = "关系配置")
    private Map<String,Object> ruleConfig;
}
