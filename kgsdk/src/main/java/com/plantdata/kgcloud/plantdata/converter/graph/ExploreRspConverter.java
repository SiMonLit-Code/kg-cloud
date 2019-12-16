package com.plantdata.kgcloud.plantdata.converter.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.link.LinkUtil;
import com.plantdata.kgcloud.plantdata.req.common.Additional;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationInfoBean;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.explore.GraphBean;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 16:31
 */
public class ExploreRspConverter extends BasicConverter {

    public static GraphBean commonBasicGraphExploreRspToGraphBean(CommonBasicGraphExploreRsp exploreRsp) {
        return basicGraphExploreRspToGraphBean(exploreRsp);
    }

    private static <T extends BasicGraphExploreRsp> GraphBean basicGraphExploreRspToGraphBean(T exploreRsp) {
        GraphBean graphBean = new GraphBean();
        graphBean.setEntityList(listToRsp(exploreRsp.getEntityList(), ExploreRspConverter::entityBeanToCommonEntityRsp));
        graphBean.setRelationList(listToRsp(exploreRsp.getRelationList(), ExploreRspConverter::relationBeanToGraphRelationRsp));
        return graphBean;
    }


    private static EntityBean entityBeanToCommonEntityRsp(CommonEntityRsp newEntity) {
        EntityBean oldEntity = new EntityBean();
        oldEntity.setConceptId(newEntity.getConceptId());
        oldEntity.setConceptIdList(newEntity.getConceptIdList());
        oldEntity.setConceptName(newEntity.getConceptName());
        oldEntity.setCreationTime(newEntity.getCreationTime());
        oldEntity.setFromTime(dateToString(newEntity.getStartTime()));
        oldEntity.setToTime(dateToString(newEntity.getEndTime()));
        oldEntity.setNodeStyle(newEntity.getNodeStyle());
        oldEntity.setLabelStyle(newEntity.getLabelStyle());
        oldEntity.setScore(newEntity.getScore());
        oldEntity.setType(newEntity.getType().getValue());
        oldEntity.setTags(listToRsp(newEntity.getTags(), a -> copy(a, Tag.class)));
        Additional additional = new Additional();
        //暂时不管
        ///additional.setColor();
        ///additional.setIsOpenGis();
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }

    private static RelationBean relationBeanToGraphRelationRsp(GraphRelationRsp newBean) {
        RelationBean oldBean = LinkUtil.link(newBean);
        oldBean.addEndTime(newBean.getEndTime());
        oldBean.addStartTime(newBean.getStartTime());
        oldBean.setBatch(newBean.getBatch());
        oldBean.setAttId(newBean.getAttId());
        oldBean.setAttName(newBean.getAttName());
        //时间
        for (GraphRelationRsp relationRsp : newBean.getSourceRelationList()) {
            setIfNoNull(relationRsp.getEndTime(), oldBean::addEndTime);
            setIfNoNull(relationRsp.getStartTime(), oldBean::addStartTime);
        }
        //边属性
        List<GraphRelationRsp> allRelation = Lists.newArrayList();
        setIfNoNull(newBean.getSourceRelationList(), allRelation::addAll);
        List<RelationInfoBean> numEdgeAttrInfoList = listToRsp(allRelation, ExploreRspConverter::edgeInfoToRelationInfoBean);
        List<RelationInfoBean> objEdgeAttrInfoList = listToRsp(allRelation, ExploreRspConverter::edgeInfoToRelationInfoBean);
        setIfNoNull(numEdgeAttrInfoList, oldBean::setnRInfo);
        setIfNoNull(objEdgeAttrInfoList, oldBean::setoRInfo);

        return oldBean;
    }

    private static RelationInfoBean edgeInfoToRelationInfoBean(GraphRelationRsp relationBean) {
        RelationInfoBean infoBean = new RelationInfoBean();
        infoBean.setId(relationBean.getId());
        infoBean.setKvs(listToRsp(relationBean.getDataValAttrs(), a -> edgeInfoToKvBean(a, relationBean.getAttId())));
        return infoBean;
    }

    private static KVBean<String, String> edgeInfoToKvBean(BasicRelationRsp.EdgeInfo edgeInfo, Integer attrDefId) {
        KVBean<String, String> kvBean = new KVBean<>(edgeInfo.getName(), edgeInfo.getValue().toString(), attrDefId);
        ///kvBean.setDomain();
        kvBean.setType(edgeInfo.getDataType());
        return kvBean;
    }
}
