package com.plantdata.kgcloud.sdk.req.edit;

import com.plantdata.kgcloud.sdk.validator.KeyCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 10:24
 * @Description:
 */
@Data
@ApiModel("属性定义添加模型")
public class AttrDefinitionReq {

    @NotEmpty
    @ApiModelProperty(required = true,value = "属性名称")
    @Length(max = 50, message = "属性名称长度不能超过50")
    private String name;

    @ApiModelProperty(value = "属性名称别名")
    @Length(max = 100, message = "属性别名长度不能超过100")
    private String alias;

    @ApiModelProperty(required = true,value = "0：数值，1：对象", allowableValues = "0,1")
    @NotNull
    private Integer type;
    @NotNull
    @ApiModelProperty(required = true,value = "属性定义域")
    private Long domainValue;

    @ApiModelProperty(value = "属性值域")
    private List<Long> rangeValue;

    @NotNull
    @ApiModelProperty(required = true,value = "属性类型")
    private Integer dataType;

    @ApiModelProperty(value = "属性单位")
    @Length(max = 10, message = "属性单位长度不能超过10")
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
    @KeyCheck
    @Length(max = 50, message = "长度不能超过50")
    private String key;

}
