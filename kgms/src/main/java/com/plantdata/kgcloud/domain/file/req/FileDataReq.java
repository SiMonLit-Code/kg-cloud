package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 17:48
 */
@Data
@ApiModel("单个文件上传参数")
public class FileDataReq {

    @ApiModelProperty("数据库id")
    private Long databaseId;

    @ApiModelProperty("数据表id")
    private Long tableId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件原始名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String path;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("关系图谱列表")
    private List<String> kgNames;

}
