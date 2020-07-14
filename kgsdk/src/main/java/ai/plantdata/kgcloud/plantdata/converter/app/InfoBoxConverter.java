package ai.plantdata.kgcloud.plantdata.converter.app;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.bean.EntityLink;
import ai.plantdata.kgcloud.plantdata.req.app.InfoBoxMultiModalParameter;
import ai.plantdata.kgcloud.plantdata.req.app.InfoBoxMultiModalParameterMore;
import ai.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import ai.plantdata.kgcloud.plantdata.req.app.InfoBoxParameterMore;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityMultiModalBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.EntityLinksBean;
import ai.plantdata.kgcloud.sdk.rsp.app.main.*;
import ai.plantdata.kgcloud.util.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.req.common.DataLinks;
import ai.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import ai.plantdata.kgcloud.plantdata.req.common.Links;
import ai.plantdata.kgcloud.plantdata.req.common.Tag;
import ai.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoboxMultiModalReq;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/16 17:03
 */
public class InfoBoxConverter extends BasicConverter {

    public static InfoBoxReq infoBoxParameterToInfoBoxReq(InfoBoxParameter infoBoxParam) {
        InfoBoxReq infoBoxReq = new InfoBoxReq();
        infoBoxReq.setAllowAttrs(infoBoxParam.getAllowAtts());
        infoBoxReq.setId(infoBoxParam.getId());
        infoBoxReq.setKw(infoBoxParam.getKw());
        infoBoxReq.setRelationAttrs(infoBoxParam.isRelationAtts());
        infoBoxReq.setAllowAttrsKey(infoBoxParam.getAllowAttsKey());
        return infoBoxReq;
    }

    public static InfoboxMultiModalReq infoBoxMultiModalParameterToInfoBoxReq(InfoBoxMultiModalParameter infoBoxParam) {
        InfoboxMultiModalReq infoBoxReq = new InfoboxMultiModalReq();
        infoBoxReq.setId(infoBoxParam.getId());
        infoBoxReq.setKw(infoBoxParam.getKw());
        return infoBoxReq;
    }

    public static BatchInfoBoxReqList infoBoxParameterMoreToBatchInfoBoxReq(@NonNull InfoBoxParameterMore parameterMore) {
        BatchInfoBoxReqList infoBoxReq = new BatchInfoBoxReqList();
        infoBoxReq.setAllowAttrs(parameterMore.getAllowAtts());
        infoBoxReq.setAllowAttrsKey(parameterMore.getAllowAttsKey());
        infoBoxReq.setRelationAttrs(parameterMore.isRelationAtts());
        infoBoxReq.setIds(parameterMore.getIds());
        infoBoxReq.setKws(parameterMore.getKws());
        return infoBoxReq;
    }

    public static EntityProfileBean infoBoxRspToEntityProfileBean(InfoBoxRsp infoBoxRsp) {
        EntityProfileBean entityProfileBean = new EntityProfileBean();
        entityProfileBean.setSelf(entityLinksRspToEntityLinksBean(infoBoxRsp.getSelf()));
        entityProfileBean.setAtts(Sets.newHashSet(toListNoNull(infoBoxRsp.getAttrs(), InfoBoxConverter::infoBoxAttrRspToKVBean)));
        entityProfileBean.setReAtts(Sets.newHashSet(toListNoNull(infoBoxRsp.getReAttrs(), InfoBoxConverter::infoBoxAttrRspToKVBean)));
        entityProfileBean.setPars(toListNoNull(infoBoxRsp.getParents(), InfoBoxConverter::infoBoxConceptRspToEntityBean));
        entityProfileBean.setSons(toListNoNull(infoBoxRsp.getSons(), InfoBoxConverter::infoBoxConceptRspToEntityBean));
        return entityProfileBean;
    }

    public static EntityMultiModalBean infoboxMultiModelRspToEntityMultiModalBean(InfoboxMultiModelRsp infoboxMultiModelRsp) {
        EntityMultiModalBean entityMultiModalBean = new EntityMultiModalBean();
        entityMultiModalBean.setClassId(infoboxMultiModelRsp.getConceptId());
        entityMultiModalBean.setId(infoboxMultiModelRsp.getId());
        entityMultiModalBean.setImg(infoboxMultiModelRsp.getImgUrl());
        entityMultiModalBean.setMeaningTag(infoboxMultiModelRsp.getMeaningTag());
        entityMultiModalBean.setName(infoboxMultiModelRsp.getName());
        consumerIfNoNull(infoboxMultiModelRsp.getType(), entityMultiModalBean::setType);
        entityMultiModalBean.setMultiModals(infoboxMultiModelRsp.getMultiModals());
        return entityMultiModalBean;
    }


