package com.plantdata.kgcloud.sdk.rsp.app.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw 2019-11-04 10:44:41
 */
@Getter
@Setter
@ApiModel("样式和附加字段")
public class AdditionalRsp {
    @ApiModelProperty("节点样式")
    private Map<String, Object> nodeStyle;
    @ApiModelProperty("label样式")
    private Map<String, Object> labelStyle;
    @ApiModelProperty("链接")
    private Map<String, Object> linkStyle;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("私有属性")
    private Map<String, Object> extra;
}
