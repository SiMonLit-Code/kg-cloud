package com.plantdata.kgcloud.domain.file.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 17:41
 */
@Data
public class FileData {

    private String id;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("缩略图路径")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("拥有者")
    private String owner;

    @ApiModelProperty("文件系统id")
    private Long fileSystemId;

    @ApiModelProperty("所属文件夹id")
    private Long folderId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("关系图谱列表")
    private List<String> kgNames;

}
