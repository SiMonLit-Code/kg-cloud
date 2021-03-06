package ai.plantdata.kgcloud.plantdata.converter.graph;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.req.common.*;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.MongoQueryConverter;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.AbstractGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.common.AttrScreeningBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;
import ai.plantdata.kgcloud.plantdata.req.explore.function.TimeGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.path.PathGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.relation.RelationGraphParameter;
import ai.plantdata.kgcloud.sdk.req.app.MetaDataReq;
import ai.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import ai.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import ai.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/24 10:59
 */
public class ExploreCommonConverter extends BasicConverter {
    private static final String RELATION_START_TIME_SSE_KEY = "开始时间";
    private static final String RELATION_END_TIME_SSE_KEY = "结束时间";

    static <T extends GraphEntityRsp> EntityBean entityBeanToGraphEntityRsp(T newEntity) {
        EntityBean oldEntity = new EntityBean();
        oldEntity.setId(newEntity.getId());
        oldEntity.setName(newEntity.getName());
        oldEntity.setMeaningTag(newEntity.getMeaningTag());
        oldEntity.setClassId(newEntity.getClassId() == null ? NumberUtils.LONG_ZERO : newEntity.getClassId());
        oldEntity.setClassIdList(newEntity.getConceptIdList());
        oldEntity.setConceptId(newEntity.getConceptId());
        oldEntity.setImg(newEntity.getImgUrl());
        oldEntity.setConceptIdList(newEntity.getConceptIdList());
        oldEntity.setConceptName(newEntity.getConceptName());
        oldEntity.setCreationTime(newEntity.getCreationTime());
        consumerIfNoNull(newEntity.getStartTime(), a -> oldEntity.setFromTime(dateToString(a)));
        consumerIfNoNull(newEntity.getEndTime(), a -> oldEntity.setToTime(dateToString(a)));
        oldEntity.setNodeStyle(newEntity.getNodeStyle());
        oldEntity.setLabelStyle(newEntity.getLabelStyle());
        oldEntity.setScore(newEntity.getScore());
        oldEntity.setType(newEntity.getType());
        Additional additional = new Additional();
        additional.setIsOpenGis(newEntity.getOpenGis());
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }


    static <T extends PathGraphParameter> CommonPathReq buildPathReq(T pathGraphParam) {
        CommonPathReq commonPathReq = new CommonPathReq();
        commonPathReq.setEnd(pathGraphParam.getEnd());
        commonPathReq.setDegreeLimit(pathGraphParam.getDegreeLimit());
        commonPathReq.setStart(pathGraphParam.getStart());
        consumerIfNoNull(pathGraphParam.getIsShortest(), commonPathReq::setShortest);
        return commonPathReq;
    }

    static <T extends RelationGraphParameter> CommonRelationReq buildRelationReq(T relationParam) {
        CommonRelationReq relationReq = new CommonRelationReq();
        relationReq.setIds(relationParam.getIds());
        relationReq.setDegreeLimit(relationParam.getDegreeLimit());
        return relationReq;
    }

    static TimeFilterExploreReq buildTimeFilter(TimeGraphParameter timeFilter) {
        if (timeFilter == null || timeFilter.getTimeFilterType() == null) {
            return null;
        }
        TimeFilterExploreReq exploreReq = new TimeFilterExploreReq();
        exploreReq.setTimeFilterType(timeFilter.getTimeFilterType());
        consumerIfNoNull(timeFilter.getFromTime(), b -> exploreReq.setFromTime(stringToDate(b)));
        consumerIfNoNull(timeFilter.getToTime(), b -> exploreReq.setToTime(stringToDate(b)));
        consumerIfNoNull(timeFilter.getSort(), a -> exploreReq.setSort(a.getDesc()));
        return exploreReq;
    }

    static Map<Long, Object> buildReasonConfig(Map<Long, JSONObject> config) {
        String configJson = JacksonUtils.writeValueAsString(config);
        return JacksonUtils.readValue(configJson, new TypeReference<Map<Long, Object>>() {
        });
    }

