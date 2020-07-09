package com.plantdata.kgcloud.domain.graph.clash.entity;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiezhenxiang 2019/12/13
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class ClashListReq extends BaseReq {

    @ApiModelProperty("实体名称")
    String name;
}
