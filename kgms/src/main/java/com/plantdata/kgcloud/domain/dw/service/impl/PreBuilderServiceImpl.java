package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.DataTypeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.access.rsp.KgConfigRsp;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.service.GraphEditService;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dw.entity.*;
import com.plantdata.kgcloud.domain.dw.parser.ExcelParser;
import com.plantdata.kgcloud.domain.dw.repository.*;
import com.plantdata.kgcloud.domain.dw.req.ModelPushReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCreateReq;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderUpdateReq;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.GraphMapService;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.req.edit.ExtraInfoVO;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class PreBuilderServiceImpl implements PreBuilderService {


    @Autowired
    private DWPrebuildModelRepository prebuildModelRepository;

    @Autowired
    private DWPrebuildConceptRepository prebuildConceptRepository;

    @Autowired
    private DWPrebuildAttrRepository prebuildAttrRepository;

    @Autowired
    private DWPrebuildRelationAttrRepository prebuildRelationAttrRepository;

    @Autowired
    private DWGraphMapRepository graphMapRepository;

    @Autowired
    private GraphApplicationService graphApplicationService;

    @Autowired
    private GraphEditService graphEditService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private DWGraphMapRelationAttrRepository graphMapRelationAttrRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private AccessTaskService accessTaskService;

    @Autowired
    private FastdfsTemplate fastdfsTemplate;

    @Autowired
    private DWService dwService;

    @Autowired
    private GraphMapService graphMapService;

    private final static String JSON_START = "{";
    private final static String ARRAY_START = "[";
    private final static String ARRAY_STRING_START = "[\"";

    private UserDetailRsp getUserDetail() {
        return userClient.getCurrentUserDetail().getData();
    }

    @Override
    public Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq req) {

        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());

        if (!req.isGraph() && !req.isManage() && !req.isUser() && !req.isDw()) {
            return Page.empty();
        }

        Specification<DWPrebuildModel> specification = new Specification<DWPrebuildModel>() {
            @Override
            public Predicate toPredicate(Root<DWPrebuildModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(req.getKw())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getKw() + "%");
                    predicates.add(likename);
                }

                List<Predicate> tags = new ArrayList<>();
                if (req.isDw()) {
                    tags.add(criteriaBuilder.isNotNull(root.get("databaseId")));
                }

                if (req.isGraph()) {
                    tags.add(criteriaBuilder.isNull(root.get("databaseId")));
                }

                if (req.isManage()) {
                    tags.add(criteriaBuilder.equal(root.get("username").as(String.class), "admin"));
                }

                if (req.isUser()) {
                    tags.add(criteriaBuilder.notEqual(root.get("username").as(String.class), "admin"));
                }

                predicates.add(criteriaBuilder.or(tags.toArray(new Predicate[]{})));

                if (!StringUtils.isEmpty(req.getModelType())) {

                    Predicate modelTypeEq = criteriaBuilder.equal(root.get("modelType").as(String.class), req.getModelType());
                    predicates.add(modelTypeEq);
                }

                //查询管理员发布公开的或者自己发布的
                Predicate isPublic = criteriaBuilder.equal(root.get("permission").as(Integer.class), 1);
                Predicate isPrivate = criteriaBuilder.and(criteriaBuilder.equal(root.get("permission").as(Integer.class), 0), criteriaBuilder.equal(root.get("userId").as(String.class), SessionHolder.getUserId()));
                predicates.add(criteriaBuilder.or(isPublic, isPrivate));

                //只能查询发布过的
                predicates.add(criteriaBuilder.equal(root.get("status").as(String.class), "1"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWPrebuildModel> all = prebuildModelRepository.findAll(specification, pageable);

        Page<PreBuilderSearchRsp> map = all.map(ConvertUtils.convert(PreBuilderSearchRsp.class));

        List<PreBuilderSearchRsp> modelRsps = map.getContent();
        for (PreBuilderSearchRsp modelSearch : modelRsps) {

            setModelSchema(modelSearch, false);
        }

        return map;
    }

    @Override
    public Page<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq req) {

        SchemaRsp schemaRsp = graphApplicationService.querySchema(req.getKgName());

        Map<String,Long> conceptNameMap = new HashMap<>();

        //概念名称映射属性名称与属性
        if (schemaRsp != null && schemaRsp.getTypes() != null && !schemaRsp.getTypes().isEmpty()) {

            schemaRsp.getTypes().forEach(c -> {
                conceptNameMap.put(c.getName(),c.getId());
            });
        }

        //已引入的概念属性
        List<SchemaQuoteReq> dataMapReqList = req.getSchemaQuoteReqList();
//        megerSchemaQuote(dataMapReqList,);
        List<SchemaQuoteReq> existMap =  getGraphMap(userId, req.getKgName(),true);
//        List<SchemaQuoteReq> existMap =  getGraphMap(userClient.getCurrentUserDetail().getData().getId(), req.getKgName(),true);
//        megerSchemaQuote(dataMapReqList,existMap);

        List<DWPrebuildConcept> concepts;
        if (req.getFindAttrConceptIds() != null && !req.getFindAttrConceptIds().isEmpty()) {
            concepts = prebuildConceptRepository.findByModelAndConceptIds(req.getModelId(), req.getFindAttrConceptIds());
        } else {
            concepts = prebuildConceptRepository.findAll(Example.of(DWPrebuildConcept.builder().modelId(req.getModelId()).build()));
        }

        if (concepts == null || concepts.isEmpty()) {
            return Page.empty();
        }

        Map<Integer, String> modelConceptNameMap = new HashMap<>();

        //概念名称映射
        for (DWPrebuildConcept concept : concepts) {
            modelConceptNameMap.put(concept.getId(), concept.getName());
        }

        List<Integer> findByConceptIds = concepts.stream().map(DWPrebuildConcept::getId).collect(Collectors.toList());

        List<DWPrebuildAttr> attrs = prebuildAttrRepository.findByConceptIds(findByConceptIds);
        if (attrs == null || attrs.isEmpty()) {
            return Page.empty();
        }

        List<PreBuilderMatchAttrRsp> matchAttrRspList = attrs.stream().map(ConvertUtils.convert(PreBuilderMatchAttrRsp.class))
                .collect(Collectors.toList());

        Map<String, Long> modelKgConceptIdMap = new HashMap<>();
        Map<String, String> modelKgConceptNameMap = new HashMap<>();

        //已引入的shcema概念名称-属性名称-属性类型映射
        Map<String, List<SchemaQuoteReq>> conceptQuoteMap = new HashMap<>();
        Map<String, Map<String, List<SchemaQuoteAttrReq>>> conceptAttrQuoteMap = new HashMap<>();
        if (dataMapReqList != null && !dataMapReqList.isEmpty()) {
            for (SchemaQuoteReq schemaQuoteReq : dataMapReqList) {

                if(req.getConceptIds() == null){
                    req.setConceptIds(Lists.newArrayList());
                }
                if (!req.getConceptIds().contains(schemaQuoteReq.getModelConceptId())) {
                    req.getConceptIds().add(schemaQuoteReq.getModelConceptId());
                }

                modelKgConceptIdMap.put(schemaQuoteReq.getEntityName()+schemaQuoteReq.getModelId(), schemaQuoteReq.getConceptId());
                modelKgConceptNameMap.put(schemaQuoteReq.getEntityName()+schemaQuoteReq.getModelId(), schemaQuoteReq.getConceptName());

                if(conceptQuoteMap.containsKey(schemaQuoteReq.getConceptName())){
                    conceptQuoteMap.get(schemaQuoteReq.getConceptName()).add(schemaQuoteReq);
                }else{
                    conceptQuoteMap.put(schemaQuoteReq.getConceptName(), Lists.newArrayList(schemaQuoteReq));
                }

                Map<String, List<SchemaQuoteAttrReq>> quoteAttrReqMap = conceptAttrQuoteMap.containsKey(schemaQuoteReq.getConceptName()) ? conceptAttrQuoteMap.get(schemaQuoteReq.getConceptName()) : new HashMap<>();

                if (schemaQuoteReq.getAttrs() != null) {
                    for (SchemaQuoteAttrReq quoteAttrReq : schemaQuoteReq.getAttrs()) {
                        quoteAttrReq.setModelId(schemaQuoteReq.getModelId());
                        if(quoteAttrReqMap.containsKey(quoteAttrReq.getAttrName())){
                            quoteAttrReqMap.get(quoteAttrReq.getAttrName()).add(quoteAttrReq);
                        }else{
                            quoteAttrReqMap.put(quoteAttrReq.getAttrName(),Lists.newArrayList(quoteAttrReq));
                        }
                    }
                }

                conceptAttrQuoteMap.put(schemaQuoteReq.getConceptName(), quoteAttrReqMap);
            }
        }

        Map<String, List<SchemaQuoteReq>> existConceptQuoteMap = new HashMap<>();
        if(existMap != null && !existMap.isEmpty()){
            for (SchemaQuoteReq schemaQuoteReq : existMap) {

                if(existConceptQuoteMap.containsKey(schemaQuoteReq.getConceptName())){
                    existConceptQuoteMap.get(schemaQuoteReq.getConceptName()).add(schemaQuoteReq);
                }else{
                    existConceptQuoteMap.put(schemaQuoteReq.getConceptName(), Lists.newArrayList(schemaQuoteReq));
                }

            }
        }

        for (PreBuilderMatchAttrRsp matchAttrRsp : matchAttrRspList) {

            matchAttrRsp.setConceptName(modelConceptNameMap.get(matchAttrRsp.getConceptId()));

            String status;
            Integer matchStatus;
            if (req.getConceptIds() == null ||!req.getConceptIds().contains(matchAttrRsp.getConceptId())) {
                //概念还未引入，不能引入属性
                status = "-";
                matchStatus = 0;

                matchAttrRsp.setAttrMatchStatus(status);
                matchAttrRsp.setMatchStatus(matchStatus);
                continue;
            }


            //映射到图谱的概念名
            String conceptName = matchAttrRsp.getConceptName();

            Long conceptId = conceptNameMap.get(conceptName);

            Map<String,AttrDefinitionRsp> graphAttrMap = Maps.newHashMap();
            if(conceptId != null){

                AttrDefinitionSearchReq attrDefinitionSearchReq = new AttrDefinitionSearchReq();
                attrDefinitionSearchReq.setConceptId(conceptId);
                List<AttrDefinitionRsp> attrDefinitionRspList = attributeService.getAttrDefinitionByConceptId(req.getKgName(), attrDefinitionSearchReq);
                if(attrDefinitionRspList != null && !attrDefinitionRspList.isEmpty()){
                    graphAttrMap = attrDefinitionRspList.stream().collect(Collectors.toMap(AttrDefinitionRsp::getName,Function.identity()));
                }

            }

            //该图谱概念映射了哪些模式
            List<SchemaQuoteReq> quoteConceptList = conceptQuoteMap.get(conceptName);

            //该概念映射过属性
            Map<Integer,SchemaQuoteAttrReq> modelAttrIds = Maps.newHashMap();
            Map<String, List<SchemaQuoteAttrReq>> quoteAttrReqMap = new HashMap<>();

            if(quoteConceptList != null && !quoteConceptList.isEmpty()){

                for(SchemaQuoteReq s : quoteConceptList){

                    if(s.getAttrs() == null || s.getAttrs().isEmpty()){
                        continue;
                    }

                    for (SchemaQuoteAttrReq quoteAttrReq : s.getAttrs()) {
                        quoteAttrReq.setModelId(s.getModelId());
                        if(quoteAttrReqMap.containsKey(quoteAttrReq.getAttrName())){
                            quoteAttrReqMap.get(quoteAttrReq.getAttrName()).add(quoteAttrReq);
                        }else{
                            quoteAttrReqMap.put(quoteAttrReq.getAttrName(),Lists.newArrayList(quoteAttrReq));
                        }
                    }
                }

                List<SchemaQuoteReq> existSchemaQuoteList = existConceptQuoteMap.get(conceptName);
                if(existSchemaQuoteList != null){
                    for(SchemaQuoteReq exist : existSchemaQuoteList){
                        if(exist.getAttrs() == null || exist.getAttrs().isEmpty()){
                            continue;
                        }

                        modelAttrIds.putAll(exist.getAttrs().stream().collect(Collectors.toMap(SchemaQuoteAttrReq::getModelAttrId,Function.identity())));
                    }
                }

                if(modelAttrIds.containsKey(matchAttrRsp.getId())){

                    //同模式同概念同名属性已引入
                    status = "已引入";
                    matchStatus = 1;
                    matchAttrRsp.setAttrId(modelAttrIds.get(matchAttrRsp.getId()).getAttrId());

                }else if(graphAttrMap.containsKey(matchAttrRsp.getName())){

                    //未引入过同名属性但是图谱中存在同名属性
                    AttrDefinitionRsp attrDefinitionRsp = graphAttrMap.get(matchAttrRsp.getName());
                    if (!attrDefinitionRsp.getType().equals(matchAttrRsp.getAttrType())) {

                        //不类型不匹配，冲突
                        status = "属性类型冲突";
                        matchStatus = 2;

                    } else if (matchAttrRsp.getAttrType() == 0) {

                        //都为数值属性
                        if (matchAttrRsp.getDataType().equals(attrDefinitionRsp.getDataType())) {
                            matchAttrRsp.setAttrId(attrDefinitionRsp.getId());
                            status = "完全匹配，可引入";
                            matchStatus = 3;

                        } else {
                            status = "数值属性类型冲突";
                            matchStatus = 2;
                        }
                    } else {

                        //都为对象属性,值域一样
                        List<Long> ranges = attrDefinitionRsp.getRangeValue();
                        Long modelRange = modelKgConceptIdMap.get(matchAttrRsp.getRangeName()+matchAttrRsp.getModelId());
                        if (ranges.contains(modelRange)) {
                            status = "完全匹配，可引入";
                            matchAttrRsp.setAttrId(attrDefinitionRsp.getId());
                            matchStatus = 3;
                        } else {
                            status = "对象属性值域冲突";
                            matchStatus = 2;
                        }
                    }


                }else if(quoteAttrReqMap.containsKey(matchAttrRsp.getName())){

                    //引用过同名属性

                    List<SchemaQuoteAttrReq> schemaQuoteAttrReqList = quoteAttrReqMap.get(matchAttrRsp.getName());
                    Integer attrType = schemaQuoteAttrReqList.get(0).getAttrType();
                    Integer dataType = schemaQuoteAttrReqList.get(0).getDataType();

                    if (!attrType.equals(matchAttrRsp.getAttrType())) {

                        //不类型不匹配，冲突
                        status = "属性类型冲突";
                        matchStatus = 2;

                    } else if (matchAttrRsp.getAttrType() == 0) {

                        //不同模式，都为数值属性
                        if (matchAttrRsp.getDataType().equals(dataType)) {
                            if (graphAttrMap.containsKey(matchAttrRsp.getName())) {
                                status = "完全匹配，可引入";
                                matchAttrRsp.setAttrId(graphAttrMap.get(matchAttrRsp.getName()).getId());
                            } else {
                                status = "新增，可引入";
                            }

                            matchStatus = 3;

                        } else {
                            status = "数值属性类型冲突";
                            matchStatus = 2;
                        }
                    } else {

                        //都为对象属性,值域一样
                        Long modelRange = modelKgConceptIdMap.get(schemaQuoteAttrReqList.get(0).getRangeName()+schemaQuoteAttrReqList.get(0).getModelId());
                        if (modelRange != null && modelRange.equals(modelKgConceptIdMap.get(matchAttrRsp.getRangeName()+matchAttrRsp.getModelId()))) {
                            status = "新增，可引入";
                            matchStatus = 3;
                        }else if(modelRange == null){

                            String modelRangeName = modelKgConceptNameMap.get(schemaQuoteAttrReqList.get(0).getRangeName()+schemaQuoteAttrReqList.get(0).getModelId());
                            if(modelRangeName != null && modelRangeName.equals(modelKgConceptNameMap.get(matchAttrRsp.getRangeName()+matchAttrRsp.getModelId()))){
                                status = "新增，可引入";
                                matchStatus = 3;
                            }else{
                                status = "对象属性值域冲突";
                                matchStatus = 2;
                            }

                        } else {
                            status = "对象属性值域冲突";
                            matchStatus = 2;
                        }
                    }

                }else{
                    //未引入过该属性，图谱中也未有同名属性
                    if (matchAttrRsp.getAttrType() == 0) {

                        status = "新增，可引入";
                        matchStatus = 3;

                    } else {

                        //对象属性,值域已引入
                        if (req.getConceptIds().contains(matchAttrRsp.getRange())) {
                            status = "新增，可引入";
                            matchStatus = 3;
                        } else {
                            status = matchAttrRsp.getRangeName() + "概念未挂载";
                            matchStatus = 2;
                        }
                    }
                }

            }else if(graphAttrMap.containsKey(matchAttrRsp.getName())){

                //未引入过同名属性但是图谱中存在同名属性
                AttrDefinitionRsp attrDefinitionRsp = graphAttrMap.get(matchAttrRsp.getName());
                if (!attrDefinitionRsp.getType().equals(matchAttrRsp.getAttrType())) {

                    //不类型不匹配，冲突
                    status = "属性类型冲突";
                    matchStatus = 2;

                } else if (matchAttrRsp.getAttrType() == 0) {

                    //都为数值属性
                    if (matchAttrRsp.getDataType().equals(attrDefinitionRsp.getDataType())) {
                        matchAttrRsp.setAttrId(attrDefinitionRsp.getId());
                        status = "完全匹配，可引入";
                        matchStatus = 3;

                    } else {
                        status = "数值属性类型冲突";
                        matchStatus = 2;
                    }
                } else {

                    //都为对象属性,值域一样
                    List<Long> ranges = attrDefinitionRsp.getRangeValue();
                    Long modelRange = modelKgConceptIdMap.get(matchAttrRsp.getRangeName()+matchAttrRsp.getModelId());
                    if (ranges.contains(modelRange)) {
                        status = "完全匹配，可引入";
                        matchAttrRsp.setAttrId(attrDefinitionRsp.getId());
                        matchStatus = 3;
                    } else {
                        status = "对象属性值域冲突";
                        matchStatus = 2;
                    }
                }


            }else {
                //该图谱什么都没有映射
                if (matchAttrRsp.getAttrType() == 0) {

                    status = "新增，可引入";
                    matchStatus = 3;

                } else {

                    //对象属性,值域已引入
                    if (req.getConceptIds().contains(matchAttrRsp.getRange())) {
                        status = "新增，可引入";
                        matchStatus = 3;
                    } else {
                        status = matchAttrRsp.getRangeName() + "概念未挂载";
                        matchStatus = 2;
                    }
                }
            }

            matchAttrRsp.setAttrMatchStatus(status);
            matchAttrRsp.setMatchStatus(matchStatus);

            if (matchAttrRsp.getAttrType().equals(1) && (matchStatus.equals(1) ||matchStatus.equals(3))) {
                //可引入/已存在的对象属性，看边属性状态

                List<DWPrebuildRelationAttr> relationAttrList = prebuildRelationAttrRepository.findAll(Example.of(DWPrebuildRelationAttr.builder().attrId(matchAttrRsp.getId()).build()));
                if (relationAttrList == null || relationAttrList.isEmpty()) {
                    continue;
                }

                List<PreBuilderRelationAttrRsp> matchRelationAttrList = new ArrayList<>();
                for (DWPrebuildRelationAttr relationAttr : relationAttrList) {
                    PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                    BeanUtils.copyProperties(relationAttr, relationAttrRsp);

                    if (modelAttrIds.containsKey(matchAttrRsp.getId())) {
                        SchemaQuoteAttrReq attrReq = modelAttrIds.get(matchAttrRsp.getId());
                        List<String> quoteRelationAttrNames = Lists.newArrayList();
                        if(attrReq.getRelationAttrs() != null && !attrReq.getRelationAttrs().isEmpty()){
                            quoteRelationAttrNames = attrReq.getRelationAttrs().stream().map(SchemaQuoteRelationAttrReq::getName).collect(Collectors.toList());
                        }

                        if(quoteRelationAttrNames.contains(relationAttr.getName())){

                            relationAttrRsp.setAttrMatchStatus("已引入");
                            relationAttrRsp.setMatchStatus(1);
                            matchRelationAttrList.add(relationAttrRsp);
                            continue;
                        }

                    }

                    if(quoteAttrReqMap.containsKey(matchAttrRsp.getName())) {

                        List<SchemaQuoteAttrReq> schemaQuoteAttrReqList = quoteAttrReqMap.get(matchAttrRsp.getName());

                        Map<String, Integer> quoteRelationNameMap = Maps.newHashMap();
                        if (schemaQuoteAttrReqList != null && !schemaQuoteAttrReqList.isEmpty()) {
                            schemaQuoteAttrReqList.forEach(schemaQuoteAttrReq -> {

                                if (schemaQuoteAttrReq.getRelationAttrs() != null && !schemaQuoteAttrReq.getRelationAttrs().isEmpty()) {

                                    schemaQuoteAttrReq.getRelationAttrs().forEach(re -> quoteRelationNameMap.put(re.getName(), re.getDataType()));

                                }
                            });
                        }

                        if (quoteRelationNameMap.containsKey(relationAttr.getName())) {

                            if (quoteRelationNameMap.get(relationAttr.getName()).equals(relationAttr.getDataType())) {
                                relationAttrRsp.setAttrMatchStatus("新增，可引入");
                                relationAttrRsp.setMatchStatus(3);
                            } else {
                                relationAttrRsp.setAttrMatchStatus("数值属性类型冲突");
                                relationAttrRsp.setMatchStatus(2);
                            }

                        }

                    }

                    //之前未印过同名属性
                    if(relationAttrRsp.getAttrMatchStatus() == null){

                        AttrDefinitionRsp attrDefinitionRsp = graphAttrMap.get(matchAttrRsp.getName());
                        Map<String, ExtraInfoVO> extraInfoVOMap = Maps.newHashMap();

                        if (attrDefinitionRsp != null && attrDefinitionRsp.getExtraInfo() != null && !attrDefinitionRsp.getExtraInfo().isEmpty()) {
                            extraInfoVOMap = attrDefinitionRsp.getExtraInfo().stream().collect(Collectors.toMap(ExtraInfoVO::getName, Function.identity()));
                        }

                        if (extraInfoVOMap.containsKey(relationAttrRsp.getName())) {

                            ExtraInfoVO extraInfoVO = extraInfoVOMap.get(relationAttrRsp.getName());

                            if (extraInfoVO.getType().equals(1)) {

                                relationAttrRsp.setAttrMatchStatus("属性类型冲突");
                                relationAttrRsp.setMatchStatus(2);

                            } else if (!extraInfoVO.getDataType().equals(relationAttrRsp.getDataType())) {

                                relationAttrRsp.setAttrMatchStatus("数值属性类型冲突");
                                relationAttrRsp.setMatchStatus(2);

                            } else {

                                relationAttrRsp.setAttrMatchStatus("完全匹配，可引入");
                                relationAttrRsp.setMatchStatus(3);

                            }

                        } else {

                            relationAttrRsp.setAttrMatchStatus("新增，可引入");
                            relationAttrRsp.setMatchStatus(3);

                        }
                    }

                    matchRelationAttrList.add(relationAttrRsp);

                }

                matchAttrRsp.setRelationAttrs(matchRelationAttrList);

            }

        }

        if(matchAttrRspList != null && req.getMatchStatus() != null){
            matchAttrRspList = matchAttrRspList.stream().filter(attr -> req.getMatchStatus().equals(attr.getMatchStatus())).collect(Collectors.toList());
        }

        List<PreBuilderMatchAttrRsp> rsList = subList(matchAttrRspList,req.getPage(),req.getSize());

        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        Page<PreBuilderMatchAttrRsp> page = new PageImpl<>(rsList, pageable, matchAttrRspList.size());

        return page;
    }

    private List<PreBuilderMatchAttrRsp> subList(List<PreBuilderMatchAttrRsp> matchAttrRspList, Integer page, Integer size) {

        if(matchAttrRspList == null || matchAttrRspList.isEmpty()){
            return matchAttrRspList;
        }

        Integer from = (page - 1) * size;

        Integer to = from + size;

        if(matchAttrRspList.size() < from){
            return Lists.newArrayList();
        }

        if(matchAttrRspList.size() > to){
            return matchAttrRspList.subList(from,to);
        }

        return matchAttrRspList.subList(from,matchAttrRspList.size());

    }

    @Override
    public JSONObject saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq) {

        JSONObject isDwModel = new JSONObject();

        if (preBuilderGraphMapReq.getQuoteConfigs() == null || preBuilderGraphMapReq.getQuoteConfigs().isEmpty()) {
            isDwModel.put("isDwModel",false);
            return isDwModel;
        }

        List<SchemaQuoteReq> quoteReqList = importToGraph(preBuilderGraphMapReq.getKgName(), preBuilderGraphMapReq.getQuoteConfigs());

        Map<Integer, Long> modelDataBaseIdMap = new HashMap<>();
        List<DWGraphMap> graphMapList = new ArrayList<>();
        List<DWGraphMapRelationAttr> graphMapRelationAttrList = new ArrayList<>();
        for (SchemaQuoteReq schemaQuoteReq : quoteReqList) {

            if (schemaQuoteReq.getConceptId().equals(0L)) {
                continue;
            }

            Long modelDataBaseId;
            if (modelDataBaseIdMap.containsKey(schemaQuoteReq.getModelId())) {
                modelDataBaseId = modelDataBaseIdMap.get(schemaQuoteReq.getModelId());
            } else {
                Optional<DWPrebuildModel> modelOpt = prebuildModelRepository.findById(schemaQuoteReq.getModelId());
                if(modelOpt.isPresent()){
                    DWPrebuildModel model = modelOpt.get();
                    modelDataBaseId = model.getDatabaseId();
                    modelDataBaseIdMap.put(model.getId(), modelDataBaseId);
                }else{
                    modelDataBaseIdMap.put(schemaQuoteReq.getModelId(), null);
                    modelDataBaseId = null;
                }
            }

            //非数仓模式不保存映射关系
            if (modelDataBaseId == null) {
                continue;
            }

            Optional<DWPrebuildConcept> modelConceptOpt = prebuildConceptRepository.findById(schemaQuoteReq.getModelConceptId());

            if(!modelConceptOpt.isPresent()){
                continue;
            }

            DWPrebuildConcept modelConcept = modelConceptOpt.get();

            List<String> conceptTableName = modelConcept.getTables();

            if (conceptTableName == null || conceptTableName.isEmpty()) {
                continue;
            }


            for (String tableName : conceptTableName) {
                DWGraphMap graphMap = new DWGraphMap();
                BeanUtils.copyProperties(schemaQuoteReq, graphMap);
                graphMap.setTableName(tableName);
                graphMap.setDataBaseId(modelDataBaseId);
                graphMap.setSchedulingSwitch(0);
                graphMap.setKgName(preBuilderGraphMapReq.getKgName());
                graphMapList.add(graphMap);
            }

            List<SchemaQuoteAttrReq> schemaQuoteAttrReqList = schemaQuoteReq.getAttrs();

            if (schemaQuoteAttrReqList == null || schemaQuoteAttrReqList.isEmpty()) {
                continue;
            }

            for (SchemaQuoteAttrReq schemaQuoteAttrReq : schemaQuoteAttrReqList) {

                Optional<DWPrebuildAttr> prebuildAttrOpt = prebuildAttrRepository.findById(schemaQuoteAttrReq.getModelAttrId());

                if(!prebuildAttrOpt.isPresent()){
                    continue;
                }

                DWPrebuildAttr prebuildAttr = prebuildAttrOpt.get();

                List<String> tables = prebuildAttr.getTables();

                if (tables != null && !tables.isEmpty()) {
                    for (String tableName : tables) {
                        DWGraphMap graphMap = new DWGraphMap();
                        BeanUtils.copyProperties(schemaQuoteReq, graphMap);
                        BeanUtils.copyProperties(schemaQuoteAttrReq, graphMap);

                        if (schemaQuoteAttrReq.getAttrType().equals(1)) {
                            graphMap.setModelRange(Lists.newArrayList(schemaQuoteAttrReq.getModelRange()));
                            graphMap.setRange(Lists.newArrayList(schemaQuoteAttrReq.getRange()));
                            graphMap.setRangeName(Lists.newArrayList(schemaQuoteAttrReq.getRangeName()));
                        }

                        graphMap.setTableName(tableName);
                        graphMap.setDataBaseId(modelDataBaseId);
                        graphMap.setSchedulingSwitch(0);
                        graphMap.setKgName(preBuilderGraphMapReq.getKgName());
                        graphMapList.add(graphMap);
                    }
                }


                if (!schemaQuoteAttrReq.getAttrType().equals(1) && schemaQuoteAttrReq.getRelationAttrs() != null && !schemaQuoteAttrReq.getRelationAttrs().isEmpty()) {
                    continue;
                }

                List<SchemaQuoteRelationAttrReq> schemaQuoteRelationAttrReqs = schemaQuoteAttrReq.getRelationAttrs();


                if (schemaQuoteRelationAttrReqs != null && !schemaQuoteRelationAttrReqs.isEmpty()) {

                    for (SchemaQuoteRelationAttrReq schemaQuoteRelationAttrReq : schemaQuoteRelationAttrReqs) {

                        Optional<DWPrebuildRelationAttr> relationAttrOpt = prebuildRelationAttrRepository.findById(schemaQuoteRelationAttrReq.getId());

                        if(!relationAttrOpt.isPresent()){
                            continue;
                        }

                        DWPrebuildRelationAttr relationAttr = relationAttrOpt.get();

                        List<String> tableNames = relationAttr.getTables();

                        if (tableNames != null && !tableNames.isEmpty()) {
                            for (String tableName : tableNames) {

                                DWGraphMapRelationAttr graphMapRelationAttr = new DWGraphMapRelationAttr();
                                BeanUtils.copyProperties(schemaQuoteRelationAttrReq, graphMapRelationAttr);
                                graphMapRelationAttr.setTableName(tableName);
                                graphMapRelationAttr.setDataBaseId(modelDataBaseId);
                                graphMapRelationAttr.setSchedulingSwitch(0);
                                graphMapRelationAttr.setKgName(preBuilderGraphMapReq.getKgName());
                                graphMapRelationAttr.setModelId(schemaQuoteReq.getModelId());
                                graphMapRelationAttr.setModelAttrId(schemaQuoteAttrReq.getModelAttrId());
                                graphMapRelationAttrList.add(graphMapRelationAttr);

                            }
                        }

                    }
                }

            }

        }

        if (!graphMapList.isEmpty()) {
            graphMapRepository.saveAll(graphMapList);
            isDwModel.put("isDwModel",true);
        }

        if (!graphMapRelationAttrList.isEmpty()) {
            graphMapRelationAttrRepository.saveAll(graphMapRelationAttrList);
            isDwModel.put("isDwModel",true);
        }

        if(!isDwModel.containsKey("isDwModel")){
            isDwModel.put("isDwModel",false);
        }

        //生成订阅任务
        createSchedulingConfig(preBuilderGraphMapReq.getKgName(), true, 0);
        return isDwModel;
    }

    @Override
    public void createSchedulingConfig(String kgName, boolean isCreateKtr, Integer status) {

        List<SchemaQuoteReq> schemaQuoteReqList = getGraphMap(SessionHolder.getUserId(), kgName,false);

        if (schemaQuoteReqList == null || schemaQuoteReqList.isEmpty()) {
            return;
        }

        Map<Integer, List<SchemaQuoteReq>> schemaModelMap = new HashMap<>();
        for (SchemaQuoteReq schemaQuoteReq : schemaQuoteReqList) {
            if (schemaModelMap.containsKey(schemaQuoteReq.getModelId())) {
                schemaModelMap.get(schemaQuoteReq.getModelId()).add(schemaQuoteReq);
            } else {
                List<SchemaQuoteReq> schemaQuoteReqs = new ArrayList<>();
                schemaQuoteReqs.add(schemaQuoteReq);
                schemaModelMap.put(schemaQuoteReq.getModelId(), schemaQuoteReqs);
            }
        }

        List<Integer> existModelTaskList = new ArrayList<>();


        for (SchemaQuoteReq schemaQuoteReq : schemaQuoteReqList) {
            Integer modelId = schemaQuoteReq.getModelId();
            if (existModelTaskList.contains(modelId)) {
                continue;
            }

            Optional<DWPrebuildModel> modelOpt = prebuildModelRepository.findById(modelId);
            if (!modelOpt.isPresent()) {
                continue;
            }

            DWPrebuildModel model = modelOpt.get();

            List<String> tableNames = schemaQuoteReq.getTables();
            String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + kgName + "_" + modelId;


            if (tableNames == null || tableNames.isEmpty()) {
                continue;
            }

            if (isCreateKtr) {
                for (String tableName : tableNames) {
                    accessTaskService.createKtrTask(tableName, model.getDatabaseId(), kgName, 0, kgName);
                    accessTaskService.createTransfer(true,model.getId(),tableName, model.getDatabaseId(), null, null, null, null, kgName);
                }
            }

            List<DataMapReq> dataMapReqList = quote2DataMap(schemaModelMap.get(modelId));
            existModelTaskList.add(modelId);

            DWTaskRsp kgTaskRsp = accessTaskService.getByTaskName(kgTaskName);

            if (kgTaskRsp == null) {
                kgTaskRsp = new DWTaskRsp();
                kgTaskRsp.setName(kgTaskName);
                kgTaskRsp.setTaskType(AccessTaskType.KG.getDisplayName());
                kgTaskRsp.setUserId(SessionHolder.getUserId());
                kgTaskRsp.setStatus(status);
                kgTaskRsp.setOutputs(new ArrayList<>());
            }

            KgConfigRsp config = new KgConfigRsp();
            config.setKgName(kgName);
            config.setDataMapping(dataMapReqList);
            config.setIsScheduled(status);
            kgTaskRsp.setConfig(JacksonUtils.writeValueAsString(config));

            accessTaskService.saveTask(kgTaskRsp);
        }
    }

    @Override
    public void pushGraphModel(String userId, ModelPushReq req) {

        SchemaRsp schemaRsp = graphApplicationService.querySchema(req.getKgName());

        if (schemaRsp == null || schemaRsp.getTypes() == null || schemaRsp.getTypes().isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
        }

        DWPrebuildModel model = DWPrebuildModel.builder()
                .name(schemaRsp.getKgTitle())
                .permission(0)
                .userId(getUserDetail().getId())
                .username(getUserDetail().getUsername())
                .modelType(req.getModelType())
                .isStandardTemplate(0)
                .status("1")
                .build();

        model = prebuildModelRepository.save(model);

        addSchema2PreBuilder(schemaRsp, model.getId());

    }

    @Override
    public void updateModel(PreBuilderUpdateReq req) {

        Optional<DWPrebuildModel> opt = prebuildModelRepository.findById(req.getId());

        if(opt.isPresent()){

            DWPrebuildModel model = opt.get();
            model.setName(req.getName());
            model.setDescription(req.getDesc());
            model.setModelType(req.getModelType());
            prebuildModelRepository.save(model);
        }
    }

    private void addSchema2PreBuilder(SchemaRsp schemaRsp, Integer modelId) {

        List<BaseConceptRsp> conceptRsps = schemaRsp.getTypes();

        Map<String, Integer> conceptMap = Maps.newHashMap();
        Map<Long, String> conceptNameMap = conceptRsps.stream().collect(Collectors.toMap(BaseConceptRsp::getId, BaseConceptRsp::getName));


        //递归添加概念
        addConceptReturn(conceptRsps, modelId, conceptMap, conceptNameMap);

        List<AttributeDefinitionRsp> attrs = schemaRsp.getAttrs();

        if (attrs == null || attrs.isEmpty()) {
            return;
        }

        for (AttributeDefinitionRsp attrRsp : attrs) {


            DWPrebuildAttr attr = new DWPrebuildAttr();
            BeanUtils.copyProperties(attrRsp, attr);
            attr.setAttrKey(attrRsp.getKey());
            attr.setAttrType(attrRsp.getType());
            attr.setId(null);

            attr.setConceptId(conceptMap.get(conceptNameMap.get(attrRsp.getDomainValue())));
            attr.setModelId(modelId);
            if (attrRsp.getType().equals(1)) {
                attr.setRange(conceptMap.get(conceptNameMap.get(attrRsp.getRangeValue().get(0))));
                attr.setRangeName(conceptNameMap.get(attrRsp.getRangeValue().get(0)));
            }

            attr = prebuildAttrRepository.save(attr);

            if (attrRsp.getType().equals(1) && attrRsp.getExtraInfos() != null && !attrRsp.getExtraInfos().isEmpty()) {

                for (AttrExtraRsp relationAttrRsp : attrRsp.getExtraInfos()) {
                    DWPrebuildRelationAttr relationAttr = new DWPrebuildRelationAttr();
                    BeanUtils.copyProperties(relationAttrRsp, relationAttr);
                    relationAttr.setUnit(relationAttrRsp.getDataUnit());

                    relationAttr.setAttrId(attr.getId());
                    relationAttr.setModelId(modelId);
                    if (relationAttr.getName() == null || relationAttr.getName().isEmpty()) {
                        continue;
                    }
                    prebuildRelationAttrRepository.save(relationAttr);
                }
            }

        }


        return;
    }

    private void addConceptReturn(List<BaseConceptRsp> neadAddConcept, Integer modelId, Map<String, Integer> conceptMap, Map<Long, String> conceptNameMap) {

        if (neadAddConcept == null || neadAddConcept.isEmpty()) {
            return;
        }

        for (Iterator<BaseConceptRsp> it = neadAddConcept.iterator(); it.hasNext(); ) {

            BaseConceptRsp concept = it.next();

            if (concept.getParentId().equals(0l)) {
                PreBuilderConceptRsp conceptRsp = new PreBuilderConceptRsp();
                conceptRsp.setId(null);
                conceptRsp.setName(concept.getName());
                conceptRsp.setImage(concept.getImg());
                conceptRsp.setConceptKey(concept.getKey());
                addPreBuilderConcept(conceptRsp, modelId, conceptMap);
                it.remove();
            } else if (conceptNameMap.containsKey(concept.getParentId()) && conceptMap.containsKey(conceptNameMap.get(concept.getParentId()))) {
                PreBuilderConceptRsp conceptRsp = new PreBuilderConceptRsp();
                conceptRsp.setId(null);
                conceptRsp.setName(concept.getName());
                conceptRsp.setImage(concept.getImg());
                conceptRsp.setConceptKey(concept.getKey());
                conceptRsp.setParentId(conceptMap.get(conceptNameMap.get(concept.getParentId())));
                addPreBuilderConcept(conceptRsp, modelId, conceptMap);
                it.remove();
            }
        }

        if (!neadAddConcept.isEmpty()) {
            addConceptReturn(neadAddConcept, modelId, conceptMap, conceptNameMap);
        }

    }

    private void addPreBuilderConcept(PreBuilderConceptRsp conceptRsp, Integer modelId, Map<String, Integer> conceptMap) {

        DWPrebuildConcept concept = new DWPrebuildConcept();
        BeanUtils.copyProperties(conceptRsp, concept);

        concept.setModelId(modelId);

        prebuildConceptRepository.save(concept);

        conceptMap.put(concept.getName(), concept.getId());

    }

    private List<DataMapReq> quote2DataMap(List<SchemaQuoteReq> mapConfig) {

        if (mapConfig == null || mapConfig.isEmpty()) {
            return new ArrayList<>();
        }

        List<DataMapReq> dataMapReqList = new ArrayList<>();
        for (SchemaQuoteReq schemaQuoteReq : mapConfig) {

            DataMapReq dataMapReq = new DataMapReq();
            dataMapReq.setEntityName(schemaQuoteReq.getEntityName());
            dataMapReq.setConceptId(schemaQuoteReq.getConceptId());
            dataMapReq.setConceptName(schemaQuoteReq.getConceptName());

            if (schemaQuoteReq.getAttrs() != null && !schemaQuoteReq.getAttrs().isEmpty()) {


                List<AttributesMapReq> attributes = new ArrayList<>();
                List<RelationsMapReq> relations = new ArrayList<>();

                for (SchemaQuoteAttrReq attrReq : schemaQuoteReq.getAttrs()) {

                    if (attrReq.getAttrType().equals(0)) {
                        //数值
                        AttributesMapReq attr = new AttributesMapReq();
                        attr.setAttrName(attrReq.getAttrName());
                        attr.setKgAttrId(attrReq.getAttrId());
                        attr.setKgAttrName(attrReq.getAttrName());

                        attributes.add(attr);
                    } else {
                        //对象

                        RelationsMapReq rela = new RelationsMapReq();
                        rela.setRelationName(attrReq.getAttrName());
                        rela.setKgRelationId(attrReq.getAttrId());
                        rela.setKgRelationName(attrReq.getAttrName());


                        List<SchemaQuoteRelationAttrReq> relationAttrReqs = attrReq.getRelationAttrs();

                        if (relationAttrReqs != null) {

                            List<AttributesMapReq> relationAttrs = new ArrayList<>();

                            for (SchemaQuoteRelationAttrReq relationAttrReq : relationAttrReqs) {

                                if(relationAttrReq.getAttrId() == null){
                                    continue;
                                }
                                AttributesMapReq attr = new AttributesMapReq();
                                attr.setAttrName(relationAttrReq.getName());
                                attr.setKgAttrId(relationAttrReq.getAttrId());
                                attr.setKgAttrName(relationAttrReq.getName());
                                relationAttrs.add(attr);
                            }

                            rela.setAttributes(relationAttrs);

                        }


                        relations.add(rela);


                    }

                }

                dataMapReq.setAttributes(attributes);
                dataMapReq.setRelations(relations);

            }

            dataMapReqList.add(dataMapReq);
        }

        return dataMapReqList;
    }

    private void megerSchemaQuote(List<SchemaQuoteReq> targetList, List<SchemaQuoteReq> sourceList) {
        if (targetList == null) {
            targetList = new ArrayList<>();
        }

        if (targetList.isEmpty()) {
            targetList.addAll(sourceList);
        } else {

            //合并
            Map<String, SchemaQuoteReq> targetMap = new HashMap<>();
            for (SchemaQuoteReq schemaQuoteReq : targetList) {

                String key = schemaQuoteReq.getModelId() + schemaQuoteReq.getEntityName() + schemaQuoteReq.getConceptId();
                targetMap.put(key, schemaQuoteReq);
            }

            for (SchemaQuoteReq schemaQuoteReq : sourceList) {
                String key = schemaQuoteReq.getModelId() + schemaQuoteReq.getEntityName() + schemaQuoteReq.getConceptId();


                //已经添加过概念，在原来的基础上新增新属性
                if (targetMap.containsKey(key)) {

                    if (schemaQuoteReq.getAttrs() != null) {
                        SchemaQuoteReq tarQuote = targetMap.get(key);
                        List<SchemaQuoteAttrReq> attrs = tarQuote.getAttrs() == null ? new ArrayList<>() : tarQuote.getAttrs();

                        attrs.addAll(schemaQuoteReq.getAttrs());
                    }
                } else {

                    //新映射的概念 直接添加
                    targetList.add(schemaQuoteReq);
                }
            }

        }
    }

    private List<SchemaQuoteReq> importToGraph(String kgName, List<SchemaQuoteReq> quoteConfigs) {

        if (quoteConfigs == null || quoteConfigs.isEmpty()) {
            return new ArrayList<>();
        }

        SchemaRsp schemaRsp = graphApplicationService.querySchema(kgName);

        Map<String, Long> conceptNameIdMap = new HashMap<>();
        if (schemaRsp.getTypes() != null && !schemaRsp.getTypes().isEmpty()) {
            schemaRsp.getTypes().forEach(type -> {
                conceptNameIdMap.put(type.getName(),type.getId());
            });
//            conceptNameIdMap = schemaRsp.getTypes().stream().collect(Collectors.toMap(BaseConceptRsp::getName, BaseConceptRsp::getId));
        }

        List<SchemaQuoteReq> needAddConcepts = new ArrayList<>();

        for (SchemaQuoteReq schemaQuoteReq : quoteConfigs) {

            if (schemaQuoteReq.getConceptId() == null) {
                //概念不存在，添加属性

                if (schemaQuoteReq.getPConceptId() == null) {

                    //父概念还未添加 需要先添加父概念才能添加该概念
                    needAddConcepts.add(schemaQuoteReq);
                } else {

                    if (conceptNameIdMap.containsKey(schemaQuoteReq.getConceptName())) {
                        schemaQuoteReq.setConceptId(conceptNameIdMap.get(schemaQuoteReq.getConceptName()));
                        schemaQuoteReq.setPConceptId(conceptNameIdMap.get(schemaQuoteReq.getPConceptName()));
                        continue;
                    }
                    ConceptAddReq conceptAddReq = new ConceptAddReq();
                    conceptAddReq.setName(schemaQuoteReq.getConceptName());
                    conceptAddReq.setParentId(schemaQuoteReq.getPConceptId());

                    Long conceptId = graphEditService.createConcept(kgName, conceptAddReq);
                    schemaQuoteReq.setConceptId(conceptId);
                    schemaQuoteReq.setPConceptId(conceptNameIdMap.get(schemaQuoteReq.getPConceptName()));
                    conceptNameIdMap.put(schemaQuoteReq.getConceptName(), conceptId);
                }
            } else {
                conceptNameIdMap.put(schemaQuoteReq.getConceptName(), schemaQuoteReq.getConceptId());
            }

        }

        if (!needAddConcepts.isEmpty()) {
            //存在未添加的概念 递归添加
            addConceptsGraph(kgName, needAddConcepts, conceptNameIdMap);
        }

        for (SchemaQuoteReq schemaQuoteReq : quoteConfigs) {

            //避免属性定义添加到顶层概念
            if(schemaQuoteReq.getConceptId().equals(0L)){
                continue;
            }

            if (schemaQuoteReq.getAttrs() == null || schemaQuoteReq.getAttrs().isEmpty()) {
                continue;
            }

            AttrDefinitionSearchReq attrDefinitionSearchReq = new AttrDefinitionSearchReq();
            attrDefinitionSearchReq.setConceptId(conceptNameIdMap.get(schemaQuoteReq.getConceptName()));
            List<AttrDefinitionRsp> attrDefinitionRspList = attributeService.getAttrDefinitionByConceptId(kgName,attrDefinitionSearchReq);
            Map<String, AttributeDefinitionRsp> attrMap = new HashMap<>();
            if (attrDefinitionRspList != null && !attrDefinitionRspList.isEmpty()) {
                schemaRsp.getAttrs().forEach(attr -> attrMap.put(attr.getName(), attr));
            }

            for (SchemaQuoteAttrReq attrReq : schemaQuoteReq.getAttrs()) {

                Integer attrId;

                if (attrReq.getAttrId() != null) {

                    attrId = attrReq.getAttrId();

                } else if (attrMap.containsKey(attrReq.getAttrName())) {

                    AttributeDefinitionRsp a = attrMap.get(attrReq.getAttrName());

                    if (!a.getType().equals(attrReq.getAttrType())) {
                        continue;
                    }

                    if (a.getType().equals(0)) {
                        if (!a.getDataType().equals(attrReq.getDataType())) {
                            continue;
                        }
                    } else {
                        if (!a.getRangeValue().contains(attrReq.getRange())) {
                            continue;
                        }
                    }

                    attrId = a.getId();

                } else {

                    AttrDefinitionReq attrDefinitionReq = new AttrDefinitionReq();
                    attrDefinitionReq.setName(attrReq.getAttrName());
                    attrDefinitionReq.setType(attrReq.getAttrType());

                    if (!StringUtils.isEmpty(attrReq.getAttrKey())) {
                        attrDefinitionReq.setKey(attrReq.getAttrKey());
                    }

                    if (!StringUtils.isEmpty(attrReq.getAlias())) {
                        attrDefinitionReq.setAlias(attrReq.getAlias());
                    }

                    attrDefinitionReq.setDomainValue(schemaQuoteReq.getConceptId());

                    if (attrReq.getAttrType().equals(0)) {
                        //数值
                        attrDefinitionReq.setDataType(attrReq.getDataType());
                        attrDefinitionReq.setDataUnit(attrReq.getUnit());
                    } else {

                        Long rangeId = attrReq.getRange();
                        if (rangeId == null) {
                            rangeId = conceptNameIdMap.get(attrReq.getRangeName());
                            attrReq.setRange(rangeId);
                        }
                        attrDefinitionReq.setRangeValue(Lists.newArrayList(rangeId));
                        attrDefinitionReq.setDirection(0);
                        attrDefinitionReq.setDataType(0);
                    }
                    OpenBatchResult<AttrDefinitionBatchRsp> openBatchResult = attributeService.batchAddAttrDefinition(kgName, Lists.newArrayList(attrDefinitionReq));
                    if(openBatchResult == null || openBatchResult.getSuccess() == null ||openBatchResult.getSuccess().isEmpty()){
                        continue;
                    }
                    attrId = openBatchResult.getSuccess().get(0).getId();

                }

                attrReq.setAttrId(attrId);

                if (attrReq.getAttrType().equals(1) && attrReq.getRelationAttrs() != null) {

                    AttributeDefinitionRsp a = attrMap.get(attrReq.getAttrName() + schemaQuoteReq.getConceptId());

                    Map<String, AttrExtraRsp> relaAMap = new HashMap<>();
                    if (a != null && a.getExtraInfos() != null) {
                        a.getExtraInfos().forEach(attr -> relaAMap.put(attr.getName(), attr));
                    }

                    for (SchemaQuoteRelationAttrReq relationAttrReq : attrReq.getRelationAttrs()) {

                        Integer relaAttrId;
                        if (relationAttrReq.getAttrId() != null) {

                            continue;

                        } else if (relaAMap.containsKey(relationAttrReq.getName())) {

                            AttrExtraRsp graphRelaAtt = relaAMap.get(relationAttrReq.getName());
                            if (graphRelaAtt.getType().equals(1) || !graphRelaAtt.getDataType().equals(relationAttrReq.getDataType())) {
                                continue;
                            }

                            relaAttrId = graphRelaAtt.getSeqNo();
                        } else {

                            EdgeAttrDefinitionReq edgeAttrDefinitionReq = new EdgeAttrDefinitionReq();
                            edgeAttrDefinitionReq.setDataType(relationAttrReq.getDataType());
                            edgeAttrDefinitionReq.setName(relationAttrReq.getName());
                            edgeAttrDefinitionReq.setDataUnit(relationAttrReq.getUnit());
                            edgeAttrDefinitionReq.setType(0);
                            relaAttrId = attributeService.addEdgeAttr(kgName, attrId, edgeAttrDefinitionReq);

                        }

                        relationAttrReq.setAttrId(relaAttrId);
                    }

                }
            }

        }

        return quoteConfigs;
    }

    private void addConceptsGraph(String kgName, List<SchemaQuoteReq> needAddConcepts, Map<String, Long> conceptNameIdMap) {

        if (needAddConcepts.isEmpty()) {
            return;
        }

        Iterator<SchemaQuoteReq> it = needAddConcepts.iterator();

        while (it.hasNext()) {
            SchemaQuoteReq concept = it.next();

            if (conceptNameIdMap.containsKey(concept.getConceptName())) {
                concept.setConceptId(conceptNameIdMap.get(concept.getConceptName()));
                concept.setPConceptId(conceptNameIdMap.get(concept.getPConceptName()));
                it.remove();
            }
            if (conceptNameIdMap.containsKey(concept.getPConceptName())) {
                ConceptAddReq conceptAddReq = new ConceptAddReq();
                conceptAddReq.setName(concept.getConceptName());
                conceptAddReq.setParentId(conceptNameIdMap.get(concept.getPConceptName()));

                Long conceptId = graphEditService.createConcept(kgName, conceptAddReq);
                concept.setConceptId(conceptId);
                concept.setPConceptId(conceptNameIdMap.get(concept.getPConceptName()));

                conceptNameIdMap.put(concept.getConceptName(), conceptId);

                it.remove();
            }
        }

        addConceptsGraph(kgName, needAddConcepts, conceptNameIdMap);
    }

    @Override
    public void createModel(DWDatabaseRsp database, List<PreBuilderConceptRsp> preBuilderConceptRspList, String modelType, String yamlContent) {

        DWPrebuildModel model = DWPrebuildModel.builder()
                .databaseId(database.getId())
                .name(database.getTitle())
                .permission(0)
                .yamlContent(yamlContent)
                .userId(SessionHolder.getUserId())
                .username(getUserDetail().getUsername())
                .modelType(modelType)
                .isStandardTemplate(0)
                .status("1")
                .build();

        model = prebuildModelRepository.save(model);

        createSchemaModel(model.getId(), preBuilderConceptRspList);
    }

    @Override
    public List<String> getTypes(String userId,Boolean isManage) {

        if(isManage == null){
            isManage = false;
        }

        List<String> modelTypes;
        UserDetailRsp userDetailRsp = getUserDetail();

        if ("admin".equals(userDetailRsp.getUsername())) {
            modelTypes = prebuildModelRepository.getAdminModelTypes();
        } else {
            if(isManage){
                modelTypes = prebuildModelRepository.getUserManageModelTypes(userId);
            }else{
                modelTypes = prebuildModelRepository.getModelTypes(userId);
            }
        }
        return modelTypes;
    }

    @Override
    public void create(PreBuilderCreateReq req) {

        UserDetailRsp userDetailRsp = getUserDetail();
        if (!"admin".equals(userDetailRsp.getUsername())) {
            throw BizException.of(KgmsErrorCodeEnum.PERMISSION_NOT_MODEL_UPLOAD_ERROR);
        }

        if (req.getFilePath() == null || (!req.getFilePath().endsWith(".xls") && !req.getFilePath().endsWith(".xlsx") && !req.getFilePath().endsWith(".zip"))) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }


        if (req.getFilePath().endsWith(".xls") || req.getFilePath().endsWith(".xlsx")) {
            //纯模式
            List<ModelExcelRsp> modelExcelRspList = addModelByExcel(req.getFilePath(), req.getName());

            DWPrebuildModel model = DWPrebuildModel.builder()
                    .userId(getUserDetail().getId())
                    .username(getUserDetail().getUsername())
                    .modelType(req.getModelType())
                    .description(req.getDesc())
                    .name(req.getName())
                    .status("0")
                    .permission(1)
                    .isStandardTemplate(0)
                    .build();

            prebuildModelRepository.save(model);

            createModelByExcel(model, modelExcelRspList);

        } else {
            //行业标准

            DWPrebuildModel model = addModelByZip(req);

            prebuildModelRepository.save(model);

            createSchemaModel(model.getId(), dwService.modelSchema2PreBuilder(model.getTagJson()));
        }


    }


    public static void bytesToFile(byte[] buffer, final String filePath) {

        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;

        try {
            output = new FileOutputStream(file);

            bufferedOutput = new BufferedOutputStream(output);

            bufferedOutput.write(buffer);
            bufferedOutput.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedOutput) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private DWPrebuildModel addModelByZip(PreBuilderCreateReq req) {

        String ktr = "";
        String file = "";
        String tableName = "";
        ModelSchemaConfigRsp modelSchema = null;
        List<DataSetSchema> schema = new ArrayList<>();

        ZipFile zipFile = null;
        String zipTempPath = "";

        String zFile = zipTempPath + req.getFilePath().substring(req.getFilePath().lastIndexOf("/"));
        try {
            byte[] bytes = fastdfsTemplate.downloadFile(req.getFilePath());
            bytesToFile(bytes, zFile);

            zipFile = new ZipFile(zFile);

            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();

                // 将压缩文件内容写入到这个文件中
                InputStream is = zipFile.getInputStream(entry);


                if (entry.getName().endsWith(".ktr")) {
                    ktr = IOUtils.toString(is, StandardCharsets.UTF_8);
                } else if (entry.getName().endsWith("tag.json")) {
                    modelSchema = JacksonUtils.readValue(is, ModelSchemaConfigRsp.class);
                    tableName = modelSchema.getTableName();

                } else {

                    List<String> lines = IOUtils.readLines(is);
                    file = org.apache.commons.lang3.StringUtils.join(lines, " ");

                    jsonFileSchema(schema, file);
                }

                is.close();
            }

            return DWPrebuildModel.builder()
                    .name(req.getName())
                    .description(req.getDesc())
                    .modelType(req.getModelType())
                    .isStandardTemplate(1)
                    .status("0")
                    .permission(1)
                    .userId(getUserDetail().getId())
                    .username(getUserDetail().getUsername())
                    .ktr(Lists.newArrayList(TableKtrRsp.builder().ktr(ktr).tableName(tableName).build()))
                    .tagJson(Lists.newArrayList(modelSchema))
                    .fileContent(file)
                    .schemas(Lists.newArrayList(StandardTemplateSchema.builder().schemas(schema).tableName(tableName).title(tableName).build()))
                    .build();
        } catch (Exception e) {

            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (Exception e) {
                }
            }

            if (new File(zFile).exists()) {
                try {
                    new File(zFile).delete();
                } catch (Exception e) {
                }
            }
        }
    }

    private void jsonFileSchema(List<DataSetSchema> setSchemas, String in) {
        Object json = JacksonUtils.readValue(in, new TypeReference<Object>() {
        });
        Map<String, Object> map = new HashMap<>();
        if (json instanceof List) {
            List l = (List) json;
            map = (Map<String, Object>) l.get(0);
        } else if (json instanceof Map) {
            map = (Map<String, Object>) json;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String o = entry.getKey();
            FieldType type = readType(entry.getValue());
            DataSetSchema dataSetSchema = new DataSetSchema();
            dataSetSchema.setField(o);
            dataSetSchema.setType(type.getCode());
            setSchemas.add(dataSetSchema);
        }
    }

    private FieldType readType(Object val) {
        FieldType type;
        String string = val.toString();
        if (string.startsWith(JSON_START)) {
            try {
                JacksonUtils.getInstance().readValue(string, ObjectNode.class);
                type = FieldType.OBJECT;
            } catch (Exception e) {
                type = FieldType.STRING;
            }
        } else if (string.startsWith(ARRAY_START)) {
            if (string.startsWith(ARRAY_STRING_START)) {
                try {
                    JacksonUtils.getInstance().readValue(string, new TypeReference<List<String>>() {
                    });
                    type = FieldType.STRING_ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            } else {
                try {
                    JacksonUtils.getInstance().readValue(string, ArrayNode.class);
                    type = FieldType.ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            }
        } else if (val instanceof Integer) {
            type = FieldType.INTEGER;
        } else if (val instanceof Long) {
            type = FieldType.LONG;
        } else if (val instanceof Date) {
            type = FieldType.DATE;
        } else if (val instanceof Double) {
            type = FieldType.DOUBLE;
        } else if (val instanceof Float) {
            type = FieldType.FLOAT;
        } else {
            type = FieldType.STRING;
        }
        return type;
    }


    private void createModelByExcel(DWPrebuildModel model, List<ModelExcelRsp> modelExcelRspList) {

        if (modelExcelRspList == null || modelExcelRspList.isEmpty()) {
            return;
        }

        Map<String, Integer> conceptMap = new HashMap<>();
        List<ModelExcelRsp.Relation> relationList = new ArrayList<>();

        //递归添加概念
        addModelConcept(conceptMap, relationList, modelExcelRspList, model.getId());

        if (!relationList.isEmpty()) {
            addModelRelationByExecl(conceptMap, relationList, model.getId());
        }

    }

    private void addModelRelationByExecl(Map<String, Integer> conceptMap, List<ModelExcelRsp.Relation> relationList, Integer modelId) {

        for (ModelExcelRsp.Relation relation : relationList) {

            prebuildAttrRepository.save(DWPrebuildAttr.builder()
                    .modelId(modelId)
                    .conceptId(conceptMap.get(relation.getDomain() + relation.getDomainMeaningTag()))
                    .name(relation.getName())
                    .alias(relation.getAlias())
                    .attrType(1)
                    .range(conceptMap.get(relation.getRange() + relation.getRangeMeaningTag()))
                    .rangeName(relation.getRange())
                    .build());

        }
    }

    private void addModelConcept(Map<String, Integer> conceptMap, List<ModelExcelRsp.Relation> relationList, List<ModelExcelRsp> modelExcelRspList, Integer modelId) {

        if (modelExcelRspList == null || modelExcelRspList.isEmpty()) {
            return;
        }

        for (Iterator<ModelExcelRsp> it = modelExcelRspList.iterator(); it.hasNext(); ) {

            ModelExcelRsp concept = it.next();

            if (concept.getParentName() == null) {
                //顶层概念
                DWPrebuildConcept pbConcept = prebuildConceptRepository.save(DWPrebuildConcept.builder()
                        .modelId(modelId)
                        .name(concept.getName())
                        .meaningTag(concept.getMeaningTag())
                        .build());

                if (concept.getAttrs() != null && !concept.getAttrs().isEmpty()) {
                    addModelAttrByExecl(modelId, pbConcept.getId(), concept.getAttrs());
                }

                if (concept.getRelations() != null && !concept.getRelations().isEmpty()) {
                    relationList.addAll(concept.getRelations());
                }

                conceptMap.put(pbConcept.getName() + pbConcept.getMeaningTag(), pbConcept.getId());

            } else if (conceptMap.containsKey(concept.getParentName() + concept.getParentMeaningTag())) {
                //父概念已经入图

                DWPrebuildConcept pbConcept = prebuildConceptRepository.save(DWPrebuildConcept.builder()
                        .modelId(modelId)
                        .name(concept.getName())
                        .parentId(conceptMap.get(concept.getParentName() + concept.getParentMeaningTag()))
                        .meaningTag(concept.getMeaningTag())
                        .build());

                if (concept.getAttrs() != null && !concept.getAttrs().isEmpty()) {
                    addModelAttrByExecl(modelId, pbConcept.getId(), concept.getAttrs());
                }

                if (concept.getRelations() != null && !concept.getRelations().isEmpty()) {
                    relationList.addAll(concept.getRelations());
                }


                conceptMap.put(pbConcept.getName() + pbConcept.getMeaningTag(), pbConcept.getId());
            } else {

                //父概念还未添加
                continue;
            }

            it.remove();

        }
        if (modelExcelRspList != null && !modelExcelRspList.isEmpty()) {
            addModelConcept(conceptMap, relationList, modelExcelRspList, modelId);
        }
    }

    private void addModelAttrByExecl(Integer modelId, Integer conceptId, List<ModelExcelRsp.Attr> attrs) {

        for (ModelExcelRsp.Attr attr : attrs) {

            prebuildAttrRepository.save(DWPrebuildAttr.builder()
                    .modelId(modelId)
                    .conceptId(conceptId)
                    .name(attr.getName())
                    .alias(attr.getAlias())
                    .dataType(attr.getDataType())
                    .unit(attr.getUnit())
                    .attrType(0)
                    .build());

        }

    }


    private List<ModelExcelRsp> addModelByExcel(String filePath, String name) {


        ByteArrayInputStream inputStream = new ByteArrayInputStream(fastdfsTemplate.downloadFile(filePath));
        ExcelParser excelParser = new ExcelParser(inputStream, filePath);

        Sheet sheet = excelParser.wb.getSheetAt(0);
        Map<String, ModelExcelRsp> conceptMap = getExcelModelConcept(excelParser, sheet, name);

        Sheet attrSheet = excelParser.wb.getSheetAt(1);
        getExcelModelAttr(excelParser, attrSheet, conceptMap);

        Sheet relationSheet = excelParser.wb.getSheetAt(2);
        getExcelModelRelation(excelParser, relationSheet, conceptMap);

        return Lists.newArrayList(conceptMap.values());

    }

    private void getExcelModelRelation(ExcelParser excelParser, Sheet sheet, Map<String, ModelExcelRsp> conceptMap) {
        excelParser.checkTitle(sheet.getRow(0), ExcelParser.ATTRIBUTE);

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i <= rows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {

                String attrName = excelParser.getCellValue(row.getCell(0));
                String domain = excelParser.getCellValue(row.getCell(2));
                String fourColumn = excelParser.getCellValue(row.getCell(4));//值域
                if (!org.springframework.util.StringUtils.hasText(attrName) || !org.springframework.util.StringUtils.hasText(domain) || !org.springframework.util.StringUtils.hasText(fourColumn)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_LACK_REQUIRES_ERROR);
                }
                String domainMeaningTag = excelParser.getCellValue(row.getCell(3));
                String fiveColumn = excelParser.getCellValue(row.getCell(5));
                if (!conceptMap.containsKey(domain + domainMeaningTag)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                }
                if (!conceptMap.containsKey(fourColumn + fiveColumn)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_RANGE_NOT_EXIST_ERROR);
                }

                String alias = excelParser.getCellValue(row.getCell(1));

                ModelExcelRsp.Relation relation = new ModelExcelRsp.Relation();
                relation.setDomain(domain);
                relation.setDomainMeaningTag(domainMeaningTag);
                relation.setName(attrName);
                relation.setAlias(alias);
                relation.setRange(fourColumn);
                relation.setRangeMeaningTag(fiveColumn);

                conceptMap.get(domain + domainMeaningTag).getRelations().add(relation);

            }

        }

    }

    private void getExcelModelAttr(ExcelParser excelParser, Sheet sheet, Map<String, ModelExcelRsp> conceptMap) {

        excelParser.checkTitle(sheet.getRow(0), ExcelParser.ATTRIBUTE);

        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i <= rows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {

                String attrName = excelParser.getCellValue(row.getCell(0));
                String domain = excelParser.getCellValue(row.getCell(2));
                String fourColumn = excelParser.getCellValue(row.getCell(4));
                if (!org.springframework.util.StringUtils.hasText(attrName) || !org.springframework.util.StringUtils.hasText(domain) || !org.springframework.util.StringUtils.hasText(fourColumn)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_LACK_REQUIRES_ERROR);
                }
                String domainMeaningTag = excelParser.getCellValue(row.getCell(3));
                String fiveColumn = excelParser.getCellValue(row.getCell(5));
                if (!conceptMap.containsKey(domain + domainMeaningTag)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                }
                Integer fourColumnId;
                fourColumnId = DataTypeEnum.getDataType(fourColumn);
                if (fourColumnId == null) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DATATYPE_NOT_EXIST_ERROR);
                }

                String alias = excelParser.getCellValue(row.getCell(1));

                ModelExcelRsp.Attr attr = new ModelExcelRsp.Attr();
                attr.setName(attrName);
                attr.setAlias(alias);
                attr.setDataType(fourColumnId);
                attr.setUnit(fiveColumn);

                conceptMap.get(domain + domainMeaningTag).getAttrs().add(attr);

            }

        }
    }

    private Map<String, ModelExcelRsp> getExcelModelConcept(ExcelParser excelParser, Sheet sheet, String name) {

        excelParser.checkTitle(sheet.getRow(0), ExcelParser.CONCEPT);

        Map<String, ModelExcelRsp> conceptMap = new HashMap<>();
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i <= rows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                String parentName = excelParser.getCellValue(row.getCell(0));
                String parentMeaningTag = excelParser.getCellValue(row.getCell(1));
                String sonName = excelParser.getCellValue(row.getCell(2));
                String sonMeaningTag = excelParser.getCellValue(row.getCell(3));
                if (!org.springframework.util.StringUtils.hasText(sonName)) {
                    throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
                }
                if (Objects.equals(parentMeaningTag, sonMeaningTag) && Objects.equals(sonName, parentName)) {
                    throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
                }

                ModelExcelRsp sonConcept;
                if (!conceptMap.containsKey(sonName + sonMeaningTag)) {
                    sonConcept = ModelExcelRsp.builder().name(sonName).meaningTag(sonMeaningTag).build();
                    sonConcept.setAttrs(new ArrayList<>());
                    sonConcept.setRelations(new ArrayList<>());
                    conceptMap.put(sonName + sonMeaningTag, sonConcept);

                    if (org.springframework.util.StringUtils.hasText(parentName) && !"root".equals(parentName)) {
                        sonConcept.setParentName(parentName);
                        sonConcept.setMeaningTag(sonMeaningTag);
                    }
                } else {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_CONCEPT_EXIST_ERROR);
                }

            }
        }

        //校验
        checkPranetConceptIsExist(conceptMap);

        return conceptMap;
    }

    private void checkPranetConceptIsExist(Map<String, ModelExcelRsp> conceptMap) {

        for (Map.Entry<String, ModelExcelRsp> concept : conceptMap.entrySet()) {

            if (concept.getValue().getParentName() != null) {
                String k = concept.getValue().getParentName() + concept.getValue().getParentMeaningTag();
                if (!conceptMap.containsKey(k)) {
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PARENT_CONCEPT_NOT_EXIST_ERROR);
                }

                List<String> parentList  = new ArrayList<>();
                getAllParentConcept(k, conceptMap, parentList);

                if(parentList.contains(k)){
                    throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PARENT_CONCEPT_NOT_EXIST_ERROR);
                }
            }
        }
    }

    private void getAllParentConcept(String k, Map<String, ModelExcelRsp> conceptMap, List<String> parentList) {

        if(conceptMap.containsKey(k)){
            ModelExcelRsp rsp = conceptMap.get(k);
            if(!StringUtils.isEmpty(rsp.getParentName())){
                parentList.add(rsp.getParentName()+rsp.getParentMeaningTag());
                getAllParentConcept(rsp.getParentName()+rsp.getParentMeaningTag(),conceptMap,parentList);
            }
        }

    }


    @Override
    public List<SchemaQuoteReq> getGraphMap(String userId, String kgName,boolean isDelete) {


        if(isDelete){
            graphMapService.deleteDataByNotExistConcept(kgName);
        }

        List<DWGraphMap> dwGraphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).build()));

        if (dwGraphMapList == null || dwGraphMapList.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, SchemaQuoteReq> schemaQuoteReqMap = new HashMap<>();
        Map<String, Map<String, SchemaQuoteAttrReq>> schemaQuoteAttrMap = new HashMap<>();
        Map<String, Map<String, List<String>>> schemaAuoteRelationAttrMap = new HashMap<>();

        for (DWGraphMap graphMap : dwGraphMapList) {

            SchemaQuoteReq schemaQuoteReq;
            String conceptKey = graphMap.getEntityName() + graphMap.getConceptName() + graphMap.getModelId();

            if (schemaQuoteReqMap.containsKey(conceptKey)) {

                schemaQuoteReq = schemaQuoteReqMap.get(conceptKey);

                if (!schemaQuoteReq.getTables().contains(graphMap.getTableName())) {
                    schemaQuoteReq.getTables().add(graphMap.getTableName());
                }

            } else {

                schemaQuoteReq = new SchemaQuoteReq();
                BeanUtils.copyProperties(graphMap, schemaQuoteReq);

                schemaQuoteReq.setAttrs(new ArrayList<>());
                schemaQuoteReq.setTables(Lists.newArrayList(graphMap.getTableName()));
                schemaQuoteAttrMap.put(conceptKey, new HashMap<>());
                schemaQuoteReqMap.put(conceptKey, schemaQuoteReq);
            }

            if (graphMap.getAttrId() == null) {
                //概念映射 没有属性
                continue;
            }

            //设置属性
            SchemaQuoteAttrReq schemaQuoteAttrReq;

            if (!schemaQuoteAttrMap.get(conceptKey).containsKey(graphMap.getAttrName())) {
                schemaQuoteAttrReq = new SchemaQuoteAttrReq();
                BeanUtils.copyProperties(graphMap, schemaQuoteAttrReq);

                if (schemaQuoteAttrReq.getAttrType().equals(1)) {
                    schemaQuoteAttrReq.setModelRange(graphMap.getModelRange().get(0));
                    schemaQuoteAttrReq.setRange(graphMap.getRange().get(0));
                    schemaQuoteAttrReq.setRangeName(graphMap.getRangeName().get(0));
                    schemaQuoteAttrReq.setRelationAttrs(new ArrayList<>());
                }

                schemaQuoteAttrReq.setTables(Lists.newArrayList(graphMap.getTableName()));


                schemaQuoteReq.getAttrs().add(schemaQuoteAttrReq);
                schemaQuoteAttrMap.get(conceptKey).put(graphMap.getAttrName(), schemaQuoteAttrReq);
            } else {
                schemaQuoteAttrReq = schemaQuoteAttrMap.get(conceptKey).get(graphMap.getAttrName());

                if (!schemaQuoteAttrReq.getTables().contains(graphMap.getTableName())) {
                    schemaQuoteAttrReq.getTables().add(graphMap.getTableName());
                }
            }

            //不是对象属性，不查边属性
            if (!schemaQuoteAttrReq.getAttrType().equals(1)) {
                continue;
            }

            List<DWGraphMapRelationAttr> graphMapRelationAttrList = graphMapRelationAttrRepository.findAll(Example.of(DWGraphMapRelationAttr.builder().modelAttrId(schemaQuoteAttrReq.getModelAttrId()).kgName(kgName).modelId(schemaQuoteReq.getModelId()).build()));
            if (graphMapRelationAttrList == null || graphMapRelationAttrList.isEmpty()) {
                continue;
            }

            List<String> relationAttrName = schemaQuoteAttrReq.getRelationAttrs().stream().map(SchemaQuoteRelationAttrReq::getName).collect(Collectors.toList());

            for (DWGraphMapRelationAttr graphMapRelationAttr : graphMapRelationAttrList) {

                if(graphMapRelationAttr.getAttrId() == null){
                    continue;
                }

                if (schemaAuoteRelationAttrMap.containsKey(conceptKey)
                        && schemaAuoteRelationAttrMap.get(conceptKey).containsKey(schemaQuoteAttrReq.getAttrName())
                        && schemaAuoteRelationAttrMap.get(conceptKey).get(schemaQuoteAttrReq.getAttrName()).contains(graphMapRelationAttr.getName())) {
                    continue;
                } else {

                    if(!relationAttrName.contains(graphMapRelationAttr.getName())){
                        SchemaQuoteRelationAttrReq schemaQuoteRelationAttrReq = new SchemaQuoteRelationAttrReq();
                        BeanUtils.copyProperties(graphMapRelationAttr, schemaQuoteRelationAttrReq);
                        schemaQuoteRelationAttrReq.setTables(Lists.newArrayList(graphMap.getTableName()));
                        schemaQuoteAttrReq.getRelationAttrs().add(schemaQuoteRelationAttrReq);
                        relationAttrName.add(graphMapRelationAttr.getName());
                    }

                }

            }

        }

        return Lists.newArrayList(schemaQuoteReqMap.values());
    }

    @Override
    public PreBuilderSearchRsp databaseDetail(String userId, Long databaseId) {


        DWPrebuildModel model = prebuildModelRepository.findByUserIdAndDatabaseId(userId, databaseId);

        PreBuilderSearchRsp modelSearch = new PreBuilderSearchRsp();
        BeanUtils.copyProperties(model, modelSearch);

        setModelSchema(modelSearch, true);
        return modelSearch;
    }

    @Override
    public Page<PreBuilderSearchRsp> listManage(String userId, PreBuilderSearchReq req) {
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize(), Sort.by(Sort.Order.desc("createAt")));


        if (!req.isGraph() && !req.isManage() && !req.isUser() && !req.isDw()) {
            return Page.empty();
        }

        String username = getUserDetail().getUsername();

        Specification<DWPrebuildModel> specification = new Specification<DWPrebuildModel>() {
            @Override
            public Predicate toPredicate(Root<DWPrebuildModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(req.getKw())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getKw() + "%");
                    predicates.add(likename);
                }

                List<Predicate> tags = new ArrayList<>();
                if (req.isDw()) {
                    tags.add(criteriaBuilder.isNotNull(root.get("databaseId")));
                    ;
                }

                if (req.isGraph()) {
                    tags.add(criteriaBuilder.isNull(root.get("databaseId")));
                }

                if (req.isManage()) {
                    tags.add(criteriaBuilder.equal(root.get("username").as(String.class), "admin"));
                }

                if (req.isUser()) {
                    tags.add(criteriaBuilder.notEqual(root.get("username").as(String.class), "admin"));
                }

                predicates.add(criteriaBuilder.or(tags.toArray(new Predicate[]{})));

                if (!StringUtils.isEmpty(req.getModelType())) {

                    Predicate modelTypeEq = criteriaBuilder.equal(root.get("modelType").as(String.class), req.getModelType());
                    predicates.add(modelTypeEq);
                }

                if ("admin".equals(username)) {
                    //管理员
                    if (!StringUtils.isEmpty(req.getUsername())) {
                        Predicate username = criteriaBuilder.equal(root.get("username").as(String.class), req.getUsername());
                        predicates.add(username);
                    }

                } else {
                    //普通用户
                    Predicate isPrivate = criteriaBuilder.equal(root.get("userId").as(String.class), SessionHolder.getUserId());
                    predicates.add(isPrivate);
                }

                if (req.getStatus() != null) {

                    Predicate status = criteriaBuilder.equal(root.get("status").as(String.class), req.getStatus());
                    predicates.add(status);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWPrebuildModel> all = prebuildModelRepository.findAll(specification, pageable);

        Page<PreBuilderSearchRsp> map = all.map(ConvertUtils.convert(PreBuilderSearchRsp.class));

        return map;

    }

    @Override
    public void delete(String userId, Integer id) {

        String username = getUserDetail().getUsername();

        if ("admin".equals(username)) {
            prebuildModelRepository.deleteById(id);
        } else {
            prebuildModelRepository.delete(DWPrebuildModel.builder().userId(userId).id(id).build());
        }

    }

    @Override
    public void update(String userId, Integer id, String status) {

        String username = getUserDetail().getUsername();

        Optional<DWPrebuildModel> modelOpt = prebuildModelRepository.findById(id);

        if(modelOpt.isPresent()){

            DWPrebuildModel model = modelOpt.get();
            model.setStatus(status);

            if ("admin".equals(username) || model.getUserId().equals(userId)) {
                prebuildModelRepository.save(model);
            }
        }

    }

    private void setModelSchema(PreBuilderSearchRsp modelSearch, boolean isSetAttr) {
        List<DWPrebuildConcept> concepts = prebuildConceptRepository.findAll(Example.of(DWPrebuildConcept.builder().modelId(modelSearch.getId()).build()));

        List<PreBuilderConceptRsp> conceptRspList = concepts.stream().map(ConvertUtils.convert(PreBuilderConceptRsp.class)).collect(Collectors.toList());


        if (isSetAttr) {
            if (modelSearch.getConcepts() != null) {

                for (PreBuilderConceptRsp concept : modelSearch.getConcepts()) {

                    List<DWPrebuildAttr> attrs = prebuildAttrRepository.findAll(Example.of(DWPrebuildAttr.builder().conceptId(concept.getId()).build()));

                    List<PreBuilderAttrRsp> attrsRspList = attrs.stream().map(ConvertUtils.convert(PreBuilderAttrRsp.class)).collect(Collectors.toList());

                    if (attrsRspList != null) {

                        for (PreBuilderAttrRsp attrRsp : attrsRspList) {

                            if (attrRsp.getAttrType().equals(0)) {
                                continue;
                            }

                            List<DWPrebuildRelationAttr> relationAttrList = prebuildRelationAttrRepository.findAll(Example.of(DWPrebuildRelationAttr.builder().attrId(attrRsp.getId()).build()));

                            List<PreBuilderRelationAttrRsp> relationAttrRspList = relationAttrList.stream().map(ConvertUtils.convert(PreBuilderRelationAttrRsp.class)).collect(Collectors.toList());
                            attrRsp.setRelationAttrs(relationAttrRspList);
                        }

                    }

                    concept.setAttrs(attrsRspList);
                }

            }
        }

        modelSearch.setConcepts(conceptRspList);
        return;
    }

    private void createSchemaModel(Integer modelId, List<PreBuilderConceptRsp> preBuilderConceptRspList) {

        Map<String, Integer> conceptMap = new HashMap<>();

        for (PreBuilderConceptRsp conceptRsp : preBuilderConceptRspList) {
            DWPrebuildConcept concept = new DWPrebuildConcept();
            BeanUtils.copyProperties(conceptRsp, concept);

            concept.setModelId(modelId);

            prebuildConceptRepository.save(concept);

            conceptMap.put(concept.getName(), concept.getId());
        }

        for (PreBuilderConceptRsp conceptRsp : preBuilderConceptRspList) {

            List<PreBuilderAttrRsp> attrsList = conceptRsp.getAttrs();
            if (attrsList == null || attrsList.isEmpty()) {
                continue;
            }

            for (PreBuilderAttrRsp attrRsp : attrsList) {

                DWPrebuildAttr attr = new DWPrebuildAttr();
                BeanUtils.copyProperties(attrRsp, attr);

                attr.setConceptId(conceptMap.get(conceptRsp.getName()));
                attr.setModelId(modelId);
                if (attrRsp.getAttrType().equals(1)) {
                    attr.setRange(conceptMap.get(attrRsp.getRangeName()));
                }

                prebuildAttrRepository.save(attr);

                if (attrRsp.getAttrType().equals(1) && attrRsp.getRelationAttrs() != null && !attrRsp.getRelationAttrs().isEmpty()) {

                    for (PreBuilderRelationAttrRsp relationAttrRsp : attrRsp.getRelationAttrs()) {
                        DWPrebuildRelationAttr relationAttr = new DWPrebuildRelationAttr();
                        BeanUtils.copyProperties(relationAttrRsp, relationAttr);

                        relationAttr.setAttrId(attr.getId());
                        relationAttr.setModelId(modelId);
                        if (relationAttr.getName() == null || relationAttr.getName().isEmpty()) {
                            continue;
                        }
                        prebuildRelationAttrRepository.save(relationAttr);
                    }
                }
            }
        }
    }

}
