package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 19:54
 **/
@Data
public class DWFileTableRsp {

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

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("拥有者")
    private String owner;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("所属文件夹")
    private Long tableId;

    @ApiModelProperty("数仓id")
    private Long dataBaseId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
