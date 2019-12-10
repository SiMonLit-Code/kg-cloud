package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import com.plantdata.kgcloud.domain.app.bo.GraphStatiticBO;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.constant.DataSetStatisticEnum;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatDataRsp;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 20:39
 */
@Service
public class KgDataServiceImpl implements KgDataService {

    @Autowired
    public GraphApi graphApi;
    @Autowired
    public StatisticsApi statisticsApi;

    @Override
    public List<Map<String, Object>> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq) {
        EntityRelationDegreeFrom degreeFrom = GraphStatiticBO.buildDegreeFrom(statisticReq);
        degreeFrom.setDistance(NumberUtils.INTEGER_ONE);
        Optional<Map<Integer, Integer>> countOneOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        degreeFrom.setDistance(NumberUtils.INTEGER_TWO);
        Optional<Map<Integer, Integer>> countTwoOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        degreeFrom.setDistance(NumberUtils.INTEGER_ZERO);
        Optional<Map<Integer, Integer>> countZeroOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        return GraphStatiticBO.graphDegreeMapToList(countOneOpt.orElse(Collections.emptyMap()), countTwoOpt.orElse(Collections.emptyMap()), countZeroOpt.orElse(Collections.emptyMap()));
    }

    @Override
    public Object statEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq) {
        if (statisticReq.getSize().equals(NumberUtils.INTEGER_MINUS_ONE)) {
            statisticReq.setSize(Integer.MAX_VALUE - NumberUtils.INTEGER_ONE);
        }
        Integer appendId = statisticReq.getEntityIds() == null || statisticReq.getEntityIds().isEmpty() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        ConceptStatisticsBean statisticsBean = new ConceptStatisticsBean();
        statisticsBean.setAllowTypes(JsonUtils.toJson(statisticReq.getAllowTypes()));
        statisticsBean.setAppendId(appendId);
        statisticsBean.setDirection(statisticReq.getDirection());
        statisticsBean.setEntityIds(JsonUtils.toJson(statisticReq.getEntityIds()));
        statisticsBean.setPos(NumberUtils.INTEGER_ZERO);
        statisticsBean.setSize(statisticReq.getSize());
        if (statisticReq.getEntityIds().isEmpty()) {
            return null;
        }
        Optional<List<Map<String, Object>>> resultOpt = RestRespConverter.convert(statisticsApi.conceptStatistics(kgName, statisticsBean));
        List<Map<String, Object>> maps = PageUtils.subList(statisticsBean.getPos(), statisticsBean.getSize(), resultOpt.orElse(Collections.emptyList()));
        return statisticEntityGroupByConcept(maps, statisticReq.getReturnType());
    }

