package com.plantdata.kgcloud.domain.app.bo;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 14:36
 */
@ToString
public class GraphCommonBO {

    /**
     * 移除多余的关系
     *
     * @param graphBean
     * @param <T>
     * @return
     */
    public static <T extends BasicGraphExploreRsp> void removeNoUseRelation(T graphBean) {
        Set<Long> entityIdSet = graphBean.getEntityList().stream().map(CommonEntityRsp::getId).collect(Collectors.toSet());
        graphBean.getRelationList().removeIf(a -> !entityIdSet.contains(a.getFrom()) || !entityIdSet.contains(a.getTo()));
    }
}
