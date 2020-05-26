package com.plantdata.kgcloud.domain.task.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author xiezhenxiang 2020/5/21
 */
@ApiModel
@Data
public class EntityIntimateRsp {

    @ApiModelProperty("始节点ID")
    private Long entityId;
    @ApiModelProperty("终节点ID")
    private Long attrValue;
    @ApiModelProperty("亲密度")
    private Integer intimate;
    @ApiModelProperty("关系ID->亲密值")
    private Map<Integer, Integer> attrCount;
}
