package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemaQuoteReq {

    @ApiModelProperty("引入的模式id")
    private Integer modelId;

    @ApiModelProperty("引入原模式概念id")
    private Integer modelConceptId;

    @ApiModelProperty("引入的模式中概念名称")
    private String entityName;

    @ApiModelProperty("映射的图谱概念名称")
    private String conceptName;

    @ApiModelProperty("映射的图谱父概念名称")
    private String pConceptName;

    @ApiModelProperty("映射的图谱概念id")
    private Long conceptId;

    @ApiModelProperty("映射的图谱父概念id")
    private Long pConceptId;

    @ApiModelProperty("映射的表名")
    private List<String> tables;

    @ApiModelProperty("引入的属性")
    private List<SchemaQuoteAttrReq> attrs;
}
