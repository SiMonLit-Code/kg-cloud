package com.plantdata.kgcloud.domain.scene.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelAnalysisRsp {

    @ApiModelProperty(value = "唯一id")
    private Long id;

    @ApiModelProperty(value = "模式识别接口地址")
    private String url;
}
