package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.AggRelationFrom;
import ai.plantdata.kg.api.pub.resp.GisRelationVO;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisRelationRsp;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
}
