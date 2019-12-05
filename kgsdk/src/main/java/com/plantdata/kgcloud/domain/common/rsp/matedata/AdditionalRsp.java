package com.plantdata.kgcloud.domain.common.rsp.matedata;

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
    @ApiModelProperty("")
    private Map<String, Object> nodeStyle;
    @ApiModelProperty("")
    private Map<String, Object> labelStyle;
    @ApiModelProperty("")
    private Map<String, Object> linkStyle;
    @ApiModelProperty("")
    private String color;
    @ApiModelProperty("私有属性")
    private Map<String, Object> extra;
}
