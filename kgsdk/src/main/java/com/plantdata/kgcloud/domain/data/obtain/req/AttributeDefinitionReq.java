package com.plantdata.kgcloud.domain.data.obtain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:05
 */
@ApiModel("属性定义")
@Getter
@Setter
public class AttributeDefinitionReq {

    @ApiModelProperty("属性定义id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("属性定义类型")
    private Long type;
    @ApiModelProperty("属性值域")
    private List<Long> range;
    @ApiModelProperty("所属概念id")
    private Long domain;
    @ApiModelProperty("属性值的类型")
    private Integer dataType;
    ///  private List<AttributeExtraInfoItem> extraInfos;
    @ApiModelProperty("方向")
    private Integer direction;
    @ApiModelProperty("属性值的附加信息")
    private Map<String, Object> additionalInfo;
}
