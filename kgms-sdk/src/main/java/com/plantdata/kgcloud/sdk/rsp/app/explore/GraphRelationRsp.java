package com.plantdata.kgcloud.sdk.rsp.app.explore;


import com.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
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
@ApiModel("graph关系视图")
@Getter
@Setter
public class GraphRelationRsp extends BasicRelationRsp implements MetaDataInterface {
    @ApiModelProperty("权重")
    private Double score = 0.0;
    @ApiModelProperty("批次")
    private String batch;
    private Double reliability;
    @ApiModelProperty("边数值属性")
    private List<BasicRelationRsp.EdgeInfo> dataValAttrs;
    @ApiModelProperty("边对象属性")
    private List<BasicRelationRsp.EdgeInfo> objAttrs;
    @ApiModelProperty("源关系")
    private List<GraphRelationRsp> sourceRelationList;

}
