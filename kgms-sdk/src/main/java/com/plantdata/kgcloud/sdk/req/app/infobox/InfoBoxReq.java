package com.plantdata.kgcloud.sdk.req.app.infobox;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 9:56
 */
@Getter
@Setter
@ApiModel("知识卡片-参数(单个)")
public class InfoBoxReq {

    @NotNull
    @ApiModelProperty(value = "实体id", required = true)
    private Long id;
    @ApiModelProperty("是否读取关系属性 默认 false")
    private Boolean relationAttrs = false;
    @ApiModelProperty("允许的属性id")
    private List<Integer> allowAttrs;
    @ApiModelProperty("允许的属性key")
    private List<String> allowAttrsKey;
}
