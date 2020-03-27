package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PreBuilderMatchAttrReq {

    @ApiModelProperty("当前配置的图谱名")
    private String kgName;

    @ApiModelProperty("要引入的模式id")
    private Integer modelId;

    @ApiModelProperty("已经引入的概念id集合")
    private List<Integer> conceptIds;

    @ApiModelProperty("要查看属性的概念id")
    private List<Integer> findAttrConceptIds;

    @ApiModelProperty("引用的概念属性映射")
    private List<SchemaQuoteReq> schemaQuoteReqList;

}
