package ai.plantdata.kgcloud.sdk.req.app.function;

import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 13:49
 */
public interface GraphRelationReqInterface {
    /**
     * 关联分析
     *
     * @return 。
     */
    CommonRelationReq fetchRelation();
}
