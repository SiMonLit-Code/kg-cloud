package com.plantdata.kgcloud.domain.graph.attr.dto;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroup;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:47
 */
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AttrDefGroupDTO {

    /**
     * 属性分组id
     */
    private Long groupId;
    /**
     * 属性分类名称
     */
    private String groupName;
    /**
     * 属性定义id
     */
    private List<Integer> attrDefId;

    public static List<AttrDefGroupDTO> listOfAttrGroupAndDetail(@NonNull List<GraphAttrGroup> groupList, List<GraphAttrGroupDetails> detailsList) {
        List<AttrDefGroupDTO> attrDefGroupList = Lists.newArrayListWithCapacity(groupList.size());
        Map<Long, List<GraphAttrGroupDetails>> detailMap = CollectionUtils.isEmpty(detailsList) ?
                Collections.emptyMap() : detailsList.stream().collect(Collectors.groupingBy(GraphAttrGroupDetails::getGroupId));
        AttrDefGroupDTO temp;
        for (GraphAttrGroup group : groupList) {
            temp = new AttrDefGroupDTO();
            temp.setGroupId(group.getId());
            temp.setGroupName(group.getGroupName());
            List<GraphAttrGroupDetails> details = detailMap.get(group.getId());
            if (!CollectionUtils.isEmpty(details)) {
                temp.setAttrDefId(details.stream().map(GraphAttrGroupDetails::getAttrId).collect(Collectors.toList()));
            }
            attrDefGroupList.add(temp);
        }
        return attrDefGroupList;
    }
}
