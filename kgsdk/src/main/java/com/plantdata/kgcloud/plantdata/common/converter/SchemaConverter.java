package com.plantdata.kgcloud.plantdata.common.converter;

import com.plantdata.kgcloud.plantdata.rsp.schema.Additional;
import com.plantdata.kgcloud.plantdata.rsp.schema.AttBean;
import com.plantdata.kgcloud.plantdata.rsp.schema.AttrCategoryOutputBean;
import com.plantdata.kgcloud.plantdata.rsp.schema.AttributeExtraInfoItem;
import com.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import com.plantdata.kgcloud.plantdata.rsp.schema.TypeBean;
import com.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionGroupRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 10:55
 */
public class SchemaConverter extends ConverterUtils {


    public static SchemaBean schemaRspToSchemaBean(SchemaRsp rsp) {
        SchemaBean schemaBean = new SchemaBean();
        //属性分组
        List<AttrCategoryOutputBean> attrDefGroupList = listToRsp(rsp.getAttrGroups(), SchemaConverter::attrDefGroupRspToAttrCategoryOutputBean);
        //属性定义
        List<AttBean> attBeanList = listToRsp(rsp.getAttrs(), SchemaConverter::attrDefRspToAttBean);
        List<TypeBean> typeBeanList = listToRsp(rsp.getTypes(), SchemaConverter::baseConceptRspToTypeBean);
        schemaBean.setAttGroup(attrDefGroupList);
        schemaBean.setAtts(attBeanList);
        schemaBean.setKgTitle(rsp.getKgTitle());
        schemaBean.setTypes(typeBeanList);
        return schemaBean;
    }


    private static AttrCategoryOutputBean attrDefGroupRspToAttrCategoryOutputBean(@NonNull AttributeDefinitionGroupRsp groupRsp) {
        AttrCategoryOutputBean temp = new AttrCategoryOutputBean();
        temp.setAttrDefIds(groupRsp.getAttrDefIds());
        temp.setId(groupRsp.getId().intValue());
        temp.setName(groupRsp.getName());
        return temp;
    }

    private static AttBean attrDefRspToAttBean(@NonNull AttributeDefinitionRsp attrDefRsp) {
        AttBean attBean = new AttBean();
        attBean.setAdditionalInfo(attrDefRsp.getAdditionalInfo());
        attBean.setDataType(attrDefRsp.getDataType());
        attBean.setDirection(attrDefRsp.getDirection());
        attBean.setDomain(attrDefRsp.getDomainValue());
        List<AttributeExtraInfoItem> extraInfoItemList = listToRsp(attrDefRsp.getExtraInfos(), SchemaConverter::attrExtraItemToAttributeExtraInfoItem);
        attBean.setExtraInfos(extraInfoItemList);
        attBean.setRange(attrDefRsp.getRangeValue());
        attBean.setK(Long.valueOf(attrDefRsp.getId()));
        attBean.setV(attrDefRsp.getName());
        attBean.setType(Long.valueOf(attrDefRsp.getType()));
        attBean.setAttrKey(attrDefRsp.getKey());
        return attBean;
    }

    private static TypeBean baseConceptRspToTypeBean(@NonNull BaseConceptRsp conceptRsp) {
        TypeBean typeBean = new TypeBean();
        typeBean.setK(conceptRsp.getId());
        typeBean.setV(conceptRsp.getName());
        typeBean.setImg(conceptRsp.getImg());
        typeBean.setAdditionalInfo(executeIfNoNull(conceptRsp.getAdditionalInfo(), a -> additionalRspToAdditional(a, conceptRsp.getOpenGis())));
        typeBean.setParentId(conceptRsp.getParentId());
        return typeBean;
    }

    private static AttributeExtraInfoItem attrExtraItemToAttributeExtraInfoItem(@NonNull AttrExtraRsp attrExtraRsp) {
        AttributeExtraInfoItem extraInfoItem = new AttributeExtraInfoItem();
        extraInfoItem.setDataType(attrExtraRsp.getDataType());
        extraInfoItem.setDataUnit(attrExtraRsp.getDataUnit());
        extraInfoItem.setIndexed(attrExtraRsp.getIndexed());
        extraInfoItem.setName(attrExtraRsp.getName());
        extraInfoItem.setObjRange(JacksonUtils.writeValueAsString(attrExtraRsp.getObjRange()));
        extraInfoItem.setSeqNo(attrExtraRsp.getSeqNo());
        return extraInfoItem;
    }

    private static Additional additionalRspToAdditional(@NonNull AdditionalRsp additionalRsp, Boolean openGis) {
        Additional additional = new Additional();
        additional.setColor(additionalRsp.getColor());
        additional.setExtra(additionalRsp.getExtra());
        additional.setIsOpenGis(openGis);
        additional.setLabelStyle(additionalRsp.getLabelStyle());
        additional.setLinkStyle(additionalRsp.getLinkStyle());
        additional.setNodeStyle(additionalRsp.getNodeStyle());
        return additional;
    }
}
