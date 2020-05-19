package com.plantdata.kgcloud.domain.task.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:45
 * @Description:
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("异步任务创建模型")
public class TaskGraphStatusReq {

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "任务类型")
    private String type;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "请求参数")
    private Map<String,Object> params;
}
