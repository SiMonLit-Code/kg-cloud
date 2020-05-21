package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 19:20
 */
@Data
@ApiModel("批量文件上传参数")
public class FileDataBatchReq {

    @ApiModelProperty("数据库id")
    private Long databaseId;

    @ApiModelProperty("数据表id")
    private Long tableId;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
