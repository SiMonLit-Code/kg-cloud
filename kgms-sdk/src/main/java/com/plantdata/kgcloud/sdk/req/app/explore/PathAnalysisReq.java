package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 11:46
 */
@Getter
@Setter
@ApiModel("路径分析-参数")
public class PathAnalysisReq extends BasicGraphExploreReq implements GraphPathReqInterface, SecondaryScreeningInterface {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @NotNull
    @Valid
    @ApiModelProperty(value = "路径", required = true)
    private CommonPathReq path;

    @Override
    public CommonPathReq fetchPath() {
        return path;
    }

}
