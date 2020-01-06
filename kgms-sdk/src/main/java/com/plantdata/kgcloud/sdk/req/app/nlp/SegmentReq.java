package com.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 18:25
 */
@Getter
@Setter
@ApiModel("分词参数")
public class SegmentReq {
    @ApiModelProperty(value = "关键字", required = true)
    @NotBlank
    private String kw;
    @ApiModelProperty("使用概念 默认true")
    private Boolean useConcept = true;
    @ApiModelProperty("使用概念 默认true")
    private Boolean useEntity = true;
    @ApiModelProperty("使用概念 默认true")
    private Boolean useAttr = true;
}
