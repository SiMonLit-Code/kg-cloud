package com.plantdata.kgcloud.domain.task.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/27 12:14
 */
@Data
public class TaskGraphSnapshotNameReq {

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("备份文件名")
    private String fileName;

    @ApiModelProperty("用户ID")
    private String userId;

}
