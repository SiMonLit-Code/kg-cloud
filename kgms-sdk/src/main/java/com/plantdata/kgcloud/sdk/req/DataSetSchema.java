package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 14:02
 **/
@Data
@ApiModel
public class DataSetSchema {

    @NotBlank
    @ApiModelProperty
    private String field;

    @NotNull
    @ApiModelProperty
    private Integer type;

    @ApiModelProperty
    private int isIndex;
    @ApiModelProperty
    private Map settings;
}
