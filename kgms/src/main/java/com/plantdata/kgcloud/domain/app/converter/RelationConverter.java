package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.BatchQueryRelationFrom;
import ai.plantdata.kg.api.edit.resp.BatchRelationVO;
import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.resp.GisRelationVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.EdgeSearchReq;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EdgeSearchRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 17:15
 */
public class RelationConverter {

    public static BatchQueryRelationFrom edgeAttrSearch(EdgeSearchReq searchReq) {
        BatchQueryRelationFrom queryRelationFrom = new BatchQueryRelationFrom();
        queryRelationFrom.setEntityIds(searchReq.getEntityIds());
        queryRelationFrom.setAttrIds(searchReq.getAttrIds());
        queryRelationFrom.setAttrValueIds(searchReq.getAttrValueIds());
        queryRelationFrom.setLimit(searchReq.getLimit());
        queryRelationFrom.setSkip(searchReq.getOffset());
        queryRelationFrom.setAttrExtInfoFilters(ConditionConverter.buildEdgeAttrSearchMap(searchReq.getEdgeAttrQuery()));
        queryRelationFrom.setDirection(searchReq.getDirection());
        //时间筛选
        Map<String, Object> attrTimeFilters = Maps.newHashMap();
        if (StringUtils.isNoneBlank(searchReq.getAttrTimeFrom())) {
            attrTimeFilters.put("attr_time_from", JacksonUtils.readValue(searchReq.getAttrTimeFrom(), new TypeReference<Map<String, Object>>() {
            }));
        }
        if (StringUtils.isNoneBlank(searchReq.getAttrTimeTo())) {
            attrTimeFilters.put("attr_time_to", JacksonUtils.readValue(searchReq.getAttrTimeTo(), new TypeReference<Map<String, Object>>() {
            }));
        }
        if (!CollectionUtils.isEmpty(attrTimeFilters)) {
            queryRelationFrom.setAttrTimeFilters(attrTimeFilters);
        }
        return queryRelationFrom;

    }


    static List<GisRelationRsp> voToGisRsp(List<GisRelationVO> relationList, Map<String, Integer> ruleIdMap) {
        List<GisRelationRsp> relationRspList = Lists.newArrayListWithCapacity(relationList.size());
        GisRelationRsp relationRsp;
        for (GisRelationVO relation : relationList) {
            relationRsp = new GisRelationRsp();
            relationRsp.setRuleId(ruleIdMap.get(relation.getId()));
            relationRsp.setAttId(relation.getAttId());
            relationRsp.setAttName(relationRsp.getAttName());
            relationRsp.setDirection(relationRsp.getDirection());
            relationRsp.setFrom(relationRsp.getFrom());
            relationRsp.setTo(relationRsp.getTo());
            relationRsp.setStartTime(relationRsp.getStartTime());
            relationRsp.setEndTime(relationRsp.getEndTime());
            relationRspList.add(relationRsp);
        }
        return relationRspList;
    }

    public static AggRelationFrom edgeAttrPromptReqToAggRelationFrom(EdgeAttrPromptReq req) {
        AggRelationFrom from = new AggRelationFrom();
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setSeqNo(req.getSeqNo());
        from.setAttrId(req.getAttrId());
        from.setIsReserved(req.getReserved());
        from.setSearchOption(StringUtils.isEmpty(req.getSearchOption()) ? req.getKw() : req.getSearchOption());
        if (!CollectionUtils.isEmpty(req.getSorts())) {
            String sort = req.getSorts().get(1);
            Optional<SortTypeEnum> typeOpt = SortTypeEnum.parseByName(sort);
            typeOpt.ifPresent(sortTypeEnum -> from.setSortDirection(sortTypeEnum.getValue()));
        }
        if (from.getSortDirection() == null) {
            from.setSortDirection(SortTypeEnum.ASC.getValue());
        }
        return from;
    }


    public static List<EdgeAttributeRsp> mapToEdgeAttributeRsp(@NonNull List<Map<Object, Integer>> mapList) {
        return mapList.stream().flatMap(map -> map.entrySet().stream()).map(entry -> new EdgeAttributeRsp(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<EdgeSearchRsp> batchVoToEdgeSearchRsp(@NonNull List<BatchRelationVO> relationList) {

        return relationList.stream().map(a -> {
            EdgeSearchRsp.EdgeSearchEntityRsp from = new EdgeSearchRsp.EdgeSearchEntityRsp();
            from.setId(a.getEntityId());
            from.setConceptId(a.getEntityConcept());
            from.setMeaningTag(a.getEntityMeaningTag());
            from.setName(a.getEntityName());
            EdgeSearchRsp.EdgeSearchEntityRsp to = new EdgeSearchRsp.EdgeSearchEntityRsp();
            to.setId(a.getAttrValueId());
            to.setConceptId(a.getAttrValueConcept());
            to.setMeaningTag(a.getAttrValueMeaningTag());
            to.setName(a.getAttrValueName());
            EdgeSearchRsp edgeSearchRsp = new EdgeSearchRsp();
            edgeSearchRsp.setFromEntity(from);
            edgeSearchRsp.setToEntity(to);
            edgeSearchRsp.setTripleId(a.getId());
            edgeSearchRsp.setExtraInfoMap(a.getExtraInfoMap());
            Map<String, Object> metaData = a.getMetaData();
            if (metaData != null) {
                if (metaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
                    edgeSearchRsp.setScore(metaData.get(MetaDataInfo.SCORE.getFieldName()).toString());
                }
                if (metaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    edgeSearchRsp.setSource(metaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
                }
                if (metaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    edgeSearchRsp.setReliability(metaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
                }
            }
            return edgeSearchRsp;
        }).collect(Collectors.toList());
    }

}
