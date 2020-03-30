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
public class DWFileTableUpdateReq {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
