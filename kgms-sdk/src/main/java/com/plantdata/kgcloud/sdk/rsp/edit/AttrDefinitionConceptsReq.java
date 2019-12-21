package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 13:49
 * @Description:
 */
@Data
@ApiModel("查询批量概念下属性定义模型")
public class AttrDefinitionConceptsReq {

    @ApiModelProperty(value = "是否继承")
    private Boolean inherit = false;
    @NonNull
    @ApiModelProperty(value = "概念ids")
    private List<Long> ids;

    @ApiModelProperty(value = "0:全部，1:数值，2:对象", allowableValues = "0,1,2")
    private Integer type;
}
