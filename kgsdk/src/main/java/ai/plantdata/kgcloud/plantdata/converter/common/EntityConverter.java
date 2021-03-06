package ai.plantdata.kgcloud.plantdata.converter.common;

import ai.plantdata.kgcloud.plantdata.req.common.Additional;
import ai.plantdata.kgcloud.plantdata.req.data.EntityAttrDelectParameter;
import ai.plantdata.kgcloud.plantdata.req.data.EntityByDataAttributeParameter;
import ai.plantdata.kgcloud.plantdata.req.data.TraceabilityParameter;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import ai.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import ai.plantdata.kgcloud.sdk.req.app.*;
import com.google.common.collect.Maps;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 16:16
 */
public class EntityConverter extends BasicConverter {

    public static <T extends BasicEntityRsp, R extends EntityBean> R basicEntityRspToEntityBean(T newEntity, R oldEntity) {
        oldEntity.setId(newEntity.getId());
        oldEntity.setName(newEntity.getName());
        oldEntity.setMeaningTag(newEntity.getMeaningTag());
        oldEntity.setConceptId(newEntity.getConceptId());
        oldEntity.setCreationTime(newEntity.getCreationTime());
        oldEntity.setType(newEntity.getType());
        Additional additional = new Additional();
        oldEntity.setAdditionalInfo(additional);
        return oldEntity;
    }

    public static ImportEntityBean openEntityRspToImportEntityBean(@NonNull OpenEntityRsp rsp) {
        ImportEntityBean entityBean = new ImportEntityBean();
        entityBean.setId(rsp.getId());
        entityBean.setName(rsp.getName());
        entityBean.setAbs(rsp.getAbs());
        entityBean.setAttributes(rsp.getAttributes());
        entityBean.setConceptId(rsp.getConceptId());
        entityBean.setConceptIdList(rsp.getConceptIdList());
        entityBean.setImageUrl(rsp.getImgUrl());
        entityBean.setMeaningTag(rsp.getMeaningTag());
        entityBean.setSynonyms(rsp.getSynonyms());
        return entityBean;
    }

    public static EntityQueryReq entityByDataAttributeParameterToEntityQueryReq(EntityByDataAttributeParameter parameter) {
        EntityQueryReq entityQueryReq = new EntityQueryReq();
        consumerIfNoNull(parameter.getQuery(), a -> {
            List<DataAttrReq> dataAttrReqs = BasicConverter.toListNoNull(a, MongoQueryConverter::entityScreeningBeanToEntityDataAttrReq);
            entityQueryReq.setDataAttrFilters(dataAttrReqs);
        });
        entityQueryReq.setConceptId(parameter.getConceptId());
        entityQueryReq.setConceptKey(parameter.getConceptKey());
        entityQueryReq.setPage(parameter.getPageNo());
        entityQueryReq.setSize(parameter.getPageSize());
        return entityQueryReq;
    }

    public static TraceabilityQueryReq traceabilityParameterToTraceabilityReq(TraceabilityParameter parameter) {
        TraceabilityQueryReq traceabilityQueryReq = new TraceabilityQueryReq();
        traceabilityQueryReq.setDataName(parameter.getDataName());
        traceabilityQueryReq.setTableName(parameter.getTableName());
        traceabilityQueryReq.setId(parameter.getId());
        traceabilityQueryReq.setPage(parameter.getPageNo());
        traceabilityQueryReq.setSize(parameter.getPageSize());
        return traceabilityQueryReq;
    }

    public static ImportEntityBean OpenBatchSaveEntityRspToImportEntityBean(@NonNull  OpenBatchSaveEntityRsp entityRsp) {
        ImportEntityBean entityBean = copy(entityRsp, ImportEntityBean.class);
        consumerIfNoNull(entityRsp.getAttributes(), a -> {
            Map<String, Object> tempMap = Maps.newHashMap();
            a.forEach((k, v) -> tempMap.put(String.valueOf(k), v));
            entityBean.setAttributes(tempMap);
        });
        consumerIfNoNull(entityRsp.getPrivateAttributes(), entityBean::setPrivateAttributes);
        return entityBean;
    }

    public static OpenBatchSaveEntityRsp importEntityBeanToOpenBatchSaveEntityRsp(@NonNull ImportEntityBean entityRsp) {
        OpenBatchSaveEntityRsp saveEntityRsp = new OpenBatchSaveEntityRsp();
        consumerIfNoNull(entityRsp.getAttributes(), a -> {
            Map<Integer, String> tempMap = Maps.newHashMap();
            a.forEach((k, v) -> tempMap.put(Integer.parseInt(k), v.toString()));
            saveEntityRsp.setAttributes(tempMap);
        });
        consumerIfNoNull(entityRsp.getPrivateAttributes(), saveEntityRsp::setPrivateAttributes);
        saveEntityRsp.setAbs(entityRsp.getAbs());
        saveEntityRsp.setConceptId(entityRsp.getConceptId());
        saveEntityRsp.setId(entityRsp.getId());
        saveEntityRsp.setName(entityRsp.getName());
        saveEntityRsp.setImageUrl(entityRsp.getImageUrl());
        saveEntityRsp.setSynonyms(entityRsp.getSynonyms());
        saveEntityRsp.setMeaningTag(entityRsp.getMeaningTag());
        saveEntityRsp.setMetaDataMap(entityRsp.getMetaData());
        saveEntityRsp.setAttrValueMetaData(entityRsp.getAttrValueMetaData());
        return saveEntityRsp;
    }

    public static BatchEntityAttrDeleteReq entityAttrBatchDelParamToReq(@NonNull EntityAttrDelectParameter param) {
        BatchEntityAttrDeleteReq attrDeleteReq = new BatchEntityAttrDeleteReq();
        consumerIfNoNull(param.getAttributeIds(), a -> attrDeleteReq.setAttributeIds(toListNoNull(a, Integer::valueOf)));
        consumerIfNoNull(param.getAttrNames(), attrDeleteReq::setAttrNames);
        consumerIfNoNull(param.getEntityIds(), attrDeleteReq::setEntityIds);
        return attrDeleteReq;
    }

}
