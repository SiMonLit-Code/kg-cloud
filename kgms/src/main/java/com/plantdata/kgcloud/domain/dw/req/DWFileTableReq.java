package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 20:05
 **/
@Data
public class DWFileTableReq {

    @ApiModelProperty("数仓id")
    private Long dataBaseId;

    @ApiModelProperty("表id")
    private Long tableId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件原始名称名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
