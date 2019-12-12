package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.AttributeStatisticsBean;
import ai.plantdata.kg.api.pub.req.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import ai.plantdata.kg.api.pub.req.RelationExtraInfoStatisticBean;
import ai.plantdata.kg.api.pub.req.RelationStatisticsBean;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import com.plantdata.kgcloud.constant.AppConstants;
import com.plantdata.kgcloud.constant.StatisticResultTypeEnum;
import com.plantdata.kgcloud.domain.app.bo.GraphAttributeStatisticBO;
import com.plantdata.kgcloud.domain.app.bo.GraphRelationStatisticBO;
import com.plantdata.kgcloud.domain.app.converter.GraphStatisticConverter;
import com.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.dataset.ReadTableReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.DateTypeReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public AttributeApi attributeApi;
    @Autowired
    public StatisticsApi statisticsApi;
    @Autowired
    public DataSetSearchService dataSetSearchService;


    @Override
    public List<Map<String, Object>> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq) {
        EntityRelationDegreeFrom degreeFrom = GraphRelationStatisticBO.buildDegreeFrom(statisticReq);
        degreeFrom.setDistance(NumberUtils.INTEGER_ONE);
        Optional<Map<Integer, Integer>> countOneOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        degreeFrom.setDistance(NumberUtils.INTEGER_TWO);
        Optional<Map<Integer, Integer>> countTwoOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        degreeFrom.setDistance(NumberUtils.INTEGER_ZERO);
        Optional<Map<Integer, Integer>> countZeroOpt = RestRespConverter.convert(graphApi.degree(kgName, degreeFrom));
        return GraphRelationStatisticBO.graphDegreeMapToList(countOneOpt.orElse(Collections.emptyMap()), countTwoOpt.orElse(Collections.emptyMap()), countZeroOpt.orElse(Collections.emptyMap()));
    }

    @Override
    public Object statEdgeGroupByEdgeValue(String kgName, EdgeAttrStatisticByAttrValueReq statisticReq) {


        Optional<AttributeDefinition> arrDefOpt = RestRespConverter.convert(attributeApi.get(kgName, statisticReq.getAttrId()));

        if (!arrDefOpt.isPresent()) {
            return GraphStatisticConverter.statisticByType(Collections.emptyList(), statisticReq.getReturnType(), StatisticResultTypeEnum.VALUE);
        }
        RelationExtraInfoStatisticBean statisticBean = GraphStatisticConverter.edgeAttrReqToRelationExtraInfoStatisticBean(statisticReq);
        Optional<List<Map<String, Object>>> dataOpt = RestRespConverter.convert(statisticsApi.relationExtraInfoStatistic(kgName, statisticBean));
        if (!dataOpt.isPresent()) {
            return GraphStatisticConverter.statisticByType(Collections.emptyList(), statisticReq.getReturnType(), StatisticResultTypeEnum.VALUE);
        }
        List<StatisticDTO> dataList = JsonUtils.readToList(JsonUtils.toJson(dataOpt.get()), StatisticDTO.class);
        AttributeDataTypeEnum dataType = GraphStatisticConverter.edgeAttrDataType(statisticReq.getSeqNo(), arrDefOpt.get());
        return buildStatisticResult(dataType, statisticReq.getMerge(), dataList, statisticReq.getDateType(), statisticReq.getSort(), statisticBean.getSize(), statisticReq.getReturnType());
    }

    @Override
    public Object statEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq) {
        ConceptStatisticsBean statisticsBean = GraphStatisticConverter.entityReqConceptStatisticsBean(statisticReq);
        if (CollectionUtils.isEmpty(statisticReq.getEntityIds())) {
            return null;
        }
        Optional<List<Map<String, Object>>> resultOpt = RestRespConverter.convert(statisticsApi.conceptStatistics(kgName, statisticsBean));
        if (!resultOpt.isPresent()) {
            return null;
        }
        List<StatisticDTO> dataList = JsonUtils.readToList(JsonUtils.toJson(resultOpt.get()), StatisticDTO.class);
        return GraphStatisticConverter.statisticByType(dataList, statisticReq.getReturnType(), StatisticResultTypeEnum.NAME);
    }

    @Override
    public Object statisticRelation(String kgName, EdgeStatisticByConceptIdReq conceptIdReq) {
        RelationStatisticsBean statisticsBean = GraphStatisticConverter.conceptIdReqConceptStatisticsBean(conceptIdReq);
        boolean isNoEdgeId = CollectionUtils.isEmpty(conceptIdReq.getTripleIds());
        if (!isNoEdgeId) {
            return null;
        }
        Optional<List<Map<String, Object>>> resultOpt = RestRespConverter.convert(statisticsApi.relationStatistics(kgName, statisticsBean));
        if (!resultOpt.isPresent()) {
            return null;
        }
        List<StatisticDTO> dataList = JsonUtils.readToList(JsonUtils.toJson(resultOpt.get()), StatisticDTO.class);
        return GraphStatisticConverter.statisticByType(dataList, conceptIdReq.getReturnType(), StatisticResultTypeEnum.NAME);
    }

    @Override
    public Object statisticAttrGroupByConcept(String kgName, EntityStatisticGroupByAttrIdReq attrIdReq) {

        Optional<AttributeDefinition> attrDefOpt = RestRespConverter.convert(attributeApi.get(kgName, attrIdReq.getAttrId()));
        if (!attrDefOpt.isPresent()) {
            return GraphStatisticConverter.statisticByType(Collections.emptyList(), attrIdReq.getReturnType(), StatisticResultTypeEnum.VALUE);
        }
        Integer valueType = attrDefOpt.get().getType();
        AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(attrDefOpt.get().getDataType());
        int reSize = GraphStatisticConverter.reBuildResultSize(attrIdReq.getSize(), valueType, dataType);

        List<StatisticDTO> dataList = Collections.emptyList();
        if (attrIdReq.getEntityIds() == null || !attrIdReq.getEntityIds().isEmpty()) {
            AttributeStatisticsBean statisticsBean = GraphStatisticConverter.attrReqToAttributeStatisticsBean(reSize, attrIdReq);
            Optional<List<Map<String, Object>>> mapOpt = RestRespConverter.convert(statisticsApi.attributeStatistics(kgName, statisticsBean));
            if (mapOpt.isPresent()) {
                dataList = JsonUtils.readToList(JsonUtils.toJson(mapOpt.get()), StatisticDTO.class);
            }
        }
        return buildStatisticResult(dataType, attrIdReq.getMerge(), dataList, attrIdReq.getDateType(), attrIdReq.getSort(), reSize, attrIdReq.getReturnType());
    }
    @Override
    public RestData<Map<String, Object>> readMongoDataSet(ReadTableReq readTableReq) {
        PageUtils pageUtils = new PageUtils(readTableReq.getPage(), readTableReq.getSize());
        return dataSetSearchService.readMongoData(readTableReq.getDatabase(), readTableReq.getTable(), pageUtils.getOffset(), pageUtils.getLimit(), readTableReq.getQuery(), readTableReq.getFields(), readTableReq.getSort());
    }

    private Object buildStatisticResult(AttributeDataTypeEnum dataType, boolean merge, List<StatisticDTO> dataList, DateTypeReq dateType, int sort, int reSize, int returnType) {
        boolean isNum = AttributeDataTypeEnum.DATA_VALUE_SET.contains(dataType);
        boolean isDate = AttributeDataTypeEnum.DATE_SET.contains(dataType);
        if (merge) {
            if (isNum) {
                dataList = GraphAttributeStatisticBO.countMerge(dataList, dataType, AppConstants.COUNT_MERGE_SIZE);
            }
            if (isDate) {
                dataList = GraphAttributeStatisticBO.countMergeDate(dataList, dateType);
            }
            if (!CollectionUtils.isEmpty(dataList) && sort == -1) {
                Collections.reverse(dataList);
            }
        }
        dataList = PageUtils.subList(NumberUtils.INTEGER_ZERO, reSize, dataList);
        return GraphStatisticConverter.statisticByType(dataList, returnType, StatisticResultTypeEnum.VALUE);
    }


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