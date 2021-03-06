package ai.plantdata.kgcloud.plantdata.converter.graph;

import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.req.common.AttrSortBean;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import ai.plantdata.kgcloud.plantdata.req.common.RelationBean;
import ai.plantdata.kgcloud.plantdata.req.common.RelationInfoBean;
import ai.plantdata.kgcloud.plantdata.req.common.Tag;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.explore.RuleGeneralGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.TimeGeneralGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GeneralGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import ai.plantdata.kgcloud.plantdata.req.explore.path.PathGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.path.TimePathGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.relation.RelationGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.relation.RuleRelationGraphParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.relation.TimeRelationGraphParameter;
import ai.plantdata.kgcloud.sdk.req.app.AttrSortReq;
import ai.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import ai.plantdata.kgcloud.sdk.req.app.PageReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CoordinateReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 15:01
 */
@Slf4j
public class ExploreReqConverter extends BasicConverter {
    /**
     * 普通图探索转换
     *
     * @param param GeneralGraphParameter
     * @return CommonExploreReq
     */
    public static CommonExploreReqList generalGraphParameterToCommonExploreReq(@NonNull GeneralGraphParameter param) {
        CommonExploreReqList exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonExploreReqList());
        exploreReq.setCommon(generalGraphParameterToCommonFiltersReq(param));
        exploreReq.setPage(new PageReq(param.getPageNo(), param.getPageSize()));
        ///300新增exploreRsp.setDisAllowConcepts(Collections.emptyList());
        consumerIfNoNull(param.getGraphBean(), a -> exploreReq.setGraphReq(ExploreReqConverter.graphBeanToBasicGraphExploreRsp(a)));
        return exploreReq;
    }

    /**
     * 业务规则
     *
     * @param param GeneralGraphParameter
     * @return ExploreByKgQlReq
     */
    public static ExploreByKgQlReq generalGraphParameterToExploreByKgQlReq(@NonNull GeneralGraphParameter param) {
        ExploreByKgQlReq exploreByKgQlReq = new ExploreByKgQlReq();
        exploreByKgQlReq.setKgQl(param.getGraphRule());
        exploreByKgQlReq.setRelationMerge(param.getIsRelationMerge());
        exploreByKgQlReq.setEntityId(param.getId());
        return exploreByKgQlReq;
    }

    /**
     * 时序图探索
     *
     * @param param TimeGeneralGraphParameter
     * @return CommonTimingExploreReq
     */
    public static CommonTimingExploreReqList timeGeneralGraphParameterToCommonTimingExploreReq(TimeGeneralGraphParameter param) {
        CommonTimingExploreReqList exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonTimingExploreReqList());
        exploreReq.setPage(new PageReq(param.getPageNo(), param.getPageSize()));
        exploreReq.setTimeFilters(ExploreCommonConverter.buildTimeFilter(param));
        exploreReq.setCommon(generalGraphParameterToCommonFiltersReq(param));
        return exploreReq;
    }

    /**
     * 推理图探索
     *
     * @param param RuleGeneralGraphParameter
     * @return CommonReasoningExploreReq
     */
    public static CommonReasoningExploreReqList ruleGeneralGraphParameterToCommonReasoningExploreReq(RuleGeneralGraphParameter param) {
        CommonReasoningExploreReqList exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonReasoningExploreReqList());
        exploreReq.setCommon(generalGraphParameterToCommonFiltersReq(param));
        exploreReq.setPage(new PageReq(param.getPageNo(), param.getPageSize()));
        exploreReq.setTimeFilters(ExploreCommonConverter.buildTimeFilter(param));
        consumerIfNoNull(param.getReasoningRuleConfigs(), a -> exploreReq.setReasoningRuleConfigs(ExploreCommonConverter.buildReasonConfig(a)));
        return exploreReq;
    }

    /**
     * 关联分析转换
     *
     * @param param RelationGraphParameter
     * @return RelationReqAnalysisReq
     */
    public static RelationReqAnalysisReqList generalGraphParameterToRelationReqAnalysisReq(RelationGraphParameter param) {
        long start = System.currentTimeMillis();
        RelationReqAnalysisReqList pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationReqAnalysisReqList());
        pathAnalysisReq.setRelation(ExploreCommonConverter.buildRelationReq(param));
        consumerIfNoNull(param.getStatsConfig(), a -> pathAnalysisReq.setConfigList(toListNoNull(a, ExploreCommonConverter::graphStatBeanToBasicStatisticReq)));
        log.error("start" + (System.currentTimeMillis() - start));
        return pathAnalysisReq;
    }

    /**
     * 关联分析转换
     *
     * @param param TimeRelationGraphParameter
     * @return RelationTimingAnalysisReq
     */
    public static RelationTimingAnalysisReqList timeRelationGraphParameterToRelationTimingAnalysisReq(TimeRelationGraphParameter param) {
        RelationTimingAnalysisReqList pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationTimingAnalysisReqList());
        pathAnalysisReq.setRelation(ExploreCommonConverter.buildRelationReq(param));
        consumerIfNoNull(param.getStatsConfig(), a -> pathAnalysisReq.setConfigList(toListNoNull(a, ExploreCommonConverter::graphStatBeanToBasicStatisticReq)));
        consumerIfNoNull(param, a -> pathAnalysisReq.setTimeFilters(ExploreCommonConverter.buildTimeFilter(a)));
        return pathAnalysisReq;
    }

    /**
     * 关联推理转换
     *
     * @param param RuleRelationGraphParameter
     * @return RelationReasoningAnalysisReq
     */
    public static RelationReasoningAnalysisReqList ruleRelationGraphParameterToRelationReasoningAnalysisReq(RuleRelationGraphParameter param) {
        RelationReasoningAnalysisReqList pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationReasoningAnalysisReqList());
        pathAnalysisReq.setRelation(ExploreCommonConverter.buildRelationReq(param));
        consumerIfNoNull(param.getStatsConfig(), a -> pathAnalysisReq.setConfigList(toListNoNull(a, ExploreCommonConverter::graphStatBeanToBasicStatisticReq)));
        consumerIfNoNull(param.getReasoningRuleConfigs(), a -> pathAnalysisReq.setReasoningRuleConfigs(ExploreCommonConverter.buildReasonConfig(a)));
        consumerIfNoNull(param, a -> pathAnalysisReq.setTimeFilters(ExploreCommonConverter.buildTimeFilter(a)));
        return pathAnalysisReq;
    }

    /**
     * 路径发现
     *
     * @param graphParam PathGraphParameter
     * @return PathAnalysisReq
     */
    public static PathAnalysisReqList pathGraphParameterToPathAnalysisReq(PathGraphParameter graphParam) {
        PathAnalysisReqList pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(graphParam, new PathAnalysisReqList());
        pathAnalysisReq.setPath(ExploreCommonConverter.buildPathReq(graphParam));
        pathAnalysisReq.setConfigList(toListNoNull(graphParam.getStatsConfig(), ExploreCommonConverter::graphStatBeanToBasicStatisticReq));
        return pathAnalysisReq;
    }

    /**
     * 时序路径发现
     *
     * @param graphParam TimePathGraphParameter
     * @return PathTimingAnalysisReq
     */
    public static PathTimingAnalysisReqList timePathGraphParameterToPathTimingAnalysisReq(TimePathGraphParameter graphParam) {
        PathTimingAnalysisReqList pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(graphParam, new PathTimingAnalysisReqList());
        pathAnalysisReq.setTimeFilters(ExploreCommonConverter.buildTimeFilter(graphParam));
        pathAnalysisReq.setPath(ExploreCommonConverter.buildPathReq(graphParam));
        pathAnalysisReq.setConfigList(toListNoNull(graphParam.getStatsConfig(), ExploreCommonConverter::graphStatBeanToBasicStatisticReq));
        return pathAnalysisReq;
    }

    private static CommonFiltersReq generalGraphParameterToCommonFiltersReq(GeneralGraphParameter graphParameter) {
        CommonFiltersReq commonFiltersReq = new CommonFiltersReq();
        commonFiltersReq.setHyponymyDistance(graphParameter.getHyponymyDistance());
        commonFiltersReq.setId(graphParameter.getId());
        commonFiltersReq.setKw(graphParameter.getKw());
        commonFiltersReq.setHighLevelSize(graphParameter.getHighLevelSize());
        consumerIfNoNull(graphParameter.getPrivateAttRead(), commonFiltersReq::setPrivateAttRead);
        consumerIfNoNull(graphParameter.getDirection(), a -> {
            //旧的 2是反向，新的-1 反向
            commonFiltersReq.setDirection(a == 2 ? -1 : a);
        });
        consumerIfNoNull(graphParameter.getAttSorts(), a -> commonFiltersReq.setEdgeAttrSorts(toListNoNull(a, ExploreReqConverter::attrSortBeanToAttrSortReq)));
        return commonFiltersReq;
    }

    private static AttrSortReq attrSortBeanToAttrSortReq(@NonNull AttrSortBean attrSortBean) {
        AttrSortReq attrSortReq = new AttrSortReq();
        attrSortReq.setSort(attrSortBean.getSort());
        attrSortReq.setAttrId(attrSortBean.getAttrId());
        attrSortReq.setSeqNo(attrSortBean.getSeqNo());
        return attrSortReq;
    }

    private static BasicGraphExploreRsp graphBeanToBasicGraphExploreRsp(GraphBean graphBean) {
        BasicGraphExploreRsp graphExploreRsp = new BasicGraphExploreRsp();

        graphExploreRsp.setEntityList(toListNoNull(graphBean.getEntityList(), a -> ExploreReqConverter.entityBeanToCommonEntityRsp(a, new CommonEntityRsp())));
        graphExploreRsp.setRelationList(toListNoNull(graphBean.getRelationList(), ExploreReqConverter::relationBeanToGraphRelationRsp));
        graphExploreRsp.setHasNextPage(graphBean.getLevel1HasNextPage());
        return graphExploreRsp;
    }

    private static GraphRelationRsp relationBeanToGraphRelationRsp(RelationBean relationBean) {
        GraphRelationRsp link = new GraphRelationRsp();
        link.setId(relationBean.getId());
        link.setFrom(relationBean.getFrom());
        link.setTo(relationBean.getTo());
        consumerIfNoNull(relationBean.getAttId(), a -> {
            if (relationBean.getType() == 1) {
                link.setReasonRuleId(a);
            } else {
                link.setAttId(a.intValue());
            }
        });
        link.setAttName(relationBean.getAttName());
        link.setBatch(relationBean.getBatch());
        link.setDirection(relationBean.getDirection());
        relationBean.setReliability(relationBean.getReliability());
        relationBean.setScore(relationBean.getScore());
        link.setLabelStyle(relationBean.getLabelStyle());
        link.setLinkStyle(relationBean.getLinkStyle());
        fillSourceAndAttr(relationBean, link);
        if (!CollectionUtils.isEmpty(relationBean.getStartTime())) {
            link.setStartTime(relationBean.getStartTime().get(0));
        }
        if (!CollectionUtils.isEmpty(relationBean.getEndTime())) {
            link.setEndTime(relationBean.getEndTime().get(0));
        }
        return link;
    }

    private static void fillSourceAndAttr(RelationBean relationBean, GraphRelationRsp main) {
        List<GraphRelationRsp> sourceRelationList = Lists.newArrayList();
        List<String> relationIdlIst = Lists.newArrayList();
        Map<String, List<RelationInfoBean>> edgeObjAttrMap = relationBean.getoRInfo().stream().peek(a -> relationIdlIst.add(a.getId())).collect(Collectors.groupingBy(RelationInfoBean::getId));
        Map<String, List<RelationInfoBean>> edgeNumAttrMap = relationBean.getnRInfo().stream().peek(a -> relationIdlIst.add(a.getId())).collect(Collectors.groupingBy(RelationInfoBean::getId));
        if (relationIdlIst.size() <= 0) {
            return;
        }
        for (int i = 0; i < relationIdlIst.size(); i++) {
            String id = relationIdlIst.get(i);
            List<RelationInfoBean> edgeObjAttrList = edgeObjAttrMap.get(id);
            List<RelationInfoBean> edgeNumAttrList = edgeNumAttrMap.get(id);
            List<BasicRelationRsp.EdgeDataInfo> edgeNumInfos = flatList(toListNoNull(edgeNumAttrList, a -> toListNoNull(a.getKvs(), ExploreReqConverter::kvBeanToEdgeDataInfo)));
            List<BasicRelationRsp.EdgeObjectInfo> edgeObjInfos = flatList(toListNoNull(edgeObjAttrList, a -> toListNoNull(a.getKvs(), ExploreReqConverter::kvBeanToEdgeObjInfo)));
            if (id.equals(relationBean.getId())) {
                main.setDataValAttrs(edgeNumInfos);
                main.setObjAttrs(edgeObjInfos);
            } else {
                GraphRelationRsp source = new GraphRelationRsp();
                BeanUtils.copyProperties(main, source);
                source.setId(id);
                List<String> startTime = relationBean.getStartTime();
                List<String> endTime = relationBean.getEndTime();
                try {
                    source.setStartTime(startTime.get(i));
                    source.setEndTime(endTime.get(i));
                } catch (Exception e) {
                    log.info("该exception忽略");
                }
                source.setDataValAttrs(edgeNumInfos);
                source.setObjAttrs(edgeObjInfos);
                sourceRelationList.add(source);
            }
        }
        consumerIfNoNull(sourceRelationList, main::setSourceRelationList);
    }


    /**
     * 边数值属性转换 old->new
     *
     * @param kvBean k 边属性名称，v 边属性值
     * @return EdgeDataInfo
     */
    private static BasicRelationRsp.EdgeDataInfo kvBeanToEdgeDataInfo(KVBean<String, String> kvBean) {
        BasicRelationRsp.EdgeDataInfo edgeObjectInfo = new BasicRelationRsp.EdgeDataInfo();
        edgeObjectInfo.setDataType(kvBean.getType());
        edgeObjectInfo.setName(kvBean.getK());
        edgeObjectInfo.setValue(kvBean.getV());
        return edgeObjectInfo;
    }

    /**
     * 边对象属性转换 old->new
     *
     * @param kvBean k 边对象属性名称，v 边对象对应实体名称
     * @return EdgeObjectInfo
     */
    private static BasicRelationRsp.EdgeObjectInfo kvBeanToEdgeObjInfo(KVBean<String, String> kvBean) {
        BasicRelationRsp.EdgeObjectInfo edgeObjectInfo = new BasicRelationRsp.EdgeObjectInfo();
        edgeObjectInfo.setDataType(kvBean.getType());
        edgeObjectInfo.setName(kvBean.getK());
        edgeObjectInfo.setEntityName(kvBean.getV());
        return edgeObjectInfo;
    }

    private static <T extends CommonEntityRsp> T entityBeanToCommonEntityRsp(EntityBean entityBean, T entityRsp) {
        entityRsp.setId(entityBean.getId());
        entityRsp.setName(entityBean.getName());
        entityRsp.setImgUrl(entityBean.getImg());
        consumerIfNoNull(entityBean.getGis(), a -> {
            entityRsp.setOpenGis(a.getIsOpenGis());
            entityRsp.setLat(a.getLat());
            entityRsp.setLng(a.getLng());
            entityRsp.setAddress(a.getAddress());
        });
        entityRsp.setConceptId(entityBean.getConceptId());
        entityRsp.setConceptIdList(entityBean.getConceptIdList());
        entityRsp.setConceptName(entityBean.getConceptName());
        entityRsp.setCoordinates(new CoordinateReq());
        entityRsp.setCreationTime(entityBean.getCreationTime());
        entityRsp.setNodeStyle(entityBean.getNodeStyle());
        entityRsp.setLabelStyle(entityBean.getLabelStyle());
        entityRsp.setScore(entityBean.getScore());
        entityRsp.setType(entityBean.getType());
        entityRsp.setClassId(entityBean.getClassId());
        entityRsp.setTags(toListNoNull(entityBean.getTags(), ExploreReqConverter::tagToTagRsp));
        consumerIfNoNull(entityBean.getCreationTime(), a -> entityRsp.setEndTime(BasicConverter.stringToDate(a)));
        consumerIfNoNull(entityBean.getFromTime(), a -> entityRsp.setStartTime(BasicConverter.stringToDate(a)));
        consumerIfNoNull(entityBean.getAdditionalInfo(), a -> entityRsp.setOpenGis(a.getIsOpenGis()));
        return entityRsp;
    }


    private static TagRsp tagToTagRsp(@NonNull Tag tag) {
        TagRsp tagRsp = new TagRsp();
        tagRsp.setCreationTime(tag.getCreationTime());
        tagRsp.setGrade(tag.getGrade());
        tagRsp.setName(tag.getName());
        tagRsp.setSource(tag.getSource());
        return tagRsp;
    }

}
