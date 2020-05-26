package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 19:25
 */
@Data
@ApiModel("文件信息更新参数")
public class FileDataUpdateReq {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
