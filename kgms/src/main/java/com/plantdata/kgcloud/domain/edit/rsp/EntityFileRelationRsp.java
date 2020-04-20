package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author EYE
 */
@Data
@ApiModel("实体文件关联")
public class EntityFileRelationRsp {

    @ApiModelProperty(value = "实体文件关联id")
    private Integer id;

    @ApiModelProperty(value = "图谱名称")
    private String kgName;

    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "实体名")
    private String entityName;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "数仓文件ID")
    private Integer dwFileId;

    @ApiModelProperty(value = "多模态mongo文件ID")
    private String multiModalId;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

}
