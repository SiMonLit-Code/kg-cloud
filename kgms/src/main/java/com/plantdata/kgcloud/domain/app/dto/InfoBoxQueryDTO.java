package com.plantdata.kgcloud.domain.app.dto;

import ai.plantdata.kg.api.pub.resp.EntityVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 20:51
 */
@AllArgsConstructor
@ToString
public class InfoBoxQueryDTO {
    @Getter
    private List<Long> relationEntityIdSet;
    @Getter
    private List<Integer> attrDefIdSet;

    public static InfoBoxQueryDTO build(@NonNull List<EntityVO> entityList) {
        Set<Long> relationEntityIdSet = Sets.newHashSet();
        Set<Integer> attrDefIdSet = Sets.newHashSet();
        entityList.forEach(a -> {
            if (!CollectionUtils.isEmpty(a.getObjectAttributes())) {
                a.getObjectAttributes().forEach((k, v) -> {
                    attrDefIdSet.add(Integer.parseInt(k));
                    relationEntityIdSet.addAll(v);
                });
            }
            if (!CollectionUtils.isEmpty(a.getReverseObjectAttributes())) {
                a.getReverseObjectAttributes().forEach((k, v) -> {
                    attrDefIdSet.add(Integer.parseInt(k));
                    relationEntityIdSet.addAll(v);
                });
            }
            if (!CollectionUtils.isEmpty(a.getDataAttributes())) {
                attrDefIdSet.addAll(a.getDataAttributes().keySet().stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            if (!CollectionUtils.isEmpty(a.getPrivateDataAttributes())) {
                attrDefIdSet.addAll(a.getPrivateDataAttributes().keySet().stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
        });
        return new InfoBoxQueryDTO(Lists.newArrayList(relationEntityIdSet), Lists.newArrayList(attrDefIdSet));
    }
}
