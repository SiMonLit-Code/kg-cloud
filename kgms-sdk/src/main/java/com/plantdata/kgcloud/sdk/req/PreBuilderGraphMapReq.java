package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PreBuilderGraphMapReq {

    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("映射配置")
    private List<SchemaQuoteReq> quoteConfigs;
}
