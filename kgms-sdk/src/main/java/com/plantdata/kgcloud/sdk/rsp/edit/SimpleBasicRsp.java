package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/1/4 13:57
 * @Description:
 */
@Setter
@Getter
@ApiModel("基本实体信息")
public class SimpleBasicRsp extends BasicInfoVO {

    @ApiModelProperty(value = "概念名称")
    private String conceptName;
}
