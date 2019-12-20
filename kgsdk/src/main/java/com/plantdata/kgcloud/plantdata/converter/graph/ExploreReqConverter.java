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
import com.plantdata.kgcloud.plantdata.req.explore.AbstrackGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.AttrScreeningBean;
import com.plantdata.kgcloud.plantdata.req.explore.GeneralGraphParameter;
import com.plantdata.kgcloud.plantdata.req.explore.GraphBean;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.AttrSortReq;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CoordinateReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import lombok.NonNull;
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
public class ExploreReqConverter extends BasicConverter {
    /**
     * 普通图探索转换
     *
     * @param graphParameter GeneralGraphParameter
     * @return CommonExploreReq
     */
    public static CommonExploreReq generalGraphParameterToCommonExploreReq(GeneralGraphParameter graphParameter) {
        CommonExploreReq exploreRsp = abstractGraphParameterToBasicGraphExploreReq(graphParameter, new CommonExploreReq());

        CommonFiltersReq commonFiltersReq = generalGraphParameterToCommonFiltersReq(graphParameter);
        PageReq pageReq = new PageReq(graphParameter.getPageNo(), graphParameter.getPageSize());
        exploreRsp.setCommon(commonFiltersReq);
        exploreRsp.setPage(pageReq);
        ///300新增exploreRsp.setDisAllowConcepts(Collections.emptyList());
        BasicGraphExploreRsp graphExploreRsp = executeIfNoNull(graphParameter.getGraphBean(), ExploreReqConverter::graphBeanToBasicGraphExploreRsp);
        exploreRsp.setGraphReq(graphExploreRsp);
        return exploreRsp;
    }

    private static CommonFiltersReq generalGraphParameterToCommonFiltersReq(GeneralGraphParameter graphParameter) {
        CommonFiltersReq commonFiltersReq = new CommonFiltersReq();
        commonFiltersReq.setHyponymyDistance(graphParameter.getHyponymyDistance());
        commonFiltersReq.setId(graphParameter.getId());
        commonFiltersReq.setKw(graphParameter.getKw());
        commonFiltersReq.setHighLevelSize(graphParameter.getHighLevelSize());
        commonFiltersReq.setPrivateAttRead(graphParameter.getPrivateAttRead());
        commonFiltersReq.setDirection(graphParameter.getDirection());
        commonFiltersReq.setEdgeAttrSorts(listToRsp(graphParameter.getAttSorts(), ExploreReqConverter::attrSortBeanToAttrSortReq));
        return commonFiltersReq;
    }

    private static <T extends AbstrackGraphParameter, R extends BasicGraphExploreReq> R abstractGraphParameterToBasicGraphExploreReq(T to, R rs) {

        rs.setReplaceClassIds(to.getReplaceClassIds());
        rs.setReplaceClassKeys(to.getReplaceClassIdsKey());
        rs.setAllowConceptsKey(to.getAllowTypesKey());
        rs.setAllowConcepts(to.getAllowTypes());
        rs.setAllowAttrsKey(to.getAllowAttsKey());
        rs.setAllowAttrs(to.getAllowAtts());
        rs.setAllowAttrGroups(listToRsp(to.getAllowAttrGroups(), Long::valueOf));
        rs.setDistance(to.getDistance());
        rs.setEdgeAttrFilters(listToRsp(to.getAttAttFilters(), ExploreReqConverter::attrScreeningBeanToRelationAttrReq));

        return rs;
    }

    private static RelationAttrReq attrScreeningBeanToRelationAttrReq(@NonNull AttrScreeningBean screeningBean) {
        return LinkUtil.link(screeningBean);
    }

    private static AttrSortReq attrSortBeanToAttrSortReq(@NonNull AttrSortBean attrSortBean) {
        return LinkUtil.link(attrSortBean);
    }

    private static BasicGraphExploreRsp graphBeanToBasicGraphExploreRsp(GraphBean graphBean) {
        BasicGraphExploreRsp graphExploreRsp = new BasicGraphExploreRsp();
        graphExploreRsp.setEntityList(listToRsp(graphBean.getEntityList(), ExploreReqConverter::entityBeanToCommonEntityRsp));
        graphExploreRsp.setRelationList(listToRsp(graphBean.getRelationList(), ExploreReqConverter::relationBeanToGraphRelationRsp));
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
            List<BasicRelationRsp.EdgeInfo> edgeNumInfos = flatList(listToRsp(edgeNumAttrList, ExploreReqConverter::relationInfoBeanToEdgeInfo));
            List<BasicRelationRsp.EdgeInfo> edgeObjInfos = flatList(listToRsp(edgeObjAttrList, ExploreReqConverter::relationInfoBeanToEdgeInfo));
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
        return listToRsp(relationInfo.getKvs(), ExploreReqConverter::kVBeanToEdgeInfo);
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
        entityRsp.setEndTime(stringToDate(entityBean.getCreationTime()));
        entityRsp.setStartTime(stringToDate(entityBean.getFromTime()));
        entityRsp.setNodeStyle(entityBean.getNodeStyle());
        entityRsp.setLabelStyle(entityBean.getLabelStyle());
        entityRsp.setScore(entityBean.getScore());
        entityRsp.setType(entityBean.getType());
        entityRsp.setTags(listToRsp(entityBean.getTags(), ExploreReqConverter::tagToTagRsp));
        if (entityBean.getAdditionalInfo() != null) {
            entityRsp.setOpenGis(entityBean.getAdditionalInfo().getIsOpenGis());
        }
        return entityRsp;
    }

    private static TagRsp tagToTagRsp(Tag tag) {
        return LinkUtil.link(tag);
    }
}
