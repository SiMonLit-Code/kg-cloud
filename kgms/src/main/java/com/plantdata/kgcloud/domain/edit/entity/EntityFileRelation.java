package com.plantdata.kgcloud.domain.edit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lp
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFileRelation {

    private String id;

    @ApiModelProperty("关联的实体相关信息")
    private List<EntityFileRelationScore> entityAnnotation;

    @ApiModelProperty("标引文件ID")
    private String fileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("创建时间")
    private Date createTime;

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
