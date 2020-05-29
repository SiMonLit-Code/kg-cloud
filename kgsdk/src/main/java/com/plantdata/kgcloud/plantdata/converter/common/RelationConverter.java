package com.plantdata.kgcloud.plantdata.converter.common;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.plantdata.bean.ImportRelationBean;
import com.plantdata.kgcloud.plantdata.constant.MetaDataEnum;
import com.plantdata.kgcloud.plantdata.req.data.QueryRelationParameter;
import com.plantdata.kgcloud.plantdata.req.explore.common.AttrScreeningBean;
import com.plantdata.kgcloud.plantdata.rsp.data.RelationBeanScore;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReqList;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.data.RelationUpdateReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.JsonUtils;
import com.plantdata.kgcloud.util.MetaDataUtils;
import lombok.NonNull;
import org.springframework.plugin.metadata.MetadataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 11:30
 */
public class RelationConverter extends BasicConverter {

    public static BatchRelationRsp  importRelationBeanToBatchRelationRsp(@NonNull ImportRelationBean relationBean) {
        BatchRelationRsp relationRsp = new BatchRelationRsp();
        relationRsp.setAttrId(relationBean.getAttrId());
        relationRsp.setAttrTimeFrom(relationBean.getAttrTimeFrom());
        relationRsp.setAttrTimeTo(relationBean.getAttrTimeTo());
        relationRsp.setAttrValueConcept(relationBean.getAttrValueConcept());
        relationRsp.setAttrValueId(relationBean.getAttrValueId());
        relationRsp.setAttrValueMeaningTag(relationBean.getEntityMeaningTag());
        relationRsp.setAttrValueName(relationBean.getAttrValueName());
        relationRsp.setEntityId(relationBean.getEntityId());
        relationRsp.setEntityName(relationBean.getEntityName());
        relationRsp.setEntityConcept(relationBean.getEntityConcept());
        relationRsp.setEntityMeaningTag(relationBean.getEntityMeaningTag());
        relationRsp.setMetaData(MetaDataUtils.getDefaultMetaData(relationBean.getMetaData()));

        consumerIfNoNull(relationBean.getExtraInfoMap(), a -> relationRsp.setExtraInfoMap(keyStringToInt(a)));
        return relationRsp;
    }


    public static ImportRelationBean batchRelationRspToBatchRelationRsp(@NonNull BatchRelationRsp relationRsp) {
        ImportRelationBean importRelationBean = new ImportRelationBean();
        importRelationBean.setTripleId(relationRsp.getId());
        importRelationBean.setEntityId(relationRsp.getEntityId());
        importRelationBean.setEntityConcept(relationRsp.getEntityConcept());
        importRelationBean.setEntityName(relationRsp.getEntityName());
        importRelationBean.setEntityMeaningTag(relationRsp.getEntityMeaningTag());
        BasicConverter.consumerIfNoNull(relationRsp.getExtraInfoMap(),
                a -> importRelationBean.setExtraInfoMap(keyIntToStr(a)));
        importRelationBean.setAttrTimeFrom(relationRsp.getAttrTimeFrom());
        importRelationBean.setAttrTimeTo(relationRsp.getAttrTimeTo());
        importRelationBean.setAttrValueId(relationRsp.getAttrValueId());
        importRelationBean.setAttrValueMeaningTag(relationRsp.getAttrValueMeaningTag());
        importRelationBean.setAttrValueName(relationRsp.getAttrValueName());
        importRelationBean.setAttrValueConcept(relationRsp.getAttrValueConcept());
        importRelationBean.setAttrId(relationRsp.getAttrId());
        importRelationBean.setNote(relationRsp.getNote());
        return importRelationBean;
    }

    public static OpenBatchResult<ImportRelationBean> batchRelationRspToOpenBatchResult(@NonNull OpenBatchResult<BatchRelationRsp> batchResult) {
        List<ImportRelationBean> success = toListNoNull(batchResult.getSuccess(), RelationConverter::batchRelationRspToBatchRelationRsp);
        List<ImportRelationBean> error = toListNoNull(batchResult.getError(), RelationConverter::batchRelationRspToBatchRelationRsp);
        return new OpenBatchResult<>(success, error);
    }


