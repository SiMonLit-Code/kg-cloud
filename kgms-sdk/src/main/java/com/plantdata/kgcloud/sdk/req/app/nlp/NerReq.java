package com.plantdata.kgcloud.sdk.req.app.nlp;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw 2019-11-01 13:49:25
 */
@Getter
@Setter
@ApiModel("中文命名实体识别参数")
public class NerReq {

    private String input;
    private NerTagConfigReq config;

    @Getter
    @Setter
    @ApiModel("中文命名实体识别配置参数")
    public static class NerTagConfigReq {
        private List<TagConfigReq> tagConfigList;
    }

    @Getter
    @Setter
    @ApiModel("中文命名实体识别tag配置参数")
    public static class TagConfigReq {
        private String name;
        private List<ModelConfig> modelConfigList;
    }
}
