package com.plantdata.kgcloud.domain.graph.source.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-23 21:49
 **/
@Data
@ApiModel("来源添加")
public class GraphSourceReq {

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("关系id")
    private String relationId;

    @ApiModelProperty("属性id")
    private Integer attrId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型 1实体 2属性 3关系")
    private Integer type;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("真实来源")
    private String trueSource;

    @ApiModelProperty("操作者")
    private String username;

    @ApiModelProperty("动作")
    private String action;

}
