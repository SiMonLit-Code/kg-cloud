package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 11:46
 */
@Getter
@Setter
@ApiModel("路径分析参数")
public class PathAnalysisReq extends BasicGraphExploreReq implements GraphPathReqInterface {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @ApiModelProperty("路径")
    private CommonPathReq path;

    @Override
    public CommonPathReq fetchPath() {
        return path;
    }
}
