package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:38
 **/
@Data
@ApiModel
public class DataSetCreateReq {

    @ApiModelProperty("文件夹id")
    private Long folderId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("唯一标识")
    private String key;

    @ApiModelProperty("数据集类型")
    private Integer dataType;

    @ApiModelProperty("创建类型")
    private Integer createType;

    @ApiModelProperty("创建方式")
    private String createWay;

    @Valid
    @ApiModelProperty
    private List<DataSetSchema> schema;
}
