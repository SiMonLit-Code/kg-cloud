package com.plantdata.kgcloud.plantdata.converter.app;

import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import com.plantdata.kgcloud.plantdata.req.common.DataLinks;
import com.plantdata.kgcloud.plantdata.req.common.EntityLink;
import com.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import com.plantdata.kgcloud.plantdata.req.common.Links;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import com.plantdata.kgcloud.plantdata.req.explore.EntityLinksBean;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.LinksRsp;

import java.util.List;

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
        infoBoxReq.setRelationAttrs(infoBoxParam.getIsRelationAtts());
        infoBoxReq.setAllowAttrsKey(infoBoxParam.getAllowAttsKey());
        return infoBoxReq;
    }

    public static EntityProfileBean infoBoxRspToEntityProfileBean(InfoBoxRsp infoBoxRsp) {
        EntityProfileBean entityProfileBean = new EntityProfileBean();
        entityProfileBean.setSelf(entityLinksRspToEntityLinksBean(infoBoxRsp.getSelf()));
//        entityProfileBean.setAtts();
//        entityProfileBean.setReAtts();
//        entityProfileBean.setPars();
//        entityProfileBean.setSons();
        return entityProfileBean;
    }

    private static EntityLinksBean entityLinksRspToEntityLinksBean(EntityLinksRsp entityLinksRsp) {
        EntityLinksBean oldBean = new EntityLinksBean();
        oldBean.setClassId(entityLinksRsp.getConceptId());
        oldBean.setId(entityLinksRsp.getId());
        setIfNoNull(entityLinksRsp.getImg(), a -> oldBean.setImg(a.getHref()));
        oldBean.setMeaningTag(entityLinksRsp.getMeaningTag());
        oldBean.setName(entityLinksRsp.getName());
        setIfNoNull(entityLinksRsp.getType(), a -> oldBean.setType(a.getValue()));
        oldBean.setTags(listToRsp(entityLinksRsp.getTags(), a -> copy(a, Tag.class)));
        oldBean.setDataLinks(listToRsp(entityLinksRsp.getDataLinks(), InfoBoxConverter::dataLinkRspToDataLinks));
        List<EntityLink> entityLinks = listToRsp(entityLinksRsp.getEntityLinks(), a -> copy(a, EntityLink.class));
        setIfNoNull(entityLinks, a -> oldBean.setEntityLinks(Sets.newHashSet(a)));
        oldBean.setExtra(listToRsp(entityLinksRsp.getExtraList(), InfoBoxConverter::extraRspToExtraKVBean));
        return oldBean;
    }

    private static ExtraKVBean extraRspToExtraKVBean(EntityLinksRsp.ExtraRsp extraRsp) {
        ExtraKVBean extraKVBean = new ExtraKVBean();
        extraKVBean.setAttDefid(extraRsp.getAttrId());
        ///todo
//        extraKVBean.setDomain(extraRsp.get);
//        extraKVBean.setType(extraRsp.getValue());
        extraKVBean.setK(extraRsp.getName());

        extraKVBean.setV(extraRsp.getValue());
        return extraKVBean;
    }

    private static DataLinks dataLinkRspToDataLinks(DataLinkRsp dataLink) {
        DataLinks dataLinks = new DataLinks();
        setIfNoNull(dataLink.getDataSetId(), a -> dataLinks.setDataSetId(a.intValue()));
        dataLinks.setDataSetTitle(dataLink.getDataSetTitle());
        dataLinks.setLinks(listToRsp(dataLink.getLinks(), InfoBoxConverter::linkRspToLinks));
        return dataLinks;
    }

    private static Links linkRspToLinks(LinksRsp linksRsp) {
        Links links = new Links();
        links.setDataId(linksRsp.getDataId());
        links.setDataTitle(linksRsp.getDataTitle());
        links.setScore(linksRsp.getScore());
        links.setSource(links.getSource());
        return links;
    }
}
