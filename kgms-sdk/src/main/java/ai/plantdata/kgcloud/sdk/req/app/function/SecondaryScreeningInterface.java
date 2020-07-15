package ai.plantdata.kgcloud.sdk.req.app.function;

import ai.plantdata.kgcloud.sdk.req.app.EntityQueryFiltersReq;
import ai.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;

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

    /**
     * 二次筛选的实体和关系
     *
     * @return 二次筛选的bean
     */
    BasicGraphExploreRsp getGraphReq();

    /**
     * 边属性筛选
     *
     * @return 。
     */
    List<RelationAttrReq> getEdgeAttrFilters();

    /**
     * 反向边属性筛选
     *
     * @return 。
     */
    List<RelationAttrReq> getReservedEdgeAttrFilters();

    /**
     * 实体筛选
     *
     * @return 。
     */
    List<EntityQueryFiltersReq> getEntityFilters();

    /**
     * 需要保留的实体ids
     *
     * @return .
     */
    default Set<Long> fetchNeedSaveEntityIds() {
        return Collections.emptySet();
    }
}
