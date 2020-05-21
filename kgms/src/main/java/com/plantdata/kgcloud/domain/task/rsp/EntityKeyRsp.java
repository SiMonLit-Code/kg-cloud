package com.plantdata.kgcloud.domain.task.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiezhenxiang 2020/5/21
 */
@Data
@ApiModel
public class EntityKeyRsp {

    @ApiModelProperty("实体ID")
    private Long id;
    @ApiModelProperty("入度")
    private Long inCount;
    @ApiModelProperty("出度")
    private Long outCount;
    @ApiModelProperty("总出入度")
    private Long totalCount;
}
