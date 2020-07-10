package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ?
 */
@Getter
@Setter
@ApiModel("节点参数模型")
public class NodeBean {
    /**
     * 0 entity 1 literal
     */
    @ApiModelProperty("节点类型， 0为实体，1为属性")
    private int type;
    @ApiModelProperty("节点id")
    private Long id;
    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("值类型")
    private int valueType;
    @ApiModelProperty("值")
    private Object value;


}
