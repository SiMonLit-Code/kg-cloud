package com.plantdata.kgcloud.domain.task.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lp
 * @date 2020/5/27 12:14
 */
@Data
public class TaskGraphSnapshotNameReq {

    @NotNull
    @ApiModelProperty("任务名称")
    private String name;

    @NotNull
    @ApiModelProperty("备份原图谱名")
    private String kgName;

    @NotNull
    @ApiModelProperty("备份文件路径")
    private String fileName;

    @NotNull
    @ApiModelProperty("用户ID")
    private String userId;

    @NotNull
    @ApiModelProperty("文件备份类型(0：仅图谱数据，1：图谱数据和多模态数据)")
    private Integer fileBackupType;

}
