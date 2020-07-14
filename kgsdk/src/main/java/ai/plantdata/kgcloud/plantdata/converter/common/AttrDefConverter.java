package ai.plantdata.kgcloud.plantdata.converter.common;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.plantdata.bean.AttributeConstraintDefinition;
import ai.plantdata.kgcloud.plantdata.bean.AttributeDefinition;
import ai.plantdata.kgcloud.plantdata.req.data.AttributeParameter;
import ai.plantdata.kgcloud.plantdata.rsp.schema.AttributeExtraInfoItem;
import ai.plantdata.kgcloud.util.JsonUtils;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.sdk.req.app.AttrDefQueryReq;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import ai.plantdata.kgcloud.sdk.req.edit.ExtraInfoReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/30 11:27
 */
public class AttrDefConverter extends BasicConverter {

    public static AttrDefQueryReq attributeParameterToAttrDefQueryReq(@NonNull AttributeParameter param) {
        AttrDefQueryReq queryReq = new AttrDefQueryReq();
        queryReq.setConceptId(param.getConceptId());
        queryReq.setConceptKey(param.getConceptKey());
        queryReq.setInherit(param.getIsInherit());
        return queryReq;
    }

    public static AttrDefinitionReq attributeConstraintDefinitionToAttrDefinitionReq(@NonNull AttributeConstraintDefinition attrDef) {
        return attrConDefToAttrDefinitionReq(attrDef, new AttrDefinitionReq());
    }

    public static AttrDefinitionModifyReq importAttributeParameterToAttrDefinitionModifyReq(@NonNull AttributeConstraintDefinition attrDef) {
        AttrDefinitionModifyReq req = attrConDefToAttrDefinitionReq(attrDef, new AttrDefinitionModifyReq());
        req.setId(attrDef.getId());
        return req;
    }

    public static AttributeDefinition attrDefinitionRspToAttributeDefinition(AttrDefinitionRsp attrDefRsp) {
        AttributeDefinition attrDef = new AttributeDefinition();
        attrDef.setDataType(attrDefRsp.getDataType());
        attrDef.setDomain(String.valueOf(attrDefRsp.getDomainValue()));
        attrDef.setId(attrDefRsp.getId());
        attrDef.setKey(attrDefRsp.getKey());
        attrDef.setType(String.valueOf(attrDefRsp.getType()));
        attrDef.setName(attrDefRsp.getName());
        attrDef.setDataUnit(attrDefRsp.getDataUnit());
        attrDef.setRange(JacksonUtils.writeValueAsString(attrDefRsp.getRangeValue()));
        attrDef.setCreateTime(attrDefRsp.getCreateTime());
        attrDef.setAlias(attrDefRsp.getAlias());
        attrDef.setIsFunctional(attrDefRsp.getFunctional());
        // attrDef.setExtraInfoList();
        return attrDef;
    }


    private static <T extends AttrDefinitionReq> T attrConDefToAttrDefinitionReq(@NonNull AttributeConstraintDefinition attrDef, T req) {
        consumerIfNoNull(attrDef.getRange(), a -> req.setRangeValue(Lists.newArrayList(Long.valueOf(a))));
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
        req.setExtraInfoList(attributeExtraInfoItemExtraInfo(attrDef.getExtraInfoList()));
        return req;
    }

    private static List<ExtraInfoReq> attributeExtraInfoItemExtraInfo(List<AttributeExtraInfoItem> extraInfoList) {

        if(extraInfoList == null|| extraInfoList.isEmpty()){
            return Lists.newArrayList();
        }

        return extraInfoList.stream().map(e -> ConvertUtils.convert(ExtraInfoReq.class).apply(e)).collect(Collectors.toList());
    }
}