//    public Object statEntityGroupByAttrId(EntityStatisticGroupByAttrIdReq attrIdReq) {
//        if (attrIdReq.getSize() == -1) {
//            attrIdReq.setSize(Integer.MAX_VALUE - 1);
//        }
//
//        Integer appendIdList = 1;
//        if (parameter.getEntityIds() == null || parameter.getEntityIds().isEmpty()) {
//            appendIdList = 0;
//        }
//        Integer reSize = parameter.getSize();
//        List<ResultsBean> list = new ArrayList<>();
//        Integer type = 0;
//        EditAttDefBean attr = editEntityFeign.getAttrById(parameter.getKgName(), parameter.getAttrId());
//        if (attr == null) {
//            return ResultsParser.EntityScreeningValueListToStatData(list, parameter.getReturnType());
//        }
//        type = attr.getDataType();
//        if ("0".equals(attr.getType()) && (type == 1 || type == 2)) {
//            if (parameter.getSize() == null) {
//                parameter.setSize(10);
//            }
//            reSize = parameter.getSize();
//        } else if ("0".equals(attr.getType()) && (type == 4 || type == 41 || type == 42)) {
//            if (parameter.getSize() == null) {
//                parameter.setSize(Integer.MAX_VALUE);
//            }
//            reSize = Integer.MAX_VALUE;
//        } else {
//            if (parameter.getSize() == null) {
//                parameter.setSize(10);
//            }
//        }
//        if (!parameter.getMerge() && (type == 1 || type == 2))
//            type = 0;
//        if (parameter.getEntityIds() == null || !parameter.getEntityIds().isEmpty()) {
//            list = statisticsFeign.statAttributeIdByConcept(StatAttributeIdByConceptRemotParameter.builder()
//                    .kgName(parameter.getKgName())
//                    .attributeId(parameter.getAttrId())
//                    .appendId(appendIdList)
//                    .entityIds(parameter.getEntityIds())
//                    .direction(parameter.getSort())
//                    .allowValues(parameter.getAllowValues())
//                    .pos(0)
//                    .size(reSize)
//                    .build());
//        }
//        if (type != 0) {
//            if (type == 1 || type == 2)
//                list = ResultsParser.countMerge(list, type, countMergeSize);
//            else if (type == 4 || type == 41 || type == 42) {
//                list = ResultsParser.countMergeDate(list, type, parameter.getDateType());
//                if (parameter.getSort() == -1) {
//                    Collections.reverse(list);
//                }
//            }
//        }
//        list = list.size() <= parameter.getSize() ? list : list.subList(0, parameter.getSize());
//        return ResultsParser.EntityScreeningValueListToStatData(list, parameter.getReturnType());
//    }

    private static Object statisticEntityGroupByConcept(List<Map<String, Object>> dataMapList, int returnType) {
        if (returnType == DataSetStatisticEnum.KV.getValue()) {
            return dataMapList.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", s.get("name"));
                map.put("value", s.get("total"));
                Object relation = s.get("relation");
                Object entity = s.get("entity");
                if (relation != null) {
                    map.put("ids", relation);
                }
                if (relation != null) {
                    map.put("ids", entity);
                }
                return map;
            }).collect(Collectors.toList());
        }
        StatDataRsp statDataRsp = new StatDataRsp();
        if (CollectionUtils.isEmpty(dataMapList)) {
            return statDataRsp;
        }
        List<String> xData = new ArrayList<>();
        List<Long> sData = new ArrayList<>();

        dataMapList.forEach(dataMap -> {
            xData.add((String) dataMap.get("name"));
            sData.add((Long) dataMap.get("total"));
        });

        statDataRsp.addData2X(xData);
        statDataRsp.addData2Series("", sData);
        return statDataRsp;
    }
//
//    @Override
//    public void sparkSqlExport(String kgName) {
//
//        String exportName = "sparql_" + kgName + "_" + System.currentTimeMillis();
//
//        //查询搜索结果
//        QueryResultBean queryResultBean = sparqlQuery(SparqlQueryParameter.builder()
//                .kgName(kgName)
//                .query(sparkSqlExportParameter.getQuery())
//                .size(sparkSqlExportParameter.getSize())
//                .build()
//        );
//        List<List<SparqlNodeBean>> sparqlNodeBeans = queryResultBean.getResult();
//
//        if (sparkSqlExportParameter.getType() == 0) {
//            //导出txt
//            JsonUtils.exportJson(exportName, JsonUtils.toJson(sparqlNodeBeans));
//        } else if (sparkSqlExportParameter.getType() == 1 || sparkSqlExportParameter.getType() == 2) {
//            //创建标题
//            List<String> titleList = Lists.newArrayList();
//            if (sparqlNodeBeans != null && sparqlNodeBeans.size() > 0) {
//                List<SparqlNodeBean> sparqlNodeBeanList = sparqlNodeBeans.get(0);
//                if (sparqlNodeBeanList.size() > 0) {
//                    for (SparqlNodeBean sparqlNodeBean : sparqlNodeBeanList) {
//                        titleList.add(sparqlNodeBean.getKey());
//                    }
//                }
//            }
//
//            //导出excel
//            ExcelUtils.generateWorkbook(exportName, "xls", titleList, sparqlNodeBeans);
//        }
//    }

}
