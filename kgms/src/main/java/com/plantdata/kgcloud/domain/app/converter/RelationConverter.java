package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.BatchQueryRelationFrom;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.resp.GisRelationVO;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.util.DateUtils;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 17:15
 */
public class RelationConverter extends BasicConverter {

    public static BatchQueryRelationFrom edgeAttrSearch(EdgeSearchReq searchReq) {
        BatchQueryRelationFrom queryRelationFrom = new BatchQueryRelationFrom();
        queryRelationFrom.setEntityIds(searchReq.getEntityIds());
        queryRelationFrom.setAttrIds(searchReq.getAttrIds());
        queryRelationFrom.setAttrValueIds(searchReq.getAttrValueIds());
        queryRelationFrom.setLimit(searchReq.getLimit());
        queryRelationFrom.setSkip(searchReq.getOffset());
        consumerIfNoNull(searchReq.getEdgeAttrQuery(), a -> queryRelationFrom.setAttrExtInfoFilters(ConditionConverter.buildEdgeAttrSearchMap(a)));
        queryRelationFrom.setDirection(searchReq.getDirection());
        //时间筛选
        Map<String, Object> attrTimeFilters = Maps.newHashMap();
        consumerIfNoNull(searchReq.getAttrTimeFrom(),a->{
            DateUtils.checkDataMap(a);
            attrTimeFilters.put("attr_time_from",a);
        });
        consumerIfNoNull(searchReq.getAttrTimeTo(),a-> {
            DateUtils.checkDataMap(a);
            attrTimeFilters.put("attr_time_to", a);
        });
        consumerIfNoNull(attrTimeFilters,queryRelationFrom::setAttrTimeFilters);
        return queryRelationFrom;

    }


    static GisRelationRsp voToGisRsp(@NotNull GisRelationVO relation, Map<String, Long> ruleIdMap) {
        GisRelationRsp relationRsp = new GisRelationRsp();
        relationRsp.setRuleId(ruleIdMap.get(relation.getId()));
        relationRsp.setAttId(relation.getAttId());
        relationRsp.setAttName(relation.getAttrName());
        relationRsp.setDirection(relation.getDirection());
        relationRsp.setFrom(relation.getFromId());
        relationRsp.setTo(relation.getToId());
        relationRsp.setStartTime(relation.getStartTime());
        relationRsp.setEndTime(relation.getEndTime());
        return relationRsp;
    }

    public static AggRelationFrom edgeAttrPromptReqToAggRelationFrom(EdgeAttrPromptReq req) {
        AggRelationFrom from = new AggRelationFrom();
        from.setSkip( req.getOffset());
        from.setLimit(req.getLimit());
        from.setSeqNo(req.getSeqNo());
        from.setAttrId(req.getAttrId());
        from.setIsReserved(req.getReserved());
        from.setSearchOption(StringUtils.isEmpty(req.getSearchOption()) ? req.getKw() : req.getSearchOption());
//        if (!CollectionUtils.isEmpty(req.getSorts())) {
//            String sort = req.getSorts().get(1);
//            Optional<SortTypeEnum> typeOpt = SortTypeEnum.parseByName(sort);
//            typeOpt.ifPresent(sortTypeEnum -> from.setSortDirection(sortTypeEnum.getValue()));
//        }
        if (from.getSortDirection() == null) {
            from.setSortDirection(SortTypeEnum.ASC.getValue());
        }
        return from;
    }


    public static List<EdgeAttributeRsp> mapToEdgeAttributeRsp(@NonNull List<Map<Object, Integer>> mapList) {
        return mapList.stream().flatMap(map -> map.entrySet().stream()).map(entry -> new EdgeAttributeRsp(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static EdgeSearchRsp batchVoToEdgeSearchRsp(@NonNull BatchRelationVO relation) {
        EdgeSearchRsp.EdgeSearchEntityRsp from = new EdgeSearchRsp.EdgeSearchEntityRsp();
        from.setId(relation.getEntityId());
        from.setConceptId(relation.getEntityConcept());
        from.setMeaningTag(relation.getEntityMeaningTag());
        from.setName(relation.getEntityName());
        EdgeSearchRsp.EdgeSearchEntityRsp to = new EdgeSearchRsp.EdgeSearchEntityRsp();
        to.setId(relation.getAttrValueId());
        to.setConceptId(relation.getAttrValueConcept());
        to.setMeaningTag(relation.getAttrValueMeaningTag());
        to.setName(relation.getAttrValueName());

        EdgeSearchRsp edgeSearchRsp = new EdgeSearchRsp();
        edgeSearchRsp.setAttrTimeFrom(relation.getAttrTimeFrom());
        edgeSearchRsp.setAttrTimeTo(relation.getAttrTimeTo());
        edgeSearchRsp.setAttrId(relation.getAttrId());
        edgeSearchRsp.setFromEntity(from);
        edgeSearchRsp.setToEntity(to);
        edgeSearchRsp.setTripleId(relation.getId());
        edgeSearchRsp.setExtraInfoMap(relation.getExtraInfoMap());
        Map<String, Object> metaData = relation.getMetaData();
        if (metaData != null) {
            if (metaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
                edgeSearchRsp.setScore(metaData.get(MetaDataInfo.SCORE.getFieldName()).toString());
            }
            if (metaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                edgeSearchRsp.setSource(metaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
            }
            if (metaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                edgeSearchRsp.setReliability(metaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString());
            }
        }
        return edgeSearchRsp;
    }

}
