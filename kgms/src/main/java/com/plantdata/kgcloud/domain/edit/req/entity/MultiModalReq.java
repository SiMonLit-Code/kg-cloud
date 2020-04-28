package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/23 10:42
 * @Description:
 */
@Setter
@Getter
@ApiModel("多模态数据")
public class MultiModalReq {
    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("缩略图路径")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("上传类型(0：上传文件，1：选择文件)")
    private Integer uploadType;

    @ApiModelProperty("数仓id")
    private Long dataBaseId;

    @ApiModelProperty("所属文件夹id")
    private Long tableId;

    @ApiModelProperty("数仓文件id(上传类型为1传值)")
    private String dwFileId;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;
}
