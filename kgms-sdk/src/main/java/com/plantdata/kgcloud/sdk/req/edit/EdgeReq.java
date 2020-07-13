package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("边属性")
public class EdgeReq {
    private Integer seqNo;
    @NotEmpty
    private String name;
    @NotNull
    private Integer dataType;
    @ApiModelProperty(
            value = "0：数值，1：对象",
            allowableValues = "0,1"
    )
    @NotNull
    private Integer type;
    private String dataUnit;
    @ApiModelProperty(
            value = "0：不索引，1：索引",
            allowableValues = "0,1"
    )
    @NotNull
    private Integer indexed;
    private List<Long> objRange;
}