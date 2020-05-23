package com.plantdata.kgcloud.domain.graph.source.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-23 21:53
 **/
@Data
public class GraphQueryReq {

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("关系id")
    private String relationId;

    @ApiModelProperty("属性id")
    private Integer attrId;

    @ApiModelProperty("类型 1实体 2属性 3关系")
    private Integer type;
}
