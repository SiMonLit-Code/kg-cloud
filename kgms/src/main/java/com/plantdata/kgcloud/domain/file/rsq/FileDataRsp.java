package com.plantdata.kgcloud.domain.file.rsq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lp
 * @date 2020/5/20 17:15
 */
@Data
public class FileDataRsp {

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

    @ApiModelProperty("数仓id")
    private Long databaseId;

    @ApiModelProperty("所属文件夹")
    private Long tableId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
