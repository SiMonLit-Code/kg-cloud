package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.GisFrom;
import ai.plantdata.kg.api.pub.req.GisLocusParam;
import ai.plantdata.kg.api.pub.resp.GisLocusVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 9:34
 */
public class GisConverter {

    public static GisFrom reqToGisFrom(GisGraphExploreReq exploreReq){
        GisFrom gisFrom = new GisFrom();
        gisFrom.setAttrId(exploreReq.getAttrId());
        gisFrom.setDirection(exploreReq.getDirection());
        gisFrom.setGisConceptIds(exploreReq.getConceptIds());
        gisFrom.setGisFilter(exploreReq.getGisFilters());
        gisFrom.setInherit(exploreReq.getIsInherit());
        BaseReq page = exploreReq.getPage();
        if(null!=page){
            gisFrom.setSkip(page.getOffset());
            gisFrom.setLimit(page.getLimit());
        }
        return gisFrom;
    }

    public static GisLocusParam reqToParam(GisLocusReq req) {
        BaseReq page = req.getPage();
        GisLocusParam gisLocusParam = new GisLocusParam();
        gisLocusParam.setFromTime(req.getFromTime());
        gisLocusParam.setToTime(req.getToTime());
        gisLocusParam.setGisFilters(req.getGisFilters());
        gisLocusParam.setPos(page.getPage());
        gisLocusParam.setSize(page.getSize());
        return gisLocusParam;
    }

    public static GisGraphExploreRsp voToGisAnalysisRsp(@NonNull List<BasicInfo> entityList){
        GisGraphExploreRsp exploreRsp = new GisGraphExploreRsp();
        //exploreRsp.setEntityList(DefaultUtils.getIfNoNull(EntityConverter.voToGisRsp(entityList)));
        return exploreRsp;
    }

    public static GisLocusAnalysisRsp voToGisLocusRsp(GisLocusVO gisLocusVO) {
        GisLocusAnalysisRsp exploreRsp = new GisLocusAnalysisRsp();
        exploreRsp.setEntityList(CollectionUtils.isEmpty(gisLocusVO.getEntityList()) ? Collections.emptyList()
                : EntityConverter.voToGisRsp(gisLocusVO.getEntityList()));
        List<GisRelationRsp> relationRspList = CollectionUtils.isEmpty(gisLocusVO.getRelationList()) ? Collections.emptyList()
                : RelationConverter.voToGisRsp(gisLocusVO.getRelationList(), gisLocusVO.getRuleRelationMap());
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
