package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@ApiModel("数值属性筛选-参数")
public class DataAttrReq extends CompareFilterReq {
    @ApiModelProperty(value = "属性定义id",required = true)
    @NotNull
    private Integer attrDefId;
}
