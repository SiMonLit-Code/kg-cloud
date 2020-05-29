package com.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 */
@Data
@ApiModel("实体文件关联插入实体")
public class EntityFileRelationReq {

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("文件ID")
    private String fileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("文件路径")
    private String path;

}
