package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.statistics.AttributeStatisticsBean;
import ai.plantdata.kg.api.pub.req.statistics.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.statistics.RelationExtraInfoStatisticBean;
import ai.plantdata.kg.api.pub.req.statistics.RelationStatisticsBean;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.ExtraInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.StatisticResultTypeEnum;
import com.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.DataSetStatisticEnum;
import com.plantdata.kgcloud.sdk.constant.StatisticConstants;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatDataRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 9:59
 */
@Slf4j
public class GraphStatisticConverter extends BasicConverter {


    public static AttributeStatisticsBean attrReqToAttributeStatisticsBean(int reSize, EntityStatisticGroupByAttrIdReq attrIdReq) {
        Integer appendId = attrIdReq.getEntityIds() == null || attrIdReq.getEntityIds().isEmpty() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        AttributeStatisticsBean statisticsBean = new AttributeStatisticsBean();
        statisticsBean.setEntityIds(attrIdReq.getEntityIds());
        statisticsBean.setSkip(NumberUtils.INTEGER_ZERO);
        statisticsBean.setLimit(reSize);
        statisticsBean.setSort(attrIdReq.getSort());
        statisticsBean.setAppendId(appendId);
        statisticsBean.setAttributeId(attrIdReq.getAttrDefId());
        statisticsBean.setAllowValues(attrIdReq.getAllowValues());
        log.error("AttributeStatisticsBean:{}", JsonUtils.objToJson(statisticsBean));
        return statisticsBean;
    }

    public static ConceptStatisticsBean entityReqConceptStatisticsBean(EntityStatisticGroupByConceptReq statisticReq) {
        Integer appendId = statisticReq.getEntityIds() == null || statisticReq.getEntityIds().isEmpty() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        ConceptStatisticsBean statisticsBean = new ConceptStatisticsBean();
        statisticsBean.setAllowTypes(statisticReq.getAllowConcepts());
        statisticsBean.setAppendId(appendId);
        statisticsBean.setSort(statisticReq.getSort());
        statisticsBean.setEntityIds(statisticReq.getEntityIds());
        statisticsBean.setSkip(NumberUtils.INTEGER_ZERO);
        statisticsBean.setLimit(defaultStatisticSize(statisticReq.getSize()));
        log.error("ConceptStatisticsBean:{}", JsonUtils.objToJson(statisticsBean));
        return statisticsBean;
    }

    public static RelationStatisticsBean conceptIdReqConceptStatisticsBean(EdgeStatisticByConceptIdReq conceptIdReq) {
        RelationStatisticsBean statisticsBean = new RelationStatisticsBean();
        Integer appendId = CollectionUtils.isEmpty(conceptIdReq.getTripleIds()) ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        statisticsBean.setAllowAttrs(conceptIdReq.getAllowAttrs());
        statisticsBean.setConceptId(conceptIdReq.getConceptId());
        statisticsBean.setAppendId(appendId);
        statisticsBean.setSort(conceptIdReq.getSort());
        statisticsBean.setStartTime(conceptIdReq.getFromTime());
        statisticsBean.setEndTime(conceptIdReq.getToTime());
        statisticsBean.setSkip(NumberUtils.INTEGER_ZERO);
        consumerIfNoNull(conceptIdReq.getTripleIds(), statisticsBean::setTripleIds);
        consumerIfNoNull(conceptIdReq.getSize(), a -> statisticsBean.setLimit(a == -1 ? Integer.MAX_VALUE - 1 : a));
        log.error("RelationStatisticsBean:{}", JsonUtils.objToJson(statisticsBean));
        return statisticsBean;
    }

    public static RelationExtraInfoStatisticBean edgeAttrReqToRelationExtraInfoStatisticBean(EdgeAttrStatisticByAttrValueReq attrValueReq) {
        RelationExtraInfoStatisticBean statisticBean = new RelationExtraInfoStatisticBean();
        Integer appendId = CollectionUtils.isEmpty(attrValueReq.getTripleIds()) ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        statisticBean.setAllowValues(attrValueReq.getAllowValues());
        statisticBean.setAttributeId(attrValueReq.getAttrDefId());
        statisticBean.setAppendId(appendId);
        statisticBean.setEntityIds(attrValueReq.getEntityIds());
        statisticBean.setSort(attrValueReq.getSort());
        statisticBean.setSeqNo(attrValueReq.getSeqNo());
        statisticBean.setTripleIds(attrValueReq.getTripleIds());
        statisticBean.setSkip(NumberUtils.INTEGER_ZERO);
        statisticBean.setLimit(defaultStatisticSize(attrValueReq.getSize()));
        statisticBean.setSort(attrValueReq.getSort());
        log.error("RelationExtraInfoStatisticBean:{}", JsonUtils.objToJson(statisticBean));
        return statisticBean;
    }

    public static Object statisticByType(List<StatisticDTO> dataMapList, int returnType, StatisticResultTypeEnum resultType) {
        StatDataRsp statDataRsp = new StatDataRsp();
        if (CollectionUtils.isEmpty(dataMapList)) {
            return statDataRsp;
        }
        if (returnType == DataSetStatisticEnum.KV.getValue()) {
            return dataMapList.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                String value = StatisticResultTypeEnum.NAME.equals(resultType) ? s.getName() : s.getValue();
                map.put("name", value);
                map.put("value", s.getTotal());
                BasicConverter.consumerIfNoNull(s.getRelation(), a -> map.put("ids", a));
                BasicConverter.consumerIfNoNull(s.getEntity(), a -> map.put("ids", a));
                return map;
            }).collect(Collectors.toList());
        }
        List<String> xData = new ArrayList<>();
        List<Long> sData = new ArrayList<>();
        dataMapList.forEach(data -> {
            String value = StatisticResultTypeEnum.NAME.equals(resultType) ? data.getName() : data.getValue();
            xData.add(value);
            sData.add(data.getTotal());
        });

        statDataRsp.addData2X(xData);
        statDataRsp.addData2Series(sData);
        return statDataRsp;
    }

    public static Integer reBuildResultSize(Integer size, Integer valueType, AttributeDataTypeEnum dataType) {
        if (AttributeValueType.isNumeric(valueType) && AttributeDataTypeEnum.DATE_SET.contains(dataType)) {
            return StatisticConstants.STATISTIC_MAX_SIZE;
        }
        return size == null ? 10 : size;
    }

    private static int defaultStatisticSize(Integer size) {
        if (size != null && size.equals(NumberUtils.INTEGER_MINUS_ONE)) {
            return StatisticConstants.STATISTIC_MAX_SIZE;
        }
        if (size == null) {
            size = PageUtils.DEFAULT_SIZE;
        }
        return size;
    }

    public static AttributeDataTypeEnum edgeAttrDataType(int seqNo, AttributeDefinition attrDef) {
        if (seqNo == -1 || seqNo == -2) {
            return AttributeDataTypeEnum.DATETIME;
        }
        if (CollectionUtils.isEmpty(attrDef.getExtraInfo())) {
            throw BizException.of(AppErrorCodeEnum.EDGE_ATTR_DEF_NULL);
        }
        Optional<ExtraInfo> firstOpt = attrDef.getExtraInfo().stream().filter(a -> a.getSeqNo() == seqNo).findFirst();
        if (!firstOpt.isPresent()) {
            throw BizException.of(AppErrorCodeEnum.EDGE_ATTR_DEF_NULL);
        }
        return AttributeDataTypeEnum.parseById(firstOpt.get().getDataType());
    }
}
