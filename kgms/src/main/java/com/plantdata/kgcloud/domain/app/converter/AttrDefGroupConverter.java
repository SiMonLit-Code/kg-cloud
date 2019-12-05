package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionGroupRsp;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:56
 */
public class AttrDefGroupConverter {

    public static List<AttributeDefinitionGroupRsp> dtoToRsp(@NonNull List<AttrDefGroupDTO> attrDefGroupList) {
        return attrDefGroupList.stream().
                map(a -> new AttributeDefinitionGroupRsp(a.getGroupId(), a.getGroupName(), a.getAttrDefId())).collect(Collectors.toList());
    }
}
