package com.plantdata.kgcloud.sdk.req;

import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by plantdata-1007 on 2019/12/2.
 */
@ApiModel("问答设置")
@Data
public class GraphConfQaReq {

    @ApiModelProperty("优先级")
    private int priority;

    @ApiModelProperty("类型")
    private int type;

    @ApiModelProperty("问题")
    private String question;

    @ApiModelProperty("选择概念")
    private ArrayNode conceptIds;
}
