package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 10:41
 * @Description:
 */
@Data
@ApiModel("属性定义修改模型")
public class AttrDefinitionModifyReq {

    @ApiModelProperty(value = "属性id")
    @NotNull(message = "属性id不能为空")
    private Integer id;


    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性名称别名")
    private String alias;

    @ApiModelProperty(value = "0：数值，1：对象", allowableValues = "0,1")
    @Min(value = 0)
    @Max(value = 1)
    private Integer type;


    @ApiModelProperty(value = "属性定义域")
    private Long domainValue;

    @ApiModelProperty(value = "属性值域")
    private List<Long> rangeValue;


    @ApiModelProperty(value = "属性类型")
    private Integer dataType;

    @ApiModelProperty(value = "属性单位")
    private String dataUnit;

    @ApiModelProperty(value = "对象属性是否唯一,0:N,1:Y")
    private Integer functional = 0;

    @ApiModelProperty(value = "对象属性方向,0:正向,1:无向")
    private Integer direction = 0;

    @ApiModelProperty(value = "前端使用")
    private Map<String, Object> additionalInfo;

    @ApiModelProperty(value = "属性约束")
    private Map<String, Object> constraints;

    @ApiModelProperty(value = "属性定义key")
    private String key;
}
