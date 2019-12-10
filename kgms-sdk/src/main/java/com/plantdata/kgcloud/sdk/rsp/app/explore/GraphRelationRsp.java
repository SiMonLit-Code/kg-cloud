package com.plantdata.kgcloud.sdk.rsp.app.explore;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 9:53
 */
@ToString
@ApiModel("关系视图")
@Getter
@Setter
public class GraphRelationRsp extends BasicRelationRsp {
    @ApiModelProperty("边数值属性")
    private List<BasicRelationRsp.EdgeInfo> dataValAttrs;
    @ApiModelProperty("边对象属性")
    private List<BasicRelationRsp.EdgeInfo> objAttrs;
    @ApiModelProperty("源关系")
    private List<BasicRelationRsp> sourceRelationList;

}
