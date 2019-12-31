package com.plantdata.kgcloud.plantdata.converter.graph;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import com.plantdata.kgcloud.plantdata.req.explore.gis.GraphLocusGisParameter;
import com.plantdata.kgcloud.plantdata.req.explore.gis.GraphRectangleParameter;
import com.plantdata.kgcloud.plantdata.rsp.explore.gis.GisLocusOldRsp;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import com.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 17:36
 */
public class GisConverter extends BasicConverter {

    public static GisGraphExploreReq graphRectangleParameterToGisGraphExploreReq(@NonNull GraphRectangleParameter param) {
        GisGraphExploreReq exploreReq = new GisGraphExploreReq();
        exploreReq.setFilterType(param.getFilterType());
        exploreReq.setConceptIds(param.getAllowTypes());
        exploreReq.setConceptKeys(param.getAllowTypesKey());
        exploreReq.setFromTime(param.getFromTime());
        exploreReq.setToTime(param.getToTime());
        consumerIfNoNull(param.getGisFilters(), a -> exploreReq.setGisFilters(JsonUtils.jsonToList(a, Object.class)));
        consumerIfNoNull(param.getFilterType(), exploreReq::setFilterType);
        exploreReq.setPage(new PageReq(param.getPageNo(), param.getPageSize()));
        return exploreReq;
    }

    public static GraphBean gisGraphExploreRspToGraphBean(@NonNull GisGraphExploreRsp exploreRsp) {
        List<EntityBean> entityBeans = toListNoNull(exploreRsp.getEntityList(), ExploreCommonConverter::entityBeanToGraphEntityRsp);
        GraphBean graphBean = new GraphBean();
        graphBean.setEntityList(entityBeans);
        graphBean.setLevel1HasNextPage(exploreRsp.getHasNextPage());
        graphBean.setRelationList(Collections.emptyList());
        return graphBean;
    }

    public static GisLocusOldRsp gisGraphExploreRspToGisLocusRsp(@NonNull GisLocusAnalysisRsp exploreRsp) {
        GisLocusOldRsp locusRsp = new GisLocusOldRsp();
        List<EntityBean> entityBeans = toListNoNull(exploreRsp.getEntityList(), ExploreCommonConverter::entityBeanToGraphEntityRsp);
        locusRsp.setRelationList(toListNoNull(exploreRsp.getRelationList(), GisConverter::gisLocusRelationVOToGisLocusRelationRsp));
        locusRsp.setEntityList(entityBeans);
        locusRsp.setLevel1HasNextPage(exploreRsp.getHasNextPage());
        return locusRsp;
    }

    private static GisLocusOldRsp.GisLocusRelationRsp gisLocusRelationVOToGisLocusRelationRsp(@NonNull GisRelationRsp relationRsp) {
        GisLocusOldRsp.GisLocusRelationRsp locusRelationRsp = new GisLocusOldRsp.GisLocusRelationRsp();
        locusRelationRsp.setAttId(relationRsp.getAttId());
        locusRelationRsp.setAttName(relationRsp.getAttName());
        locusRelationRsp.setId(relationRsp.getId());
        locusRelationRsp.setFrom(relationRsp.getFrom());
        locusRelationRsp.setTo(relationRsp.getTo());
        locusRelationRsp.setRuleId(relationRsp.getRuleId());
        locusRelationRsp.setLabelStyle(relationRsp.getLabelStyle());
        locusRelationRsp.setLinkStyle(relationRsp.getLinkStyle());
        return locusRelationRsp;
    }

    public static GisLocusReq graphLocusGisParameterToGisLocusReq(@NonNull GraphLocusGisParameter param) {
        GisLocusReq locusReq = new GisLocusReq();
        locusReq.setRules(toListNoNull(param.getRules(), GisConverter::gisRuleParamToGisLocusReq));
        locusReq.setTimeFilterType(param.getTimeFilterType());
        locusReq.setFromTime(param.getFromTime());
        locusReq.setToTime(param.getToTime());
        consumerIfNoNull(param.getGisFilters(), a -> locusReq.setGisFilters(JsonUtils.jsonToList(param.getGisFilters(), Object.class)));
        return locusReq;
    }

    private static GisLocusReq.GisRuleParam gisRuleParamToGisLocusReq(@NonNull GraphLocusGisParameter.GisRuleParam param) {
        GisLocusReq.GisRuleParam ruleParam = new GisLocusReq.GisRuleParam();
        ruleParam.setRuleId(param.getRuleId());
        ruleParam.setKql(param.getKql());
        ruleParam.setIds(param.getIds());
        return ruleParam;
    }
}
