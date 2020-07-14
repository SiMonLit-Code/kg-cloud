package ai.plantdata.kgcloud.domain.prebuilder.req;

import ai.plantdata.kgcloud.sdk.req.AttributesMapReq;
import ai.plantdata.kgcloud.sdk.req.RelationsMapReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataMapReq {

    @ApiModelProperty("引入的模式id")
    private Integer modelId;

    @ApiModelProperty("引入的模式中概念名称")
    private String entityName;

    @ApiModelProperty("映射的图谱概念名称")
    private String conceptName;

    @ApiModelProperty("映射的图谱概念id")
    private Long conceptId;

    @ApiModelProperty("属性映射")
    private List<AttributesMapReq> attributes;

    @ApiModelProperty("关系映射")
    private List<RelationsMapReq> relations;
}
