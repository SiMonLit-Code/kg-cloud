package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.api.pub.req.GisFrom;
import ai.plantdata.kg.api.pub.req.GisLocusParam;
import ai.plantdata.kg.api.pub.resp.GisLocusVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.sdk.constant.GisFilterTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.PageReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 9:34
 */
@Slf4j
public class GisConverter extends BasicConverter {

    private static final int GIS_FILTERS_LENGTH = 2;
    private static final List<Integer> DEFAULT_GIS_A = Lists.newArrayList(-180, 90);
    private static final List<Integer> DEFAULT_GIS_B = Lists.newArrayList(180, -90);
    private static final Double GIS_CONSTANT = 6378.1;

    public static GisFrom reqToGisFrom(GisGraphExploreReq exploreReq) {
        GisFrom gisFrom = new GisFrom();
        if (CollectionUtils.isEmpty(exploreReq.getGisFilters())) {
            exploreReq.setFilterType("$box");
            exploreReq.setGisFilters(Lists.newArrayList(DEFAULT_GIS_A, DEFAULT_GIS_B));
        }
        PageReq page = exploreReq.getPage();
        if (page == null) {
            gisFrom.setSkip(NumberUtils.INTEGER_ZERO);
            gisFrom.setLimit(BaseReq.DEFAULT_SIZE);
        } else {
            gisFrom.setSkip(page.getOffset());
            gisFrom.setLimit(page.getLimit());
        }
        gisFrom.setAttrId(exploreReq.getAttrId());
        gisFrom.setDirection(exploreReq.getDirection());
        gisFrom.setGisConceptIds(exploreReq.getConceptIds());
        gisFrom.setGisFilter(buildSearchMap(exploreReq.getFilterType(), exploreReq.getGisFilters()));
        gisFrom.setInherit(exploreReq.getIsInherit());
        log.error("gisFrom:{}", JsonUtils.objToJson(gisFrom));
        return gisFrom;
    }

    public static GisLocusParam reqToParam(GisLocusReq req) {
        PageReq page = req.getPage();
        GisLocusParam gisLocusParam = new GisLocusParam();
        if (page == null) {
            gisLocusParam.setSkip(NumberUtils.INTEGER_ZERO);
            gisLocusParam.setLimit(BaseReq.DEFAULT_SIZE);
        } else {
            gisLocusParam.setSkip(page.getOffset());
            gisLocusParam.setLimit(page.getLimit());
        }
        gisLocusParam.setFromTime(req.getFromTime());
        gisLocusParam.setToTime(req.getToTime());
        consumerIfNoNull(req.getFilterType(), a -> gisLocusParam.setGisFilters(buildSearchMap(a, req.getGisFilters())));
        gisLocusParam.setRules(listConvert(req.getRules(), GisConverter::gisRuleParamToGisLocusRulesParam));
        return gisLocusParam;
    }

    public static Map<String, Object> buildSearchMap(String filterType, List<Object> list) {
        Optional<GisFilterTypeEnum> enumObject = EnumUtils.getEnumObject(GisFilterTypeEnum.class, filterType);
        if (!enumObject.isPresent()) {
            log.error("gisFilterType:{}", filterType);
            throw BizException.of(AppErrorCodeEnum.GIS_TYPE_ERROR);
        }
        GisFilterTypeEnum typeEnum = enumObject.get();
        if (list.size() != GIS_FILTERS_LENGTH || ((List) list.get(0)).size() != GIS_FILTERS_LENGTH) {
            throw BizException.of(AppErrorCodeEnum.GIS_INFO_ERROR);
        }
        //box 校验
        if (GisFilterTypeEnum.BOX.equals(typeEnum)) {
            List paramOne = (List) list.get(1);
            if (paramOne.size() != GIS_FILTERS_LENGTH) {
                throw BizException.of(AppErrorCodeEnum.GIS_INFO_ERROR);
            }
        }
        if (GisFilterTypeEnum.CENTER_SPHERE.equals(typeEnum)) {
            Double paramTwo = Double.parseDouble(list.get(1).toString());
            list.remove(1);
            list.add(paramTwo / GIS_CONSTANT);
        }
        Map<String, Object> filtersMap = Maps.newHashMap();
        filtersMap.put(typeEnum.getValue(), list);
        return filtersMap;
    }


    private static GisLocusParam.GisLocusRulesParam gisRuleParamToGisLocusRulesParam(GisLocusReq.GisRuleParam gisRuleParam) {
        GisLocusParam.GisLocusRulesParam rulesParam = new GisLocusParam.GisLocusRulesParam();
        rulesParam.setIds(gisRuleParam.getIds());
        rulesParam.setKql(gisRuleParam.getKql());
        rulesParam.setRuleId(gisRuleParam.getRuleId());
        return rulesParam;
    }


    public static GisGraphExploreRsp voToGisAnalysisRsp(@NonNull List<BasicInfo> entityList, Map<Long, BasicInfo> conceptIdMap) {
        GisGraphExploreRsp exploreRsp = new GisGraphExploreRsp();
        exploreRsp.setEntityList(DefaultUtils.executeIfNoNull(entityList, a -> EntityConverter.basicInfoToGisEntity(a, conceptIdMap)));
        return exploreRsp;
    }

    public static GisLocusAnalysisRsp voToGisLocusRsp(GisLocusVO gisLocusVO) {
        GisLocusAnalysisRsp exploreRsp = new GisLocusAnalysisRsp();
        exploreRsp.setEntityList(CollectionUtils.isEmpty(gisLocusVO.getEntityList()) ? Collections.emptyList()
                : EntityConverter.voToGisRsp(gisLocusVO.getEntityList()));
        List<GisRelationRsp> relationRspList = listConvert(gisLocusVO.getRelationList(), a -> RelationConverter.voToGisRsp(a, gisLocusVO.getRuleRelationMap()));

        exploreRsp.setRelationList(relationRspList);
        exploreRsp.setHasNextPage(gisLocusVO.getHasNextPage());

        return exploreRsp;
    }

    public static void check(GisLocusReq req) {
        for (GisLocusReq.GisRuleParam rule : req.getRules()) {
            if (rule.getRuleId() == null) {
                throw new BizException(AppErrorCodeEnum.NULL_GIS_RULE_ID);
            }
            if (CollectionUtils.isEmpty(rule.getIds())) {
                throw new BizException(AppErrorCodeEnum.NULL_GIS_ENTITY_ID);
            }
            if (StringUtils.isEmpty(rule.getKql())) {
                throw new BizException(AppErrorCodeEnum.NULL_GIS_KG_QL);
            }
        }
    }
}
