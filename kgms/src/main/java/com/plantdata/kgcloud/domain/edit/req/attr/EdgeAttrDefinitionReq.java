package com.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 14:06
 * @Description:
 */
@Data
@ApiModel("边属性定义创建模型")
public class EdgeAttrDefinitionReq {
    @ApiModelProperty(value = "边属性id")
    private Integer seqNo;

    @ApiModelProperty(value = "边属性名称")
    private String name;

    @ApiModelProperty(value = "边属性类型")
    private Integer dataType;

    @ApiModelProperty(value = "边属性类型,0:数值属性,1:对象属性")
    private Integer type;

    @ApiModelProperty(value = "边属性单位")
    private String dataUnit;

    @ApiModelProperty(value = "边属性值域")
    private List<Long> objRange;

    @ApiModelProperty(value = "是否创建索引,0-N,1-Y")
    private Integer indexed = 0;
}
