package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by plantdata-1007 on 2019/12/2.
 */
@ApiModel("业务设置")
@Data
public class GraphConfKgqlReq {

    @NotBlank
    @ApiModelProperty("规则名称")
    private String kgqlName;

    @ApiModelProperty("规则语言")
    private String kgql;

    @ApiModelProperty("规则类型")
    private int ruleType;
}
