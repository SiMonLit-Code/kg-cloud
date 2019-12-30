package com.plantdata.kgcloud.plantdata.converter.common;

import com.plantdata.kgcloud.plantdata.bean.AttributeConstraintDefinition;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/30 11:27
 */
public class AttrDefConverter extends BasicConverter {

    public static AttrDefinitionReq attributeConstraintDefinitionToAttrDefinitionReq(@NonNull AttributeConstraintDefinition attrDef) {
        return attrConDefToAttrDefinitionReq(attrDef, new AttrDefinitionReq());
    }

    public static AttrDefinitionModifyReq importAttributeParameterToAttrDefinitionModifyReq(AttributeConstraintDefinition attrDef) {
        AttrDefinitionModifyReq req = attrConDefToAttrDefinitionReq(attrDef, new AttrDefinitionModifyReq());
        req.setId(attrDef.getId());
        return req;
    }

    private static <T extends AttrDefinitionReq> T attrConDefToAttrDefinitionReq(@NonNull AttributeConstraintDefinition attrDef, T req) {
        consumerIfNoNull(attrDef.getRange(), a -> req.setRangeValue(JsonUtils.jsonToList(a, Long.class)));
        consumerIfNoNull(attrDef.getAdditionalInfo(), a -> req.setAdditionalInfo(JsonUtils.stringToMap(a)));
        consumerIfNoNull(attrDef.getType(), a -> req.setType(Integer.parseInt(a)));
        consumerIfNoNull(attrDef.getDomain(), a -> req.setDomainValue(Long.valueOf(a)));
        req.setAlias(attrDef.getAlias());
        req.setConstraints(attrDef.getConstraints());
        req.setName(attrDef.getName());
        req.setKey(attrDef.getKey());
        req.setFunctional(attrDef.getIsFunctional());
        req.setDataType(attrDef.getDataType());
        req.setDirection(NumberUtils.INTEGER_ZERO);
        req.setDataUnit(attrDef.getDataUnit());
        return req;
    }
}