    static <T extends AbstractGraphParameter, R extends BasicGraphExploreReqList> R abstractGraphParameterToBasicGraphExploreReq(T to, R rs) {
        rs.setDistance(to.getDistance());
        consumerIfNoNull(to.getAllowTypesKey(), rs::setAllowConceptsKey);
        consumerIfNoNull(to.getReplaceClassIds(), rs::setReplaceClassIds);
        consumerIfNoNull(to.getReplaceClassIdsKey(), rs::setReplaceClassKeys);
        consumerIfNoNull(to.getIsRelationMerge(), rs::setRelationMerge);
        consumerIfNoNull(to.getAllowAttsKey(), rs::setAllowAttrsKey);
        consumerIfNoNull(to.getAllowTypes(), rs::setAllowConcepts);
        consumerIfNoNull(to.getAllowAttrGroups(), a -> rs.setAllowAttrGroups(toListNoNull(a, Long::valueOf)));
        consumerIfNoNull(to.getAllowAtts(), rs::setAllowAttrs);
        consumerIfNoNull(to.getEntityQuery(), a -> rs.setEntityFilters(toListNoNull(a, MongoQueryConverter::entityScreeningBeanToEntityQueryFiltersReq)));
        consumerIfNoNull(to.getAttAttFilters(), a -> rs.setEdgeAttrFilters(toListNoNull(a, ExploreCommonConverter::attrScreeningBeanToRelationAttrReq)));
        consumerIfNoNull(to.getReservedAttFilters(), a -> rs.setReservedAttFilters(toListNoNull(a, ExploreCommonConverter::attrScreeningBeanToMetaDataReq)));
        return rs;
    }

    static <T extends BasicGraphExploreRsp> GraphBean basicGraphExploreRspToGraphBean(T exploreRsp) {
        GraphBean graphBean = new GraphBean();
        graphBean.setLevel1HasNextPage(exploreRsp.getHasNextPage());
        graphBean.setEntityList(toListNoNull(exploreRsp.getEntityList(), ExploreCommonConverter::entityBeanToCommonEntityRsp));
        Map<Long, Long> entityConceptMap = exploreRsp.getEntityList().stream().filter(a -> a.getConceptId() != null).collect(Collectors.toMap(CommonEntityRsp::getId, CommonEntityRsp::getConceptId, (a, b) -> b));
        graphBean.setRelationList(toListNoNull(exploreRsp.getRelationList(), a -> ExploreCommonConverter.relationBeanToGraphRelationRsp(a, entityConceptMap)));
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
        graphStatBean.setRs(toListNoNull(statisticRsp.getStatisticDetails(), ExploreCommonConverter::graphStatisticDetailRspToGraphStatDetailBean));
        graphStatBean.setType(statisticRsp.getConceptId());
        return graphStatBean;
    }

