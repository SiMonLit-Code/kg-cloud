package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 19:20
 */
@Data
public class FileDataBatchReq {

    @ApiModelProperty("数仓id")
    private Long databaseId;

    @ApiModelProperty("表id")
    private Long tableId;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
