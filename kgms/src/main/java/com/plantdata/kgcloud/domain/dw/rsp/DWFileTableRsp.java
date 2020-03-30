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

    private Integer id;

    @ApiModelProperty("文件名")
    private String name;

    private Date createAt;

    private Date updateAt;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("拥有者")
    private String owner;

    private String userId;

    private Long tableId;
    private Long dataBaseId;
}
