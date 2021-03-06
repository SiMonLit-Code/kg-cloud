package ai.plantdata.kgcloud.plantdata.converter.common;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.rsp.schema.Additional;
import ai.plantdata.kgcloud.plantdata.rsp.schema.AttBean;
import ai.plantdata.kgcloud.plantdata.rsp.schema.AttrCategoryOutputBean;
import ai.plantdata.kgcloud.plantdata.rsp.schema.AttributeExtraInfoItem;
import ai.plantdata.kgcloud.plantdata.rsp.schema.SchemaBean;
import ai.plantdata.kgcloud.plantdata.rsp.schema.TypeBean;
import ai.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionGroupRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 10:55
 */
public class SchemaBasicConverter extends BasicConverter {


    public static SchemaBean schemaRspToSchemaBean(SchemaRsp rsp) {
        SchemaBean schemaBean = new SchemaBean();
        //属性分组
        List<AttrCategoryOutputBean> attrDefGroupList = toListNoNull(rsp.getAttrGroups(), SchemaBasicConverter::attrDefGroupRspToAttrCategoryOutputBean);
        //属性定义
        List<AttBean> attBeanList = toListNoNull(rsp.getAttrs(), SchemaBasicConverter::attrDefRspToAttBean);
        List<TypeBean> typeBeanList = toListNoNull(rsp.getTypes(), SchemaBasicConverter::baseConceptRspToTypeBean);
        schemaBean.setAttGroup(attrDefGroupList);
        schemaBean.setAtts(attBeanList);
        schemaBean.setKgTitle(rsp.getKgTitle());
        schemaBean.setTypes(typeBeanList);
        return schemaBean;
    }


    private static AttrCategoryOutputBean attrDefGroupRspToAttrCategoryOutputBean(@NonNull AttributeDefinitionGroupRsp groupRsp) {
        AttrCategoryOutputBean temp = new AttrCategoryOutputBean();
        temp.setAttrDefIds(groupRsp.getAttrDefIds());
        temp.setId(groupRsp.getId());
        temp.setName(groupRsp.getName());
        return temp;
    }

    private static AttBean attrDefRspToAttBean(@NonNull AttributeDefinitionRsp attrDefRsp) {
        AttBean attBean = new AttBean();
        attBean.setAdditionalInfo(attrDefRsp.getAdditionalInfo());
        attBean.setDataType(attrDefRsp.getDataType());
        attBean.setDirection(attrDefRsp.getDirection());
        attBean.setDomain(attrDefRsp.getDomainValue());
        List<AttributeExtraInfoItem> extraInfoItemList = toListNoNull(attrDefRsp.getExtraInfos(), SchemaBasicConverter::attrExtraItemToAttributeExtraInfoItem);
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
        consumerIfNoNull(attrExtraRsp.getIndexed(), extraInfoItem::setIndexed);
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
