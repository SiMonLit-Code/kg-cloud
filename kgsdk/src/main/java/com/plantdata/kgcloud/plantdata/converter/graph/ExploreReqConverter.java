package com.plantdata.kgcloud.plantdata.converter.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.link.LinkUtil;
import com.plantdata.kgcloud.plantdata.req.common.AttrSortBean;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationBean;
import com.plantdata.kgcloud.plantdata.req.common.RelationInfoBean;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.explore.RuleGeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.TimeGeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import com.plantdata.kgcloud.plantdata.req.explore.path.PathGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.path.TimePathGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.relation.RelationGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.relation.RuleRelationGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.relation.TimeRelationGraphParameter;
import com.plantdata.kgcloud.sdk.req.app.AttrSortReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CoordinateReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static CommonExploreReq generalGraphParameterToCommonExploreReq(GeneralGraphParameter param) {
        CommonExploreReq exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonExploreReq());
        PageReq pageReq = new PageReq(param.getPageNo(), param.getPageSize());
        exploreReq.setCommon(generalGraphParameterToCommonFiltersReq(param));
        exploreReq.setPage(pageReq);
        ///300新增exploreRsp.setDisAllowConcepts(Collections.emptyList());
        consumerIfNoNull(param.getGraphBean(), a -> exploreReq.setGraphReq(ExploreReqConverter.graphBeanToBasicGraphExploreRsp(a)));
        return exploreReq;
    }

    /**
     * 时序图探索
     *
     * @param param TimeGeneralGraphParameter
     * @return CommonTimingExploreReq
     */
    public static CommonTimingExploreReq timeGeneralGraphParameterToCommonTimingExploreReq(TimeGeneralGraphParameter param) {
        CommonTimingExploreReq exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonTimingExploreReq());
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
    public static CommonReasoningExploreReq ruleGeneralGraphParameterToCommonReasoningExploreReq(RuleGeneralGraphParameter param) {
        CommonReasoningExploreReq exploreReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new CommonReasoningExploreReq());
        exploreReq.setCommon(generalGraphParameterToCommonFiltersReq(param));
        consumerIfNoNull(param.getReasoningRuleConfigs(), a -> exploreReq.setReasoningRuleConfigs(ExploreCommonConverter.buildReasonConfig(a)));
        return exploreReq;
    }

    /**
     * 关联分析转换
     *
     * @param param RelationGraphParameter
     * @return RelationReqAnalysisReq
     */
    public static RelationReqAnalysisReq generalGraphParameterToRelationReqAnalysisReq(RelationGraphParameter param) {
        RelationReqAnalysisReq pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationReqAnalysisReq());
        pathAnalysisReq.setRelation(ExploreCommonConverter.buildRelationReq(param));
        consumerIfNoNull(param.getStatsConfig(), a -> pathAnalysisReq.setConfigList(toListNoNull(a, ExploreCommonConverter::graphStatBeanToBasicStatisticReq)));
        return pathAnalysisReq;
    }

    /**
     * 关联分析转换
     *
     * @param param TimeRelationGraphParameter
     * @return RelationTimingAnalysisReq
     */
    public static RelationTimingAnalysisReq timeRelationGraphParameterToRelationTimingAnalysisReq(TimeRelationGraphParameter param) {
        RelationTimingAnalysisReq pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationTimingAnalysisReq());
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
    public static RelationReasoningAnalysisReq ruleRelationGraphParameterToRelationReasoningAnalysisReq(RuleRelationGraphParameter param) {
        RelationReasoningAnalysisReq pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(param, new RelationReasoningAnalysisReq());
        pathAnalysisReq.setRelation(ExploreCommonConverter.buildRelationReq(param));
        consumerIfNoNull(param.getStatsConfig(), a -> pathAnalysisReq.setConfigList(toListNoNull(a, ExploreCommonConverter::graphStatBeanToBasicStatisticReq)));
        consumerIfNoNull(param.getReasoningRuleConfigs(), a -> pathAnalysisReq.setReasoningRuleConfigs(ExploreCommonConverter.buildReasonConfig(a)));
        return pathAnalysisReq;
    }

    /**
     * 路径发现
     *
     * @param graphParam PathGraphParameter
     * @return PathAnalysisReq
     */
    public static PathAnalysisReq pathGraphParameterToPathAnalysisReq(PathGraphParameter graphParam) {
        PathAnalysisReq pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(graphParam, new PathAnalysisReq());
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
    public static PathTimingAnalysisReq timePathGraphParameterToPathTimingAnalysisReq(TimePathGraphParameter graphParam) {
        PathTimingAnalysisReq pathAnalysisReq = ExploreCommonConverter.abstractGraphParameterToBasicGraphExploreReq(graphParam, new PathTimingAnalysisReq());
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
        consumerIfNoNull(graphParameter.getDirection(), commonFiltersReq::setDirection);
        consumerIfNoNull(graphParameter.getAttSorts(), a -> commonFiltersReq.setEdgeAttrSorts(toListNoNull(a, ExploreReqConverter::attrSortBeanToAttrSortReq)));
        return commonFiltersReq;
    }

    private static AttrSortReq attrSortBeanToAttrSortReq(@NonNull AttrSortBean attrSortBean) {
        return LinkUtil.link(attrSortBean);
    }

    private static BasicGraphExploreRsp graphBeanToBasicGraphExploreRsp(GraphBean graphBean) {
        BasicGraphExploreRsp graphExploreRsp = new BasicGraphExploreRsp();
        graphExploreRsp.setEntityList(toListNoNull(graphBean.getEntityList(), ExploreReqConverter::entityBeanToCommonEntityRsp));
        graphExploreRsp.setRelationList(toListNoNull(graphBean.getRelationList(), ExploreReqConverter::relationBeanToGraphRelationRsp));
        graphExploreRsp.setHasNextPage(graphBean.getLevel1HasNextPage());
        return graphExploreRsp;
    }

    private static GraphRelationRsp relationBeanToGraphRelationRsp(RelationBean relationBean) {
        GraphRelationRsp link = LinkUtil.link(relationBean);
        List<GraphRelationRsp> sourceRelationList = Lists.newArrayList();
        Set<String> relationIdlIst = Sets.newHashSet();
        Map<String, List<RelationInfoBean>> edgeObjAttrMap = relationBean.getoRInfo().stream().peek(a -> relationIdlIst.add(a.getId())).collect(Collectors.groupingBy(RelationInfoBean::getId));
        Map<String, List<RelationInfoBean>> edgeNumAttrMap = relationBean.getnRInfo().stream().peek(a -> relationIdlIst.add(a.getId())).collect(Collectors.groupingBy(RelationInfoBean::getId));
        relationIdlIst.forEach(id -> {
            List<RelationInfoBean> edgeObjAttrList = edgeObjAttrMap.get(id);
            List<RelationInfoBean> edgeNumAttrList = edgeNumAttrMap.get(id);
            List<BasicRelationRsp.EdgeInfo> edgeNumInfos = flatList(toListNoNull(edgeNumAttrList, ExploreReqConverter::relationInfoBeanToEdgeInfo));
            List<BasicRelationRsp.EdgeInfo> edgeObjInfos = flatList(toListNoNull(edgeObjAttrList, ExploreReqConverter::relationInfoBeanToEdgeInfo));
            if (id.equals(relationBean.getId())) {
                link.setDataValAttrs(edgeNumInfos);
                link.setObjAttrs(edgeObjInfos);
            } else {
                GraphRelationRsp source = new GraphRelationRsp();
                BeanUtils.copyProperties(link, source);
                source.setId(id);
                source.setDataValAttrs(edgeNumInfos);
                source.setObjAttrs(edgeObjInfos);
                sourceRelationList.add(source);
            }
        });
        if (!CollectionUtils.isEmpty(relationBean.getStartTime())) {
            link.setStartTime(relationBean.getStartTime().get(0));
        }
        if (!CollectionUtils.isEmpty(relationBean.getEndTime())) {
            link.setEndTime(relationBean.getEndTime().get(0));
        }
        return link;
    }

    private static List<BasicRelationRsp.EdgeInfo> relationInfoBeanToEdgeInfo(RelationInfoBean relationInfo) {
        return toListNoNull(relationInfo.getKvs(), ExploreReqConverter::kVBeanToEdgeInfo);
    }

    private static BasicRelationRsp.EdgeInfo kVBeanToEdgeInfo(KVBean<String, String> kvBean) {
        BasicRelationRsp.EdgeInfo edgeInfo = new BasicRelationRsp.EdgeInfo();
        edgeInfo.setDataType(kvBean.getType());
        edgeInfo.setName(kvBean.getK());
        edgeInfo.setValue(kvBean.getV());
        return edgeInfo;
    }

    private static CommonEntityRsp entityBeanToCommonEntityRsp(EntityBean entityBean) {
        CommonEntityRsp entityRsp = new CommonEntityRsp();
        entityRsp.setConceptId(entityBean.getConceptId());
        entityRsp.setConceptIdList(entityBean.getConceptIdList());
        entityRsp.setConceptName(entityBean.getConceptName());
        entityRsp.setCoordinates(new CoordinateReq());
        entityRsp.setCreationTime(entityBean.getCreationTime());
        entityRsp.setNodeStyle(entityBean.getNodeStyle());
        entityRsp.setLabelStyle(entityBean.getLabelStyle());
        entityRsp.setScore(entityBean.getScore());
        entityRsp.setType(entityBean.getType());
        entityRsp.setTags(toListNoNull(entityBean.getTags(), ExploreReqConverter::tagToTagRsp));
        consumerIfNoNull(entityBean.getCreationTime(), a -> entityRsp.setEndTime(BasicConverter.stringToDate(a)));
        consumerIfNoNull(entityBean.getFromTime(), a -> entityRsp.setStartTime(BasicConverter.stringToDate(a)));
        consumerIfNoNull(entityBean.getAdditionalInfo(), a -> entityRsp.setOpenGis(a.getIsOpenGis()));
        return entityRsp;
    }


    private static TagRsp tagToTagRsp(Tag tag) {
        return LinkUtil.link(tag);
    }

}
