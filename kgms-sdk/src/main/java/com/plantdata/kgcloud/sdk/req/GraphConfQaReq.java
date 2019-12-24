package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by plantdata-1007 on 2019/12/2.
 */
@ApiModel("问答设置")
@Data
public class GraphConfQaReq {

    @ApiModelProperty("优先级")
    private int priority;

    @ApiModelProperty(value = "类型",required = true)
    private int type;

    @ApiModelProperty(value = "问题",required = true)
    private String question;

    @ApiModelProperty(value = "选择概念",required = true)
    private List<Long> conceptIds;
}
