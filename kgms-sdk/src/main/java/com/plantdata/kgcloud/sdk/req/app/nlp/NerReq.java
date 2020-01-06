package com.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author cjw 2019-11-01 13:49:25
 */
@Getter
@Setter
@ApiModel("中文命名实体识别-参数")
public class NerReq {
    @NotBlank
    @ApiModelProperty(value = "输入文本", required = true)
    private String input;
    @ApiModelProperty("中文命名实体识别配置-参数")
    private NerTagConfigReq config;

    @Getter
    @Setter
    @ApiModel("中文命名实体识别配置-参数")
    public static class NerTagConfigReq {
        private List<TagConfigReq> tagConfigList;
    }

    @Getter
    @Setter
    @ApiModel("中文命名实体识别tag配置-参数")
    public static class TagConfigReq {
        private String name;
        private List<ModelConfig> modelConfigList;
    }
}
