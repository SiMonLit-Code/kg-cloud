package com.plantdata.kgcloud.domain.graph.log.entity;

import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiezhenxiang 2020/1/15
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class ServiceLogReq extends BaseReq {

    @ApiModelProperty("业务功能模块")
    private ServiceEnum serviceEnum;
    @ApiParam("开始时间")
    private Long startTime;
    @ApiParam("结束时间")
    private Long endTime;
}