    private static EntityBean infoBoxConceptRspToEntityBean(@NonNull InfoBoxConceptRsp conceptRsp) {
        EntityBean entityBean = new EntityBean();
        entityBean.setId(conceptRsp.getId());
        entityBean.setName(conceptRsp.getName());
        entityBean.setClassId(conceptRsp.getId());
        entityBean.setClassIdList(Lists.newArrayList(conceptRsp.getId()));
        entityBean.setConceptIdList(Lists.newArrayList(conceptRsp.getId()));
        entityBean.setConceptId(conceptRsp.getId());
        entityBean.setImg(conceptRsp.getImageUrl());
        entityBean.setMeaningTag(conceptRsp.getMeaningTag());
        entityBean.setType(EntityTypeEnum.CONCEPT.getValue());
        return entityBean;
    }

    private static KVBean<String, List<EntityBean>> infoBoxAttrRspToKVBean(@NonNull InfoBoxRsp.InfoBoxAttrRsp attrRsp) {
        List<EntityBean> entityBeans = toListNoNull(attrRsp.getEntityList(), PromptConverter::promptEntityRspToEntityBean);
        return new KVBean<>(attrRsp.getAttrDefName(), entityBeans, attrRsp.getAttrDefId());
    }


    private static EntityLinksBean entityLinksRspToEntityLinksBean(EntityLinksRsp entityLinksRsp) {
        EntityLinksBean oldBean = new EntityLinksBean();
        oldBean.setClassId(entityLinksRsp.getConceptId());
        oldBean.setId(entityLinksRsp.getId());
        oldBean.setImg(entityLinksRsp.getImgUrl());
        oldBean.setMeaningTag(entityLinksRsp.getMeaningTag());
        oldBean.setName(entityLinksRsp.getName());
        consumerIfNoNull(entityLinksRsp.getType(), oldBean::setType);
        oldBean.setTags(toListNoNull(entityLinksRsp.getTags(), a -> copy(a, Tag.class)));
        oldBean.setDataLinks(toListNoNull(entityLinksRsp.getDataLinks(), InfoBoxConverter::dataLinkRspToDataLinks));
        List<EntityLink> entityLinks = toListNoNull(entityLinksRsp.getEntityLinks(), a -> copy(a, EntityLink.class));
        consumerIfNoNull(entityLinks, a -> oldBean.setEntityLinks(Sets.newHashSet(a)));
        oldBean.setExtra(toListNoNull(entityLinksRsp.getExtraList(), InfoBoxConverter::extraRspToExtraKVBean));
        consumerIfNoNull(entityLinksRsp.getMultiModals(), oldBean::setMultiModals);
        consumerIfNoNull(entityLinksRsp.getKnowledgeIndexs(), oldBean::setKnowledgeIndexs);
        consumerIfNoNull(entityLinksRsp.getDictList(), oldBean::setDictList);
        return oldBean;
    }

    private static ExtraKVBean extraRspToExtraKVBean(@NonNull EntityLinksRsp.ExtraRsp extraRsp) {
        ExtraKVBean extraKVBean = new ExtraKVBean();
        extraKVBean.setAttDefid(extraRsp.getAttrId());
        extraKVBean.setDomain(extraRsp.getDomainValue());
        extraKVBean.setType(extraRsp.getDataType());
        extraKVBean.setK(extraRsp.getName());

        consumerIfNoNull(extraRsp.getValue(), a -> {
            //图片缩略图适配
            if (extraRsp.getDataType() != null && extraRsp.getDataType() == 91) {
                Map<String, Object> objectMap = JsonUtils.stringToMap(JacksonUtils.writeValueAsString(a));
                objectMap.put("thumppath", objectMap.get("thumbnail"));
                extraKVBean.setV(objectMap);
            } else {
                extraKVBean.setV(a);
            }
        });
        return extraKVBean;
    }

    private static DataLinks dataLinkRspToDataLinks(DataLinkRsp dataLink) {
        DataLinks dataLinks = new DataLinks();
        consumerIfNoNull(dataLink.getDataSetId(), dataLinks::setDataSetId);
        dataLinks.setDataSetTitle(dataLink.getDataSetTitle());
        dataLinks.setLinks(toListNoNull(dataLink.getLinks(), InfoBoxConverter::linkRspToLinks));
        return dataLinks;
    }

    private static Links linkRspToLinks(LinksRsp linksRsp) {
        Links links = new Links();
        links.setDataId(linksRsp.getDataId());
        links.setDataTitle(linksRsp.getDataTitle());
        links.setScore(linksRsp.getScore());
        links.setSource(linksRsp.getSource());
        return links;
    }

    public static BatchMultiModalReqList infoBoxMultiModalMoreToBatchMultiModalReq(InfoBoxMultiModalParameterMore infoBoxMultiModalParameterMore) {

        BatchMultiModalReqList multiModalReq = new BatchMultiModalReqList();
        multiModalReq.setIds(infoBoxMultiModalParameterMore.getIds());
        multiModalReq.setKws(infoBoxMultiModalParameterMore.getKws());
        return multiModalReq;
    }
}
