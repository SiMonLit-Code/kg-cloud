package com.plantdata.kgcloud.domain.app.bo;

import com.google.common.collect.Sets;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

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
    public static <T extends BasicGraphExploreRsp> void rebuildGraphRelationAndEntity(T graphBean, Set<Long> searchIds) {
        Set<Long> entityIdSet = graphBean.getEntityList().stream().map(CommonEntityRsp::getId).collect(Collectors.toSet());
        Set<Long> needSaveEntityIdSet = Sets.newHashSet();
        graphBean.getRelationList().removeIf(a -> {
            boolean needHave = entityIdSet.contains(a.getFrom()) && entityIdSet.contains(a.getTo());
            if (!needHave) {
                return true;
            }
            needSaveEntityIdSet.add(a.getFrom());
            needSaveEntityIdSet.add(a.getTo());
            return false;
        });
        if (CollectionUtils.isEmpty(graphBean.getEntityList()) || CollectionUtils.isEmpty(searchIds)) {
            return;
        }
        graphBean.getEntityList().removeIf(a -> !needSaveEntityIdSet.contains(a.getId()) && !searchIds.contains(a.getId()));
    }
}
