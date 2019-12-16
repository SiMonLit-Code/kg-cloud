package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.AttributeStatisticsBean;
import ai.plantdata.kg.api.pub.req.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import ai.plantdata.kg.api.pub.req.RelationExtraInfoStatisticBean;
import ai.plantdata.kg.api.pub.req.RelationStatisticsBean;
import ai.plantdata.kg.api.ql.SparqlApi;
import ai.plantdata.kg.api.ql.resp.NodeBean;
import ai.plantdata.kg.api.ql.resp.QueryResultVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AppConstants;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.ExportTypeEnum;
import com.plantdata.kgcloud.constant.StatisticResultTypeEnum;
import com.plantdata.kgcloud.domain.app.bo.GraphAttributeStatisticBO;
import com.plantdata.kgcloud.domain.app.bo.GraphRelationStatisticBO;
import com.plantdata.kgcloud.domain.app.converter.GraphStatisticConverter;
import com.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.service.KgDataService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.domain.app.util.TextUtils;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.DateTypeReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    private DataSetService dataSetService;
    @Autowired
    public DataSetSearchService dataSetSearchService;
    @Autowired
    public DataOptService dataOptService;
    @Autowired
    public SparqlApi sparqlApi;

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
    public RestData<Map<String, Object>> searchDataSet(String userId, NameReadReq nameReadReq) {
        PageUtils pageUtils = new PageUtils(nameReadReq.getPage(), nameReadReq.getSize());
        List<Long> dataSetIds = dataSetService.findByDataNames(userId, Lists.newArrayList(nameReadReq.getDataName()));

        DataSetRsp dataSetRsp = dataSetService.findById(userId, dataSetIds.get(0));
        if (DataType.MONGO.getDataType() == dataSetRsp.getDataType()) {
            return dataSetSearchService.readMongoData(dataSetRsp.getDbName(), dataSetRsp.getTbName(),
                    pageUtils.getOffset(), pageUtils.getLimit(), nameReadReq.getQuery(), nameReadReq.getFields(), nameReadReq.getSort());
        }
        return dataSetSearchService.readEsDataSet(Lists.newArrayList(dataSetRsp.getDbName()),
                Lists.newArrayList(dataSetRsp.getTbName()), nameReadReq.getFields(), nameReadReq.getQuery(), nameReadReq.getSort(), pageUtils.getOffset(), pageUtils.getLimit());
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

    @Override
    public void sparkSqlExport(String kgName, ExportTypeEnum exportType, SparQlReq sparQlReq, HttpServletResponse response) throws IOException {

        //查询搜索结果
        Optional<QueryResultVO> resOpt = RestRespConverter.convert(sparqlApi.query(kgName, sparQlReq.getQuery(), sparQlReq.getSize()));
        if (!resOpt.isPresent()) {
            return;
        }
        List<List<NodeBean>> sparQlNodeBeans = resOpt.get().getResult();
        String exportName = "sparql_" + kgName + "_" + System.currentTimeMillis();

        if (ExportTypeEnum.TXT.equals(exportType)) {
            //导出txt
            TextUtils.exportJson(exportName, JsonUtils.toJson(sparQlNodeBeans), response);
            return;
        }
        List<List<String>> titleList = Lists.newArrayList();
        List<List<String>> valueList = Lists.newArrayList();
        if (sparQlNodeBeans != null && sparQlNodeBeans.size() > 0) {
            List<NodeBean> sparQlNodeBeanList = sparQlNodeBeans.get(0);
            if (sparQlNodeBeanList.size() > 0) {
                for (NodeBean sparQlNodeBean : sparQlNodeBeanList) {
                    titleList.add(Lists.newArrayList(sparQlNodeBean.getKey()));
                    valueList.add(Lists.newArrayList(sparQlNodeBean.getValue()));
                }
            }
        }
        ExcelTypeEnum excelType = ExportTypeEnum.XLS.equals(exportType) ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
        EasyExcelFactory.write().file(response.getOutputStream()).head(titleList).excelType(excelType).sheet(0, exportName).doWrite(valueList);
    }

}



