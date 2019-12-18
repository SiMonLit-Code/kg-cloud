package com.plantdata.kgcloud.domain.edit.req.entity;

import ai.plantdata.kg.api.edit.validator.TypeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 14:46
 * @Description:
 */
@Data
@ApiModel("删除私有数值或对象属性值模型")
public class DeletePrivateDataReq {


    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "0：数值，1：对象", allowableValues = "0,1")
    @NotNull
    @TypeRange
    private Integer type;

    @ApiModelProperty(value = "数值或对象属性值的id")
    private List<String> tripleIds;
}
