package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "预构建模式属性匹配")
public class PreBuilderMatchAttrReq extends BaseReq {

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

    @ApiModelProperty("状态筛选")
    private Integer matchStatus;

}
