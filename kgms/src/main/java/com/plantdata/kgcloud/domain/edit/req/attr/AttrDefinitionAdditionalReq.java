package com.plantdata.kgcloud.domain.edit.req.attr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 16:34
 * @Description:
 */
@Data
@ApiModel(value = "修改(对象)属性定义业务信息模型")
public class AttrDefinitionAdditionalReq {

    @ApiModelProperty(value = "属性id")
    private Integer id;

    @ApiModelProperty(value = "业务配置信息")
    private Map<String, Object> additional;

    @JsonIgnore
    private String additionalInfo;
}
