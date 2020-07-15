package ai.plantdata.kgcloud.domain.app.dto;

import ai.plantdata.kg.api.pub.resp.EntityVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 20:51
 */
@ToString
public class InfoBoxQueryDTO {
    @Getter
    private List<Long> relationEntityIdSet;
    @Getter
    private List<Long> sourceEntityIds;

    public InfoBoxQueryDTO(List<Long> relationEntityIdSet, List<Long> sourceEntityIds) {
        this.relationEntityIdSet = relationEntityIdSet;
        this.sourceEntityIds = sourceEntityIds;

    }

    public static InfoBoxQueryDTO build(@NonNull List<EntityVO> entityList) {
        Set<Long> relationEntityIdSet = Sets.newHashSet();
        Set<Long> entityIds = Sets.newHashSet();
        entityList.forEach(a -> {
            entityIds.add(a.getId());
            if (!CollectionUtils.isEmpty(a.getObjectAttributes())) {
                a.getObjectAttributes().forEach((k, v) -> {
                    relationEntityIdSet.addAll(v);
                });
            }
            if (!CollectionUtils.isEmpty(a.getReverseObjectAttributes())) {
                a.getReverseObjectAttributes().forEach((k, v) -> {
                    relationEntityIdSet.addAll(v);
                });
            }
        });
        relationEntityIdSet.addAll(entityIds);
        return new InfoBoxQueryDTO(Lists.newArrayList(relationEntityIdSet), Lists.newArrayList(entityIds));
    }
}
