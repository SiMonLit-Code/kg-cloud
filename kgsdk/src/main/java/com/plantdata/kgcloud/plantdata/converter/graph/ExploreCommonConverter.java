package com.plantdata.kgcloud.plantdata.converter.graph;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.MongoQueryConverter;
import com.plantdata.kgcloud.plantdata.link.LinkUtil;
import com.plantdata.kgcloud.plantdata.req.common.Additional;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationInfoBean;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.AbstrackGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.AttrScreeningBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;
import com.plantdata.kgcloud.plantdata.req.explore.function.TimeGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.path.PathGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.relation.RelationGraphParameter;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/24 10:59
 */
public class ExploreCommonConverter extends BasicConverter {

    static <T extends PathGraphParameter> CommonPathReq buildPathReq(T pathGraphParam) {
        CommonPathReq commonPathReq = new CommonPathReq();
        commonPathReq.setEnd(pathGraphParam.getEnd());
        commonPathReq.setStart(pathGraphParam.getStart());
        consumerIfNoNull(pathGraphParam.getIsShortest(), commonPathReq::setShortest);
        return commonPathReq;
    }

    static <T extends RelationGraphParameter> CommonRelationReq buildRelationReq(T relationParam) {
        CommonRelationReq relationReq = new CommonRelationReq();
        relationReq.setIds(relationParam.getIds());
        return relationReq;
    }

    static TimeFilterExploreReq buildTimeFilter(TimeGraphParameter timeFilter) {
        TimeFilterExploreReq exploreReq = new TimeFilterExploreReq();
        exploreReq.setTimeFilterType(timeFilter.getTimeFilterType());
        exploreReq.setFromTime(stringToDate(timeFilter.getFromTime()));
        exploreReq.setToTime(stringToDate(timeFilter.getToTime()));
        consumerIfNoNull(timeFilter.getSort(), a -> exploreReq.setSort(a.getDesc()));
        return exploreReq;
    }

    static <T extends AbstrackGraphParameter, R extends BasicGraphExploreReq> R abstractGraphParameterToBasicGraphExploreReq(T to, R rs) {
        rs.setDistance(to.getDistance());
        consumerIfNoNull(to.getAllowTypesKey(), rs::setAllowConceptsKey);
        consumerIfNoNull(to.getReplaceClassIds(), rs::setReplaceClassIds);
        consumerIfNoNull(to.getReplaceClassIdsKey(), rs::setReplaceClassKeys);
        consumerIfNoNull(to.getAllowAttsKey(), rs::setAllowAttrsKey);
        consumerIfNoNull(to.getAllowTypes(), rs::setAllowConcepts);
        consumerIfNoNull(to.getAllowAttrGroups(), a -> rs.setAllowAttrGroups(listToRsp(a, Long::valueOf)));
        consumerIfNoNull(to.getAllowAtts(), rs::setAllowAttrs);
        consumerIfNoNull(to.getEntityQuery(), a -> rs.setEntityFilters(listToRsp(a, MongoQueryConverter::entityScreeningBeanToEntityQueryFiltersReq)));
        consumerIfNoNull(to.getAttAttFilters(), a -> listToRsp(a, ExploreCommonConverter::attrScreeningBeanToRelationAttrReq));
        return rs;
    }

    static <T extends BasicGraphExploreRsp> GraphBean basicGraphExploreRspToGraphBean(T exploreRsp) {
        GraphBean graphBean = new GraphBean();
        graphBean.setEntityList(listToRsp(exploreRsp.getEntityList(), ExploreCommonConverter::entityBeanToCommonEntityRsp));
        graphBean.setRelationList(listToRsp(exploreRsp.getRelationList(), ExploreCommonConverter::relationBeanToGraphRelationRsp));
        return graphBean;
    }

    static BasicStatisticReq graphStatBeanToBasicStatisticReq(GraphStatBean graphStatBean) {
        BasicStatisticReq statisticReq = new BasicStatisticReq();
        statisticReq.setAttrIdList(graphStatBean.getAtts());
        statisticReq.setConceptId(graphStatBean.getType());
        statisticReq.setKey(graphStatBean.getKey());
        return statisticReq;
    }

    static GraphStatBean graphStatisticRspToGraphStatBean(GraphStatisticRsp statisticRsp) {
        GraphStatBean graphStatBean = new GraphStatBean();
        graphStatBean.setAtts(statisticRsp.getAttrIdList());
        graphStatBean.setKey(statisticRsp.getKey());
        graphStatBean.setRs(listToRsp(statisticRsp.getStatisticDetails(), ExploreCommonConverter::graphStatisticDetailRspToGraphStatDetailBean));
        graphStatBean.setType(statisticRsp.getConceptId());
        return graphStatBean;
    }

