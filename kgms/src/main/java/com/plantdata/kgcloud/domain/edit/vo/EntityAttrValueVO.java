package com.plantdata.kgcloud.domain.edit.vo;

import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 18:05
 * @Description:
 */
@Data
@ApiModel("属性值查询结果模型")
public class EntityAttrValueVO extends AttrDefinitionVO {

    @ApiModelProperty(value = "数值属性值")
    private Object dataValue;

    @ApiModelProperty(value = "数值属性TripleId")
    private String dataValueTripleId;

    @ApiModelProperty(value = "对象属性值")
    private List<ObjectAttrValueVO> objectValues;
}
