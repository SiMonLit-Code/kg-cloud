package com.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author EYE
 */
@Data
@ApiModel("实体文件关联插入实体")
public class EntityFileRelationReq {

    @NotNull(message = "实体id不能为空")
    @ApiModelProperty(required = true, value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "文件路径")
    private String dataHref;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "简介")
    private String description;

    @NotNull(message = "数仓文件ID不能为空")
    @ApiModelProperty(value = "数仓文件ID")
    private Integer dwFileId;

}