    static void fillStatisticConfig(GraphBean graphBean, List<GraphStatisticRsp> statisticRspList) {
        consumerIfNoNull(statisticRspList, a -> {
            List<GraphStatBean> graphStatBeans = toListNoNull(a, ExploreCommonConverter::graphStatisticRspToGraphStatBean);
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
        EntityBean oldEntity = entityBeanToGraphEntityRsp(newEntity);
        oldEntity.setTags(toListNoNull(newEntity.getTags(), a -> copy(a, Tag.class)));
        Additional additional = new Additional();
        ///暂时不管
        //additional.setColor(newEntity);
        //additional.setIsOpenGis();
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }

    private static RelationBean relationBeanToGraphRelationRsp(GraphRelationRsp newBean, Map<Long, Long> entityConceptMap) {
        RelationBean oldBean = new RelationBean();
        BeanUtils.copyProperties(newBean, oldBean);
        oldBean.setFrom(newBean.getFrom());
        oldBean.setTo(newBean.getTo());
        oldBean.setDirection(newBean.getDirection());
        oldBean.setBatch(newBean.getBatch());
        consumerIfNoNull(newBean.getOrigin(), a -> {
            oldBean.setOrigin(new OriginBean(a.getSource(), a.getSourceReason()));
        });
        oldBean.setAttName(newBean.getAttName());
        consumerIfNoNull(newBean.getStartTime(), oldBean::addStartTime);
        consumerIfNoNull(newBean.getEndTime(), oldBean::addEndTime);
        //推理规则id强行适配
        if (newBean.getReasonRuleId() != null) {
            oldBean.setAttId(-newBean.getReasonRuleId());
            oldBean.setType(1);
        } else {
            consumerIfNoNull(newBean.getAttId(), a -> oldBean.setAttId(a.longValue()));
        }
        List<GraphRelationRsp> allRelation = Lists.newArrayList();
        consumerIfNoNull(newBean.getSourceRelationList(), allRelation::addAll);
        if (CollectionUtils.isEmpty(allRelation)) {
            allRelation.add(newBean);
        }

        //时间
        if (!CollectionUtils.isEmpty(newBean.getSourceRelationList())) {
            newBean.getSourceRelationList().forEach(relationRsp -> {
                if (newBean.getId() != null && !relationRsp.getId().equals(newBean.getId())) {
                    consumerIfNoNull(relationRsp.getEndTime(), oldBean::addEndTime);
                    consumerIfNoNull(relationRsp.getStartTime(), oldBean::addStartTime);
                }
            });
        }

        //边数值属性
        List<RelationInfoBean> numEdgeAttrInfoList = toListNoNull(allRelation,
                a -> ExploreCommonConverter.edgeInfoToRelationInfoBean(a, entityConceptMap.get(a.getFrom())));
        //边对象属性
        List<RelationInfoBean> objEdgeAttrInfoList = toListNoNull(allRelation,
                a -> ExploreCommonConverter.edgeInfoToRelationInfoObjBean(a.getId(), a.getObjAttrs()));
        consumerIfNoNull(numEdgeAttrInfoList, oldBean::setnRInfo);
        consumerIfNoNull(objEdgeAttrInfoList, oldBean::setoRInfo);

        return oldBean;
    }

    private static KVBean<String, String> edgeInfoToKvBean(BasicRelationRsp.EdgeDataInfo edgeDataInfo, Integer attrDefId, Long conceptId) {
        KVBean<String, String> kvBean = new KVBean<>(edgeDataInfo.getName(), edgeDataInfo.getValue().toString(), attrDefId);
        kvBean.setDomain(conceptId);
        kvBean.setType(edgeDataInfo.getDataType());
        return kvBean;
    }

    private static RelationInfoBean edgeInfoToRelationInfoBean(GraphRelationRsp relationBean, Long conceptId) {
        RelationInfoBean infoBean = new RelationInfoBean();
        infoBean.setId(relationBean.getId());
        consumerIfNoNull(toListNoNull(relationBean.getDataValAttrs(), a -> edgeInfoToKvBean(a, relationBean.getAttId(), conceptId)), infoBean::setKvs);
        consumerIfNoNull(relationBean.getStartTime(), a -> infoBean.addKv(RELATION_START_TIME_SSE_KEY, a));
        consumerIfNoNull(relationBean.getEndTime(), a -> infoBean.addKv(RELATION_END_TIME_SSE_KEY, a));
        return infoBean;
    }

    private static RelationInfoBean edgeInfoToRelationInfoObjBean(String relationId, List<BasicRelationRsp.EdgeObjectInfo> objList) {
        RelationInfoBean infoBean = new RelationInfoBean();
        infoBean.setId(relationId);
        infoBean.setKvs(toListNoNull(objList, a -> new KVBean<>(a.getName(), a.getEntityName())));
        return infoBean;
    }

    public static RelationAttrReq attrScreeningBeanToRelationAttrReq(@NonNull AttrScreeningBean screeningBean) {
        RelationAttrReq relationAttrReq = JsonUtils.parseObj(JacksonUtils.writeValueAsString(screeningBean), RelationAttrReq.class);
        consumerIfNoNull(relationAttrReq, a -> a.set$ne(screeningBean.get$neq()));
        return relationAttrReq;
    }

    public static MetaDataReq attrScreeningBeanToMetaDataReq(@NonNull AttrScreeningBean screeningBean) {
        MetaDataReq metaDataReq = JsonUtils.parseObj(JacksonUtils.writeValueAsString(screeningBean), MetaDataReq.class);
        consumerIfNoNull(metaDataReq, a -> a.set$ne(screeningBean.get$neq()));
        return metaDataReq;
    }
}
