package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@ApiModel("词典创建/修改")
@Data
public class DictionaryReq {

    @ApiModelProperty(value = "词典名称", required = true)
    @NotNull
    private String title;

    @ApiModelProperty(value = "词典描述")
    private String remark;

}

