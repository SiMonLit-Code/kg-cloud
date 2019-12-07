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
public class DataSetReq {
    private Long folderId;

    private String title;
    @ApiModelProperty
    private Integer dataType;
    @ApiModelProperty
    private Integer createType;
    @ApiModelProperty
    private String createWay;
    @Valid
    @ApiModelProperty
    private List<DataSetSchema> cols;
}
