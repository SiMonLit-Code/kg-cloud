package com.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("属性分组查询结果模型")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphAttrGroupRsp {

    @ApiModelProperty(value = "属性分组id")
    private Long id;

    @ApiModelProperty(value = "属性分组名称")
    private String groupName;

    @ApiModelProperty(value = "属性分组包含的属性定义id")
    private List<Integer> attrIds;

    @ApiModelProperty(value = "属性分组包含的属性定义id")
    private List<AttrDefinitionRsp> attrRsp;
}
