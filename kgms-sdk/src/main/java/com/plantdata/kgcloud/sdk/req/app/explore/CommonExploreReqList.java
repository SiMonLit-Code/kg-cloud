package com.plantdata.kgcloud.sdk.req.app.explore;

import com.google.common.collect.Sets;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 10:14
 */
@ApiModel("普通图探索-参数")
@Getter
@Setter
public class CommonExploreReqList extends BasicGraphExploreReqList implements GraphCommonReqInterface, SecondaryScreeningInterface {

    @NotNull
    @Valid
    @ApiModelProperty(value = "通用参数", required = true)
    private CommonFiltersReq common;

    @Override
    public CommonFiltersReq fetchCommon() {
        return common;
    }

    @Override
    public Set<Long> fetchNeedSaveEntityIds() {
        return common.getId() == null ? Collections.emptySet() : Sets.newHashSet(common.getId());
    }
}
