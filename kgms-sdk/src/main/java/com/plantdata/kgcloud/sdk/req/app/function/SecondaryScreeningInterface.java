package com.plantdata.kgcloud.sdk.req.app.function;

import com.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 二次筛选功能
 *
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 20:35
 */
public interface SecondaryScreeningInterface {

    BasicGraphExploreRsp  getGraphReq();

    List<RelationAttrReq> getEdgeAttrFilters();

    List<RelationAttrReq> getReservedEdgeAttrFilters();

    List<EntityQueryFiltersReq> getEntityFilters();

    /**
     * 需要保留的实体ids
     *
     * @retur ..
     */
    default Set<Long> getNeedSaveEntityIds() {
        return Collections.emptySet();
    }
}
