package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/26 10:16
 */
@Getter
@Setter
public class GraphEntityRsp extends BasicEntityRsp  {


    @ApiModelProperty("顶层概念")
    private ExploreConceptRsp topConcept;
    @ApiModelProperty("主概念")
    private ExploreConceptRsp mainConcept;
    @ApiModelProperty("所有概念id集合，包含顶层父概念")
    private List<Long> conceptIdList;
    @ApiModelProperty("坐标信息")
    private CoordinateReq coordinates;
}
