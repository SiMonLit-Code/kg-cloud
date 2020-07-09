package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kg.api.pub.req.SemanticDistanceFrom;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 15:54
 */
public class DistanceConverter {

    public static SemanticDistanceFrom distanceListReqToSemanticDistanceFrom(DistanceListReq listReq) {
        SemanticDistanceFrom distanceFrom = new SemanticDistanceFrom();
        distanceFrom.setIds(listReq.getIds());
        BaseReq page = listReq.getPage();
        if (page != null) {
            distanceFrom.setLimit(page.getLimit());
            distanceFrom.setSkip(page.getOffset());
        }
        return distanceFrom;
    }
}
