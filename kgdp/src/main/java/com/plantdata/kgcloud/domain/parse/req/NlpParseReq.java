package com.plantdata.kgcloud.domain.parse.req;

import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class NlpParseReq {

    @ApiModelProperty(value = "nlp配置")
    private List<NlpRsp> nlpConfigs;

    @ApiModelProperty(value = "输入文本")
    private List<String> inputs;
}