    static void fillStatisticConfig(GraphBean graphBean, List<GraphStatisticRsp> statisticRspList) {
        consumerIfNoNull(statisticRspList, a -> {
            List<GraphStatBean> graphStatBeans = listToRsp(a, ExploreCommonConverter::graphStatisticRspToGraphStatBean);
            graphBean.setStats(graphStatBeans);
        });
    }

    private static GraphStatBean.GraphStatDetailBean graphStatisticDetailRspToGraphStatDetailBean(GraphStatisticRsp.GraphStatisticDetailRsp detailRsp) {
        GraphStatBean.GraphStatDetailBean statDetailBean = new GraphStatBean.GraphStatDetailBean();
        statDetailBean.setCount(detailRsp.getCount());
        statDetailBean.setId(detailRsp.getId());
        return statDetailBean;
    }

    private static EntityBean entityBeanToCommonEntityRsp(CommonEntityRsp newEntity) {
        EntityBean oldEntity = new EntityBean();
        oldEntity.setId(newEntity.getId());
        oldEntity.setName(newEntity.getName());
        oldEntity.setMeaningTag(newEntity.getMeaningTag());
        oldEntity.setClassId(newEntity.getClassId());
        oldEntity.setClassIdList(newEntity.getConceptIdList());
        oldEntity.setConceptId(newEntity.getConceptId());
        oldEntity.setConceptIdList(newEntity.getConceptIdList());
        oldEntity.setConceptName(newEntity.getConceptName());
        oldEntity.setCreationTime(newEntity.getCreationTime());
        consumerIfNoNull(newEntity.getStartTime(), a -> oldEntity.setFromTime(dateToString(a)));
        consumerIfNoNull(newEntity.getEndTime(), a -> oldEntity.setToTime(dateToString(a)));
        oldEntity.setNodeStyle(newEntity.getNodeStyle());
        oldEntity.setLabelStyle(newEntity.getLabelStyle());
        oldEntity.setScore(newEntity.getScore());
        oldEntity.setType(newEntity.getType());
        oldEntity.setTags(listToRsp(newEntity.getTags(), a -> copy(a, Tag.class)));
        Additional additional = new Additional();
        //暂时不管
        ///additional.setColor();
        ///additional.setIsOpenGis();
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }

    private static RelationBean relationBeanToGraphRelationRsp(GraphRelationRsp newBean) {
        RelationBean oldBean = new RelationBean();
        BeanUtils.copyProperties(newBean, oldBean);
        oldBean.setFrom(newBean.getFrom());
        oldBean.setTo(newBean.getTo());
        oldBean.addEndTime(newBean.getEndTime());
        oldBean.addStartTime(newBean.getStartTime());
        oldBean.setBatch(newBean.getBatch());
        oldBean.setAttId(newBean.getAttId());
        oldBean.setAttName(newBean.getAttName());
        //时间
        if (!CollectionUtils.isEmpty(newBean.getSourceRelationList())) {
            newBean.getSourceRelationList().forEach(relationRsp -> {
                consumerIfNoNull(relationRsp.getEndTime(), oldBean::addEndTime);
                consumerIfNoNull(relationRsp.getStartTime(), oldBean::addStartTime);
            });
        }
        //边属性
        List<GraphRelationRsp> allRelation = Lists.newArrayList();
        consumerIfNoNull(newBean.getSourceRelationList(), allRelation::addAll);
        List<RelationInfoBean> numEdgeAttrInfoList = listToRsp(allRelation, ExploreCommonConverter::edgeInfoToRelationInfoBean);
        List<RelationInfoBean> objEdgeAttrInfoList = listToRsp(allRelation, ExploreCommonConverter::edgeInfoToRelationInfoBean);
        consumerIfNoNull(numEdgeAttrInfoList, oldBean::setnRInfo);
        consumerIfNoNull(objEdgeAttrInfoList, oldBean::setoRInfo);

        return oldBean;
    }

    private static KVBean<String, String> edgeInfoToKvBean(BasicRelationRsp.EdgeInfo edgeInfo, Integer attrDefId) {
        KVBean<String, String> kvBean = new KVBean<>(edgeInfo.getName(), edgeInfo.getValue().toString(), attrDefId);
        ///kvBean.setDomain();
        kvBean.setType(edgeInfo.getDataType());
        return kvBean;
    }

    private static RelationInfoBean edgeInfoToRelationInfoBean(GraphRelationRsp relationBean) {
        RelationInfoBean infoBean = new RelationInfoBean();
        infoBean.setId(relationBean.getId());
        infoBean.setKvs(listToRsp(relationBean.getDataValAttrs(), a -> edgeInfoToKvBean(a, relationBean.getAttId())));
        return infoBean;
    }

    private static RelationAttrReq attrScreeningBeanToRelationAttrReq(@NonNull AttrScreeningBean screeningBean) {
        return LinkUtil.link(screeningBean);
    }
}