    public static RelationUpdateReq importRelationBeanToRelationUpdateReq(@NonNull ImportRelationBean relationBean) {
        RelationUpdateReq updateReq = new RelationUpdateReq();
        updateReq.setAttrId(relationBean.getAttrId());
        updateReq.setAttrTimeFrom(relationBean.getAttrTimeFrom());
        updateReq.setTripleId(relationBean.getTripleId());
        updateReq.setAttrTimeTo(relationBean.getAttrTimeTo());
        consumerIfNoNull(relationBean.getExtraInfoMap(), a -> {
            Map<Integer, String> map = Maps.newHashMap();
            a.forEach((k, v) -> map.put(Integer.valueOf(k), v.toString()));
            updateReq.setExtraInfoMap(map);
        });
        return updateReq;
    }

    public static EdgeSearchReqList queryRelationParameterToEdgeSearchReq(@NonNull QueryRelationParameter param) {
        EdgeSearchReqList searchReq = new EdgeSearchReqList();
        searchReq.setAllowAttrs(param.getAttrIds());
        searchReq.setAllowAttrsKey(param.getAttrKeys());
        searchReq.setDirection(param.getDirection());
        consumerIfNoNull(param.getAttrTimeFrom(), a -> searchReq.setAttrTimeFrom(JsonUtils.stringToMap(a)));
        consumerIfNoNull(param.getAttrTimeTo(), a -> searchReq.setAttrTimeTo(JsonUtils.stringToMap(a)));
        searchReq.setPage(param.getPageNo());
        searchReq.setSize(param.getPageSize());
        searchReq.setAttrValueIds(param.getAttrValueIds());
        consumerIfNoNull(param.getQuery(), a -> searchReq.setEdgeAttrQuery(toListNoNull(a, RelationConverter::attrScreeningBeanToRelationAttrReq)));
        searchReq.setEntityIds(param.getEntityIds());
        return searchReq;
    }

    public static RelationBeanScore edgeSearchRspToRelationBeanScore(@NonNull EdgeSearchRsp rsp) {
        RelationBeanScore beanScore = new RelationBeanScore();
        beanScore.setAttrId(rsp.getAttrId());
        beanScore.setTripleId(rsp.getTripleId());
        beanScore.setSource(rsp.getSource());
        beanScore.setScore(rsp.getScore());
        beanScore.setAttrTimeFrom(rsp.getAttrTimeFrom());
        beanScore.setAttrTimeTo(rsp.getAttrTimeTo());
        consumerIfNoNull(rsp.getFromEntity(), a -> {
            beanScore.setAttrTimeFrom(a.getId().toString());
            beanScore.setEntityConcept(a.getConceptId());
            beanScore.setEntityName(a.getName());
            beanScore.setEntityId(a.getId());
            beanScore.setEntityMeaningTag(a.getMeaningTag());
        });
        consumerIfNoNull(rsp.getToEntity(), a -> {
            beanScore.setAttrValueId(a.getId());
            beanScore.setAttrTimeTo(a.getId().toString());
            beanScore.setAttrValueConcept(a.getConceptId());
            beanScore.setAttrValueName(a.getName());
            beanScore.setAttrValueMeaningTag(a.getMeaningTag());
        });
        consumerIfNoNull(rsp.getExtraInfoMap(), a -> beanScore.setExtraInfoMap(keyIntToStr(a)));
        return beanScore;
    }


    private static RelationAttrReq attrScreeningBeanToRelationAttrReq(@NonNull AttrScreeningBean screeningBean) {
        RelationAttrReq attrReq = new RelationAttrReq();
        attrReq.setAttrId(screeningBean.getAttrId());
        attrReq.setSeqNo(screeningBean.getSeqNo());
        attrReq.set$eq(screeningBean.get$eq());
        attrReq.set$gt(screeningBean.get$gt());
        attrReq.set$lt(screeningBean.get$lt());
        return attrReq;
    }
}
