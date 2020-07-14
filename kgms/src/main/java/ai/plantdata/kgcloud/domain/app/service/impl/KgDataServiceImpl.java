package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.req.BasicInfoListFrom;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.SparqlApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.EntityRelationDegreeFrom;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.api.pub.req.statistics.AttributeStatisticsBean;
import ai.plantdata.kg.api.pub.req.statistics.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.statistics.RelationExtraInfoStatisticBean;
import ai.plantdata.kg.api.pub.req.statistics.RelationStatisticsBean;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.NodeBean;
import ai.plantdata.kg.api.pub.resp.QueryResultVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.constant.AppConstants;
import ai.plantdata.kgcloud.constant.ExportTypeEnum;
import ai.plantdata.kgcloud.constant.StatisticResultTypeEnum;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.converter.EntityConverter;
import ai.plantdata.kgcloud.domain.app.converter.GraphStatisticConverter;
import ai.plantdata.kgcloud.domain.app.converter.TraceabilityConverter;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.sdk.req.app.statistic.*;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.domain.app.bo.GraphAttributeStatisticBO;
import ai.plantdata.kgcloud.domain.app.bo.GraphRelationStatisticBO;
import ai.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.service.KgDataService;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.domain.app.util.PageUtils;
import ai.plantdata.kgcloud.domain.app.util.TextUtils;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.EdgeStatisticByEntityIdRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.StatDataRsp;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 20:39
 */
@Service
public class KgDataServiceImpl implements KgDataService {
    @Autowired
    public EntityApi entityApi;
    @Autowired
    public GraphApi graphApi;
    @Autowired
    public AttributeApi attributeApi;
    @Autowired
    public StatisticsApi statisticsApi;
    @Autowired
    public SparqlApi sparqlApi;
    @Autowired
    public ConceptEntityApi conceptEntityApi;
    @Autowired
    private GraphHelperService graphHelperService;

