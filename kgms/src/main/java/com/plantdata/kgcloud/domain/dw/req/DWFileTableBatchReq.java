package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 20:05
 **/
@Data
public class DWFileTableBatchReq {

    @ApiModelProperty("数仓id")
    private Long dataBaseId;

    @ApiModelProperty("表id")
    private Long tableId;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("owner")
    private String owner;

    @ApiModelProperty("简介")
    private String description;

}
