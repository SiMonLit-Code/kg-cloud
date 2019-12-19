package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionGroupRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 15:56
 */
public class AttrDefGroupConverter {

    public static AttributeDefinitionGroupRsp dtoToRsp(@NonNull AttrDefGroupDTO attrDefGroup) {
        return new AttributeDefinitionGroupRsp(attrDefGroup.getGroupId(), attrDefGroup.getGroupName(), attrDefGroup.getAttrDefId());
    }
}
