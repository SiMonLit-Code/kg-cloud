package com.plantdata.kgcloud.domain.prebuilder.req;

import com.plantdata.kgcloud.domain.prebuilder.aop.DefaultHandlerReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PreBuilderGraphMapReq extends DefaultHandlerReq {

    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("映射配置")
    private List<SchemaQuoteReq> quoteConfigs;
}
