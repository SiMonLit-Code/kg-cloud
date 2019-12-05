package com.plantdata.kgcloud.domain.data.obtain.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:13
 */
@ApiModel("属性分组")
@Getter
@Setter
public class AttributeDefinitionGroupRsp {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("属性定义id")
    private List<Integer> attrDefIds;
}
