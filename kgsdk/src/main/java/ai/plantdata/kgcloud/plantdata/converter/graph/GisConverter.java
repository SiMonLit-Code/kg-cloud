package ai.plantdata.kgcloud.plantdata.converter.graph;

import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.constant.DirectionEnum;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.req.common.GisBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import ai.plantdata.kgcloud.plantdata.req.explore.gis.GraphLocusGisParameter;
import ai.plantdata.kgcloud.plantdata.req.explore.gis.GraphRectangleParameter;
import ai.plantdata.kgcloud.plantdata.rsp.explore.gis.GisLocusOldRsp;
import ai.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import ai.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import ai.plantdata.kgcloud.sdk.req.app.PageReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import ai.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

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
        exploreReq.setAttrId(param.getAttrId());
        consumerIfNoNull(param.getDirection(), a -> exploreReq.setDirection(NumberUtils.INTEGER_TWO.equals(a) ? DirectionEnum.BACKWARD.getValue() : a));
        consumerIfNoNull(param.getGisFilters(), a -> exploreReq.setGisFilters(JsonUtils.jsonToList(a, Object.class)));
        consumerIfNoNull(param.getFilterType(), exploreReq::setFilterType);
        exploreReq.setPage(new PageReq(param.getPageNo(), param.getPageSize()));
        return exploreReq;
    }

    public static GraphBean gisGraphExploreRspToGraphBean(@NonNull GisGraphExploreRsp exploreRsp) {
        List<EntityBean> entityBeans = toListNoNull(exploreRsp.getEntityList(), GisConverter::gisGraphExploreRspToEntityBean);
        GraphBean graphBean = new GraphBean();
        graphBean.setEntityList(entityBeans);
        graphBean.setLevel1HasNextPage(exploreRsp.getHasNextPage());
        graphBean.setRelationList(Collections.emptyList());
        return graphBean;
    }

    private static EntityBean gisGraphExploreRspToEntityBean(@NonNull GisEntityRsp gisEntityRsp) {
        EntityBean entityBean = ExploreCommonConverter.entityBeanToGraphEntityRsp(gisEntityRsp);
        GisBean gisBean = new GisBean();
        gisBean.setIsOpenGis(gisEntityRsp.getOpenGis());
        consumerIfNoNull(gisEntityRsp.getGis(), gis -> {
            gisBean.setAddress(gis.getAddress());
            gisBean.setLat(gis.getLat());
            gisBean.setLng(gis.getLng());
            entityBean.setGis(gisBean);
        });
        return entityBean;
    }

    public static GisLocusOldRsp gisGraphExploreRspToGisLocusRsp(@NonNull GisLocusAnalysisRsp exploreRsp) {
        GisLocusOldRsp locusRsp = new GisLocusOldRsp();
        List<EntityBean> entityBeans = toListNoNull(exploreRsp.getEntityList(), GisConverter::gisGraphExploreRspToEntityBean);
        locusRsp.setRelationList(toListNoNull(exploreRsp.getRelationList(), GisConverter::gisLocusRelationVOToGisLocusRelationRsp));
        locusRsp.setEntityList(entityBeans);
        locusRsp.setLevel1HasNextPage(exploreRsp.getHasNextPage());
        return locusRsp;
    }

    private static GisLocusOldRsp.GisLocusRelationRsp gisLocusRelationVOToGisLocusRelationRsp(@NonNull GisRelationRsp relationRsp) {
        GisLocusOldRsp.GisLocusRelationRsp locusRelationRsp = new GisLocusOldRsp.GisLocusRelationRsp();
        consumerIfNoNull(relationRsp.getAttId(), a -> locusRelationRsp.setAttId(a.longValue()));
        locusRelationRsp.setAttName(relationRsp.getAttName());
        locusRelationRsp.setId(relationRsp.getId());
        locusRelationRsp.setFrom(relationRsp.getFrom());
        locusRelationRsp.setStartTime(Lists.newArrayList(relationRsp.getStartTime()));
        locusRelationRsp.setEndTime(Lists.newArrayList(relationRsp.getEndTime()));
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
