package com.plantdata.kgcloud.domain.task.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:27
 * @Description:
 */
@Getter
@Setter
@ApiModel("异步任务结果模型")
public class TaskGraphStatusRsp {
    @ApiModelProperty(value = "任务id")
    private Integer id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "任务类型")
    private String type;

    @ApiModelProperty(value = "任务状态")
    private String status;
}
