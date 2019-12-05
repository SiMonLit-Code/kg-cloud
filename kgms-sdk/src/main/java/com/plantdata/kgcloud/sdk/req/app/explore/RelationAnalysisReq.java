package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 15:46
 */
@Getter
@Setter
@ApiModel("关联分析参数")
public class RelationAnalysisReq extends BasicGraphExploreReq {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @ApiModelProperty("关联搜索参数")
    private CommonRelationReq relation;
}
