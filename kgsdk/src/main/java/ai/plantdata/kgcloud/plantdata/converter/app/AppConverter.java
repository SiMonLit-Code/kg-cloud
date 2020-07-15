package ai.plantdata.kgcloud.plantdata.converter.app;

import ai.plantdata.kgcloud.plantdata.converter.graph.ExploreCommonConverter;
import ai.plantdata.kgcloud.sdk.req.app.*;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.converter.common.MongoQueryConverter;
import ai.plantdata.kgcloud.plantdata.req.app.AssociationParameter;
import ai.plantdata.kgcloud.plantdata.req.app.LayerAssociationParameter;
import ai.plantdata.kgcloud.plantdata.req.app.LayerKnowledgeParameter;
import ai.plantdata.kgcloud.plantdata.req.common.KVBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.rsp.app.ApkBean;
import ai.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 19:52
 */
public class AppConverter extends BasicConverter {

    public static KnowledgeRecommendReqList associationParameterToKnowledgeRecommendReq(@NonNull AssociationParameter param) {
        KnowledgeRecommendReqList recommendReq = new KnowledgeRecommendReqList();
        recommendReq.setAllowAttrs(param.getAllowAtts());
        recommendReq.setAllowAttrsKey(param.getAllowAttsKey());
        recommendReq.setDirection(param.getDirection());
        recommendReq.setEntityId(param.getEntityId());
        recommendReq.setPage(NumberUtils.INTEGER_ONE);
        recommendReq.setSize(param.getPageSize());
        return recommendReq;
    }

    public static LayerKnowledgeRecommendReqList layerAssociationParameterToKnowledgeRecommendReq(@NonNull LayerAssociationParameter param) {
        LayerKnowledgeRecommendReqList recommendReq = new LayerKnowledgeRecommendReqList();
        recommendReq.setKw(param.getKw());
        recommendReq.setEntityId(param.getEntityId());
        recommendReq.setPage(new PageReq(param.getPageNo(),param.getPageSize()));
        recommendReq.setLayerFilter(layerParamer2KnowledgeRecommendCommonFilterReq(param.getLayerFilter()));
        return recommendReq;
    }

    private static Map<Integer, KnowledgeRecommendCommonFilterReq> layerParamer2KnowledgeRecommendCommonFilterReq(Map<Integer, LayerKnowledgeParameter> layerFilter) {

        Map<Integer,KnowledgeRecommendCommonFilterReq> map = new HashMap<>(layerFilter.size());
        for(Map.Entry<Integer,LayerKnowledgeParameter> entry : layerFilter.entrySet()){
            KnowledgeRecommendCommonFilterReq filter = new KnowledgeRecommendCommonFilterReq();
            consumerIfNoNull(entry.getValue().getAllowTypesKey(), filter::setAllowConceptsKey);
            consumerIfNoNull(entry.getValue().getReplaceClassIds(), filter::setReplaceClassIds);
            consumerIfNoNull(entry.getValue().getReplaceClassIdsKey(), filter::setReplaceClassKeys);
            consumerIfNoNull(entry.getValue().getIsRelationMerge(), filter::setRelationMerge);
            consumerIfNoNull(entry.getValue().getAllowAttsKey(), filter::setAllowAttrsKey);
            consumerIfNoNull(entry.getValue().getAllowTypes(), filter::setAllowConcepts);
            consumerIfNoNull(entry.getValue().getAllowAttrGroups(), a -> filter.setAllowAttrGroups(toListNoNull(a, Long::valueOf)));
            consumerIfNoNull(entry.getValue().getAllowAtts(), filter::setAllowAttrs);
            consumerIfNoNull(entry.getValue().getEntityQuery(), a -> filter.setEntityFilters(toListNoNull(a, MongoQueryConverter::entityScreeningBeanToEntityQueryFiltersReq)));
            consumerIfNoNull(entry.getValue().getAttAttFilters(), a -> filter.setEdgeAttrFilters(toListNoNull(a, ExploreCommonConverter::attrScreeningBeanToRelationAttrReq)));
            consumerIfNoNull(entry.getValue().getReservedAttFilters(), a -> filter.setReservedAttFilters(toListNoNull(a, ExploreCommonConverter::attrScreeningBeanToMetaDataReq)));


            map.put(entry.getKey(),filter);
        }

        return map;
    }

    public static KVBean<String, List<EntityBean>> infoBoxAttrRspToKvBean(@NonNull ObjectAttributeRsp attrRsp) {
        List<EntityBean> entityBeans = toListNoNull(attrRsp.getEntityList(), PromptConverter::promptEntityRspToEntityBean);
        return new KVBean<>(String.valueOf(attrRsp.getAttrDefId()), entityBeans);
    }

    public static ApkBean apkRspToApkBean(@NonNull ApkRsp apkRsp) {
        ApkBean apkBean = new ApkBean();
        apkBean.setApk(apkRsp.getApk());
        apkBean.setKgName(apkRsp.getKgName());
        apkBean.setTitle(apkRsp.getTitle());
        return apkBean;
    }
}
