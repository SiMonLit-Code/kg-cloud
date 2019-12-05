package com.plantdata.kgcloud.domain.edit.req.attr;

import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 10:41
 * @Description:
 */
@Data
@ApiModel("属性定义修改模型")
public class AttrDefinitionModifyReq extends AttrDefinitionReq {

    @ApiModelProperty(value = "属性id")
    @NotNull(message = "属性id不能为空")
    private Integer id;

}
