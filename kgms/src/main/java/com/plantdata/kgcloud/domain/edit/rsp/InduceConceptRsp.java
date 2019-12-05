package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 19:09
 * @Description:
 */
@Data
@ApiModel("概念规约结果模型")
public class InduceConceptRsp {

    @ApiModelProperty(value = "概念名称")
    private String conceptName;

    @ApiModelProperty(value = "父概念id")
    private Long parentId;

    @ApiModelProperty(value = "数值属性列表")
    private List<Map<String, Object>> dataAttributeValueList;

    @ApiModelProperty(value = "对象属性列表")
    private List<Map<String, Object>> objectAttributeValueList;
}
