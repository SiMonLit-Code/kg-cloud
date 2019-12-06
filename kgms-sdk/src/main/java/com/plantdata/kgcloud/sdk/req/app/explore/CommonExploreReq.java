package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphCommonReqInterface;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 10:14
 */
@ApiModel("普通图探索参数")
@Getter
@Setter
public class CommonExploreReq extends BasicGraphExploreReq implements GraphCommonReqInterface {

    private CommonFiltersReq common;

    @Override
    public CommonFiltersReq fetchCommon() {
        return common;
    }
}
