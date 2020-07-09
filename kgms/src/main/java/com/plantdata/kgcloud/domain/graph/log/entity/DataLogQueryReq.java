package com.plantdata.kgcloud.domain.graph.log.entity;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author xiezhenxiang 2020/1/15
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DataLogQueryReq extends BaseReq {

    @ApiModelProperty("实体ID")
    private Long id;
    @ApiModelProperty("实体类型 0概念 1实体")
    private Integer type;
    @ApiModelProperty("关系ID[]")
    private List<Integer> attrIds;
    @ApiParam("属性定义ID[]")
    private List<Integer> attrDefineIds;
}