    @Override
    public List<EdgeStatisticByEntityIdRsp> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq) {
        EntityRelationDegreeFrom degreeFrom = GraphRelationStatisticBO.buildDegreeFrom(statisticReq);
        degreeFrom.setDirection(NumberUtils.INTEGER_ONE);
        Optional<Map<Integer, Integer>> countOneOpt = RestRespConverter.convert(graphApi.degree(KGUtil.dbName(kgName), degreeFrom));
        degreeFrom.setDirection(NumberUtils.INTEGER_MINUS_ONE);
        Optional<Map<Integer, Integer>> countTwoOpt = RestRespConverter.convert(graphApi.degree(KGUtil.dbName(kgName), degreeFrom));
        return GraphRelationStatisticBO.graphDegreeMapToList(countOneOpt.orElse(Collections.emptyMap()), countTwoOpt.orElse(Collections.emptyMap()));
    }

    @Override
    public Object statEdgeGroupByEdgeValue(String kgName, EdgeAttrStatisticByAttrValueReq statisticReq) {
        graphHelperService.replaceByAttrKey(kgName, statisticReq, true);
        Optional<AttributeDefinition> arrDefOpt = getAttrDefById(kgName, statisticReq.getAttrDefId());

        if (!arrDefOpt.isPresent()) {
            return new StatDataRsp();
        }
        RelationExtraInfoStatisticBean statisticBean = GraphStatisticConverter.edgeAttrReqToRelationExtraInfoStatisticBean(statisticReq);
        Optional<List<Map<String, Object>>> dataOpt = RestRespConverter.convert(statisticsApi.relationExtraInfoStatistic(KGUtil.dbName(kgName), statisticBean));
        if (!dataOpt.isPresent()) {
            return new StatDataRsp();
        }
        List<StatisticDTO> dataList = JsonUtils.objToList(dataOpt.get(), StatisticDTO.class);
        AttributeDataTypeEnum dataType = GraphStatisticConverter.edgeAttrDataType(statisticReq.getSeqNo(), arrDefOpt.get());
        boolean isFromToTime = AttributeDataTypeEnum.FROM_TO_TIME_SET.contains(statisticReq.getSeqNo());
        return buildStatisticResult(dataType, dataList, statisticReq.getDateType(), isFromToTime, statisticReq.getMerge(),
                statisticReq.getSort(), statisticBean.getLimit(), statisticReq.getReturnType());
    }

    @Override
    public Object statEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq) {
        graphHelperService.replaceByConceptKey(kgName, statisticReq);
        ConceptStatisticsBean statisticsBean = GraphStatisticConverter.entityReqConceptStatisticsBean(statisticReq);

        Optional<List<Map<String, Object>>> resultOpt = RestRespConverter.convert(statisticsApi.conceptStatistics(KGUtil.dbName(kgName), statisticsBean));
        if (!resultOpt.isPresent()) {
            return new StatDataRsp();
        }
        List<StatisticDTO> dataList = JsonUtils.objToList(resultOpt.get(), StatisticDTO.class);
        return GraphStatisticConverter.statisticByType(dataList, statisticReq.getReturnType(), StatisticResultTypeEnum.NAME);
    }

    @Override
    public Object statisticRelation(String kgName, EdgeStatisticByConceptIdReq conceptIdReq) {
        graphHelperService.replaceByAttrKey(kgName, conceptIdReq);
        graphHelperService.replaceByConceptKey(kgName, conceptIdReq);
        RelationStatisticsBean statisticsBean = GraphStatisticConverter.conceptIdReqConceptStatisticsBean(conceptIdReq);
        Optional<List<Map<String, Object>>> resultOpt = RestRespConverter.convert(statisticsApi.relationStatistics(KGUtil.dbName(kgName), statisticsBean));
        List<StatisticDTO> dataList = !resultOpt.isPresent() ? Collections.emptyList()
                : JsonUtils.objToList(resultOpt.get(), StatisticDTO.class);
        return GraphStatisticConverter.statisticByType(dataList, conceptIdReq.getReturnType(), StatisticResultTypeEnum.NAME);
    }

    @Override
    public Object statisticAttrGroupByConcept(String kgName, EntityStatisticGroupByAttrIdReq attrIdReq) {
        graphHelperService.replaceByAttrKey(kgName, attrIdReq, true);
        Optional<AttributeDefinition> attrDefOpt = getAttrDefById(kgName, attrIdReq.getAttrDefId());
        if (!attrDefOpt.isPresent()) {
            return GraphStatisticConverter.statisticByType(Collections.emptyList(), attrIdReq.getReturnType(), StatisticResultTypeEnum.VALUE);
        }
        Integer valueType = attrDefOpt.get().getType();
        AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(attrDefOpt.get().getDataType());
        int reSize = GraphStatisticConverter.reBuildResultSize(attrIdReq.getSize(), valueType, dataType);

        List<StatisticDTO> dataList = Collections.emptyList();
        AttributeStatisticsBean statisticsBean = GraphStatisticConverter.attrReqToAttributeStatisticsBean(reSize, attrIdReq);
        Optional<List<Map<String, Object>>> mapOpt = RestRespConverter.convert(statisticsApi.attributeStatistics(KGUtil.dbName(kgName), statisticsBean));
        if (mapOpt.isPresent()) {
            dataList = JsonUtils.objToList(mapOpt.get(), StatisticDTO.class);
        }
        return buildStatisticResult(dataType, dataList, attrIdReq.getDataType(), false, attrIdReq.getMerge(),
                attrIdReq.getSort(), reSize, attrIdReq.getReturnType());
    }

    private Optional<AttributeDefinition> getAttrDefById(String kgName, Integer attrId) {
        Optional<List<AttributeDefinition>> opt = RestRespConverter.convert(attributeApi.listByIds(KGUtil.dbName(kgName), Lists.newArrayList(attrId)));
        if (opt.isPresent() && !CollectionUtils.isEmpty(opt.get())) {
            return Optional.of(opt.get().get(0));
        }
        return Optional.empty();
    }


    private Object buildStatisticResult(AttributeDataTypeEnum dataType, List<StatisticDTO> dataList, DateTypeReq dateType,
                                        boolean isFromToTime, boolean merge, int sort, int reSize, int returnType) {
        boolean isNum = AttributeDataTypeEnum.DATA_VALUE_SET.contains(dataType);
        boolean isDate = AttributeDataTypeEnum.DATE_SET.contains(dataType);
        //merge+数字类型
        if (merge && isNum) {
            dataList = GraphAttributeStatisticBO.countMerge(dataList, dataType, AppConstants.COUNT_MERGE_SIZE);
        }
        //merge/开始结束时间+时间类型
        if ((merge || isFromToTime) && isDate) {
            dataList = GraphAttributeStatisticBO.countMergeDate(dataList, dateType);
        }
        //merge+反序
        if (merge && sort == -1 && !CollectionUtils.isEmpty(dataList)) {
            Collections.reverse(dataList);
        }
        dataList = PageUtils.subList(NumberUtils.INTEGER_ZERO, reSize, dataList);
        return GraphStatisticConverter.statisticByType(dataList, returnType, StatisticResultTypeEnum.VALUE);
    }

    @Override
    public void sparkSqlExport(String kgName, ExportTypeEnum exportType, String query, int size, HttpServletResponse response) throws IOException {

        //查询搜索结果
        Optional<QueryResultVO> resOpt = RestRespConverter.convert(sparqlApi.query(KGUtil.dbName(kgName), query, size));
        if (!resOpt.isPresent()) {
            return;
        }
        List<List<NodeBean>> sparQlNodeBeans = resOpt.get().getResult();
        String exportName = "sparql_" + kgName + "_" + System.currentTimeMillis();

        if (ExportTypeEnum.TXT.equals(exportType)) {
            //导出txt
            TextUtils.exportJson(exportName, JacksonUtils.writeValueAsString(sparQlNodeBeans), response);
            return;
        }
        List<List<String>> titleList = Lists.newArrayList();
        List<List<String>> valueList = Collections.emptyList();
        if (sparQlNodeBeans != null && sparQlNodeBeans.size() > 0) {
            List<NodeBean> sparQlNodeBeanList = sparQlNodeBeans.get(0);
            if (sparQlNodeBeanList.size() > 0) {
                for (NodeBean sparQlNodeBean : sparQlNodeBeanList) {
                    titleList.add(Lists.newArrayList(sparQlNodeBean.getKey()));
                }
            }
            valueList = sparQlNodeBeans.stream().map(a -> BasicConverter.listConvert(a, NodeBean::getValue)).collect(Collectors.toList());
        }
        ExcelTypeEnum excelType = ExportTypeEnum.XLS.equals(exportType) ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
        // 设置response参数
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((exportName + excelType.getValue()).getBytes(), "iso-8859-1"));
        EasyExcelFactory.write().file(response.getOutputStream()).head(titleList).excelType(excelType).sheet(0, "data").doWrite(valueList);
    }

    @Override
    public List<OpenEntityRsp> queryEntityByNameAndMeaningTag(String kgName, List<EntityQueryWithConditionReq> conditionReqs) {
        List<BasicInfo> basicInfoList = BasicConverter.listToRsp(conditionReqs, EntityConverter.entityQueryWithConditionReqToBasicInfo);
        Optional<Map<String, Set<Long>>> entityIdListOpt = RestRespConverter.convert(entityApi.getIdByNameAndMeaningTag(kgName, basicInfoList));
        if (!entityIdListOpt.isPresent()) {
            return Collections.emptyList();
        }
        Set<Long> entityIdSet = entityIdListOpt.get().values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        KgServiceEntityFrom entityFrom = EntityConverter.buildIdsQuery(entityIdSet,true);
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(kgName, entityFrom));
        if (!entityOpt.isPresent()) {
            return Collections.emptyList();
        }
        return BasicConverter.listToRsp(entityOpt.get(), EntityConverter::voToOpenEntityRsp);
    }

    @Override
    public BasePage<OpenEntityRsp> queryEntityBySource(String kgName, TraceabilityQueryReq req) {
        BasicInfoListFrom basicInfoListFrom = TraceabilityConverter.traceabilityToMetaDataQuery(req);
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        basicInfoListFrom.setSkip(page);
        basicInfoListFrom.setLimit(size + 1);
        RestResp<List<ai.plantdata.kg.api.edit.resp.EntityVO>> restResp = conceptEntityApi.list(KGUtil.dbName(kgName),
                basicInfoListFrom);
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> optional = RestRespConverter.convert(restResp);
        List<OpenEntityRsp> basicInfoRspList =
                optional.orElse(new ArrayList<>()).stream().map(EntityConverter::voToEditOpenEntityRsp).collect(Collectors.toList());
        int count = basicInfoRspList.size();
        if (count > size) {
            basicInfoRspList.remove(size.intValue());
            count += page;
        }

        return new BasePage<>(count,basicInfoRspList);
    }
}



