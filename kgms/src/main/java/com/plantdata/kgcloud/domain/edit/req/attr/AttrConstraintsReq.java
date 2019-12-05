package com.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 15:28
 * @Description:
 */
@Data
@ApiModel("属性约束模型")
public class AttrConstraintsReq {

    @NotNull
    @ApiModelProperty(required = true, value = "属性id")
    private Integer attrId;

    @ApiModelProperty(value = "数值属性约束,格式{\"$gte\":\"1\",\"$lte\":\"100\",\"$in\":[]}")
    private Map<String, Object> constraints;

    @ApiModelProperty(value = "对象属性约束,0:非单值,1:单值")
    private Integer functional;
}
