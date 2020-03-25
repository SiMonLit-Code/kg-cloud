package com.plantdata.kgcloud.domain.dw.service.impl;

import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kg.common.bean.ParentSon;
import cn.hiboot.mcn.core.exception.ErrorMsg;
import cn.hiboot.mcn.core.exception.ServiceException;
import com.alibaba.excel.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.access.rsp.KgConfigRsp;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.service.GraphEditService;
import com.plantdata.kgcloud.domain.dw.entity.*;
import com.plantdata.kgcloud.domain.dw.parser.ExcelParser;
import com.plantdata.kgcloud.domain.dw.repository.*;
import com.plantdata.kgcloud.domain.dw.req.PreBuilderCreateReq;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
    private DWTableRepository tableRepository;

    private UserDetailRsp getUserDetail(){
        return userClient.getCurrentUserDetail().getData();
    }

    @Override
    public Page<PreBuilderSearchRsp> findModel(String userId, PreBuilderSearchReq req) {

        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());

        if(!req.isGraph() && req.isManage() && !req.isUser() && !req.isDw()){
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
                    tags.add(criteriaBuilder.isNotNull(root.get("databaseId")));;
                }

                if (req.isGraph()) {
                    tags.add(criteriaBuilder.isNull(root.get("databaseId")));
                }

                if (req.isManage()) {
                    tags.add(criteriaBuilder.equal(root.get("username").as(String.class),"admin"));
                }

                if (req.isUser()) {
                    tags.add(criteriaBuilder.notEqual(root.get("username").as(String.class),"admin"));
                }

                predicates.add(criteriaBuilder.or(tags.toArray(new Predicate[]{})));

                if (!StringUtils.isEmpty(req.getModelType())) {

                    Predicate modelTypeEq = criteriaBuilder.equal(root.get("modelType").as(String.class), req.getModelType());
                    predicates.add(modelTypeEq);
                }

                //查询管理员发布公开的或者自己发布的
                Predicate isPublic = criteriaBuilder.equal(root.get("permission").as(Integer.class), 1);
                Predicate isPrivate = criteriaBuilder.and(criteriaBuilder.equal(root.get("permission").as(Integer.class), 0),criteriaBuilder.equal(root.get("userId").as(String.class), SessionHolder.getUserId()));
                predicates.add(criteriaBuilder.or(isPublic,isPrivate));

                //只能查询发布过的
                predicates.add(criteriaBuilder.equal(root.get("status").as(String.class),"1"));

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
    public List<PreBuilderMatchAttrRsp> matchAttr(String userId, PreBuilderMatchAttrReq req) {

        SchemaRsp schemaRsp = graphApplicationService.querySchema(req.getKgName());

        //属性定义域映射
        Map<Long, List<AttributeDefinitionRsp>> attrMap = new HashMap<>();
        if (schemaRsp.getAttrs() != null) {

            for (AttributeDefinitionRsp attr : schemaRsp.getAttrs()) {
                if (attrMap.containsKey(attr.getDomainValue())) {
                    List<AttributeDefinitionRsp> attrs = attrMap.get(attr.getDomainValue());
                    attrs.add(attr);
                } else {
                    List<AttributeDefinitionRsp> attrs = new ArrayList<>();
                    attrs.add(attr);
                    attrMap.put(attr.getDomainValue(), attrs);
                }
            }
        }

        //属性名称映射
        Map<String, Map<String, AttributeDefinitionRsp>> conceptAttrMap = new HashMap<>();

        Map<Long, String> conceptNameMap = new HashMap<>();

        //概念名称映射属性名称与属性
        if (schemaRsp.getTypes() != null) {

            for (BaseConceptRsp conceptRsp : schemaRsp.getTypes()) {

                conceptNameMap.put(conceptRsp.getId(), conceptRsp.getName());

                Map<String, AttributeDefinitionRsp> attrRspMap = new HashMap<>();

                List<AttributeDefinitionRsp> attrs = attrMap.get(conceptRsp.getId());
                if (attrs != null) {
                    for (AttributeDefinitionRsp attr : attrs) {
                        attrRspMap.put(attr.getName(), attr);
                    }
                }

                conceptAttrMap.put(conceptRsp.getName(), attrRspMap);
            }
        }


        megerSchemaQuote(req.getSchemaQuoteReqList(), getGraphMap(userId, req.getKgName()));


        List<DWPrebuildConcept> concepts = prebuildConceptRepository.findByModelAndConceptIds(req.getModelId(), req.getFindAttrConceptIds());

        if (concepts == null || concepts.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Integer, String> modelConceptNameMap = new HashMap<>();

        //概念名称映射属性名称与属性
        for (DWPrebuildConcept concept : concepts) {
            modelConceptNameMap.put(concept.getId(), concept.getName());
        }

        List<DWPrebuildAttr> attrs = prebuildAttrRepository.findByConceptIds(req.getFindAttrConceptIds());
        if (attrs == null || attrs.isEmpty()) {
            return new ArrayList<>();
        }

        List<PreBuilderMatchAttrRsp> matchAttrRspList = attrs.stream().map(ConvertUtils.convert(PreBuilderMatchAttrRsp.class))
                .collect(Collectors.toList());

        List<SchemaQuoteReq> dataMapReqList = req.getSchemaQuoteReqList();

        Map<Integer, Long> modelKgConceptIdMap = new HashMap<>();

        //已引入的shcema概念名称-属性名称-属性类型映射
        Map<String, SchemaQuoteReq> conceptQuoteMap = new HashMap<>();
        Map<String, Map<String, SchemaQuoteAttrReq>> conceptAttrQuoteMap = new HashMap<>();
        if (dataMapReqList != null) {
            for (SchemaQuoteReq schemaQuoteReq : dataMapReqList) {

                if (!req.getConceptIds().contains(schemaQuoteReq.getModelConceptId())) {
                    req.getConceptIds().add(schemaQuoteReq.getModelConceptId());
                }

                modelKgConceptIdMap.put(schemaQuoteReq.getModelConceptId(), schemaQuoteReq.getConceptId());

                conceptQuoteMap.put(schemaQuoteReq.getModelId() + "_" + schemaQuoteReq.getEntityName(), schemaQuoteReq);

                Map<String, SchemaQuoteAttrReq> quoteAttrReqMap = conceptAttrQuoteMap.containsKey(schemaQuoteReq.getConceptName()) ? conceptAttrQuoteMap.get(schemaQuoteReq.getConceptName()) : new HashMap<>();

                if (schemaQuoteReq.getAttrs() != null) {
                    for (SchemaQuoteAttrReq quoteAttrReq : schemaQuoteReq.getAttrs()) {
                        quoteAttrReqMap.put(quoteAttrReq.getAttrName(), quoteAttrReq);
                    }
                }

                conceptAttrQuoteMap.put(schemaQuoteReq.getConceptName(), quoteAttrReqMap);
            }
        }

        for (PreBuilderMatchAttrRsp matchAttrRsp : matchAttrRspList) {

            matchAttrRsp.setConceptName(modelConceptNameMap.get(matchAttrRsp.getConceptId()));

            String status;
            String key = matchAttrRsp.getModelId() + "_" + modelConceptNameMap.get(matchAttrRsp.getConceptId());
            if (!conceptQuoteMap.containsKey(key)) {
                //概念还未引入，不能引入属性
                status = "-";
                matchAttrRsp.setAttrMatchStatus(status);
                continue;
            }

            SchemaQuoteReq quoteConcept = conceptQuoteMap.get(key);


            //映射到图谱的概念名
            String conceptName = quoteConcept.getConceptName();


            //在引入的属性里面找是否映射
            if (conceptAttrQuoteMap.containsKey(conceptName) && conceptAttrQuoteMap.get(conceptName).containsKey(matchAttrRsp.getName())) {

                //引用未保存的图谱存在有同名概念下有同名属性
                SchemaQuoteAttrReq quoteAttrReq = conceptAttrQuoteMap.get(quoteConcept.getConceptName()).get(matchAttrRsp.getName());

                if (!quoteAttrReq.getAttrType().equals(matchAttrRsp.getAttrType())) {
                    status = "数据类型冲突";
                } else if (matchAttrRsp.getAttrType() == 0) {

                    //都为数值属性
                    if (matchAttrRsp.getDataType().equals(quoteAttrReq.getDataType())) {
                        if (quoteAttrReq.getAttrId() == null) {
                            status = "可引入";
                        } else {
                            status = "已存在";
                        }
                    } else {
                        status = "数值属性类型冲突";
                    }
                } else {
                    //都为对象属性,值域一样
                    if (quoteAttrReq.getModelRange().equals(matchAttrRsp.getRange())) {
                        if (quoteAttrReq.getAttrId() == null) {
                            status = "可引入";
                        } else {
                            status = "已存在";
                        }
                    } else {
                        status = "对象属性值域冲突";
                    }
                }

            } else if (conceptAttrMap.containsKey(conceptName) && conceptAttrMap.get(conceptName).containsKey(matchAttrRsp.getName())) {

                //原图谱已经存在同概念有同名属性
                AttributeDefinitionRsp attrGraph = conceptAttrMap.get(conceptName).get(matchAttrRsp.getName());

                if (!attrGraph.getType().equals(matchAttrRsp.getAttrType())) {
                    status = "数据类型冲突";
                } else if (matchAttrRsp.getAttrType() == 0) {

                    //都为数值属性
                    if (matchAttrRsp.getDataType().equals(attrGraph.getDataType())) {
                        status = "已存在";
                    } else {
                        status = "数值属性类型冲突";
                    }
                } else {

                    //值域概念已经引入 判断是否在已经定义的值域值内
                    if (req.getConceptIds().contains(matchAttrRsp.getRange())) {

                        List<Long> ranges = attrGraph.getRangeValue();

                        //已经引入的概念在图谱中的id是在改对象属性的值域内
                        if (ranges.contains(modelKgConceptIdMap.get(matchAttrRsp.getRange()))) {
                            status = "已存在";
                        } else {
                            status = "对象属性值域冲突";
                        }
                    } else {
                        status = matchAttrRsp.getRangeName() + "概念未挂载";
                    }
                }
            } else if (matchAttrRsp.getAttrType().equals(1)) {
                //已引入的概念没有同名属性 如果是对象属性需要判断值域

                if (req.getConceptIds().contains(matchAttrRsp.getRange())) {
                    status = "可引入";
                } else {
                    status = matchAttrRsp.getRangeName() + "概念未引入";
                }
            } else {
                //没有同名的数值属性
                status = "可引入";
            }

            if (matchAttrRsp.getAttrType().equals(1) && ("可引入".equals(status) || "已存在".equals(status))) {
                //可引入/已存在的对象属性，看边属性状态

                List<DWPrebuildRelationAttr> relationAttrList = prebuildRelationAttrRepository.findAll(Example.of(DWPrebuildRelationAttr.builder().attrId(matchAttrRsp.getId()).build()));
                if (relationAttrList != null) {

                    List<PreBuilderRelationAttrRsp> matchRelationAttrList = new ArrayList<>();
                    if ("可引入".equals(status)) {

                        for (DWPrebuildRelationAttr relationAttr : relationAttrList) {

                            PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                            BeanUtils.copyProperties(relationAttr, relationAttrRsp);
                            relationAttrRsp.setAttrMatchStatus("可引入");
                            matchRelationAttrList.add(relationAttrRsp);

                        }

                    } else {

                        SchemaQuoteAttrReq quoteAttrReq = conceptAttrQuoteMap.get(quoteConcept.getConceptName()) != null ? conceptAttrQuoteMap.get(quoteConcept.getConceptName()).get(matchAttrRsp.getName()) : null;
                        AttributeDefinitionRsp attributeDefinitionRsp = conceptAttrMap.get(conceptName) != null ? conceptAttrMap.get(conceptName).get(matchAttrRsp.getName()) : null;


                        if (quoteAttrReq != null && quoteAttrReq.getRelationAttrs() != null && !quoteAttrReq.getRelationAttrs().isEmpty()) {

                            Map<String, SchemaQuoteRelationAttrReq> quoteRelationAttrReqMap = new HashMap<>();
                            for (SchemaQuoteRelationAttrReq schemaQuoteRelationAttrReq : quoteAttrReq.getRelationAttrs()) {
                                quoteRelationAttrReqMap.put(schemaQuoteRelationAttrReq.getName(), schemaQuoteRelationAttrReq);
                            }

                            for (DWPrebuildRelationAttr relationAttr : relationAttrList) {
                                PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                                BeanUtils.copyProperties(relationAttr, relationAttrRsp);

                                if (quoteRelationAttrReqMap.containsKey(relationAttr.getName())) {

                                    if (quoteRelationAttrReqMap.get(relationAttr.getName()).getDataType().equals(relationAttr.getDataType())) {
                                        relationAttrRsp.setAttrMatchStatus("已存在");
                                    } else {
                                        relationAttrRsp.setAttrMatchStatus("属性类型冲突");
                                    }

                                } else {
                                    relationAttrRsp.setAttrMatchStatus("可引入");
                                }

                                matchRelationAttrList.add(relationAttrRsp);


                            }

                        } else if (attributeDefinitionRsp != null && attributeDefinitionRsp.getExtraInfos() != null && !attributeDefinitionRsp.getExtraInfos().isEmpty()) {

                            Map<String, AttrExtraRsp> relationAttrReqMap = new HashMap<>();
                            for (AttrExtraRsp attrExtraRsp : attributeDefinitionRsp.getExtraInfos()) {
                                relationAttrReqMap.put(attrExtraRsp.getName(), attrExtraRsp);
                            }


                            for (DWPrebuildRelationAttr relationAttr : relationAttrList) {
                                PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                                BeanUtils.copyProperties(relationAttr, relationAttrRsp);

                                if (relationAttrReqMap.containsKey(relationAttr.getName())) {

                                    if (relationAttrReqMap.get(relationAttr.getName()).getDataType().equals(relationAttr.getDataType()) && relationAttrReqMap.get(relationAttr.getName()).getType().equals(0)) {
                                        relationAttrRsp.setAttrMatchStatus("已存在");
                                    } else {
                                        relationAttrRsp.setAttrMatchStatus("属性类型冲突");
                                    }

                                } else {
                                    relationAttrRsp.setAttrMatchStatus("可引入");
                                }

                                matchRelationAttrList.add(relationAttrRsp);

                            }

                        } else {

                            for (DWPrebuildRelationAttr relationAttr : relationAttrList) {
                                PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                                BeanUtils.copyProperties(relationAttr, relationAttrRsp);
                                relationAttrRsp.setAttrMatchStatus("可引入");
                                matchRelationAttrList.add(relationAttrRsp);

                            }
                        }


                    }

                    matchAttrRsp.setRelationAttrs(matchRelationAttrList);

                }

            }

            matchAttrRsp.setAttrMatchStatus(status);

        }

        return matchAttrRspList;
    }

    @Override
    public void saveGraphMap(String userId, PreBuilderGraphMapReq preBuilderGraphMapReq) {

        if(preBuilderGraphMapReq.getQuoteConfigs() == null || preBuilderGraphMapReq.getQuoteConfigs().isEmpty()){
            return ;
        }

        List<SchemaQuoteReq> quoteReqList = importToGraph(preBuilderGraphMapReq.getKgName(), preBuilderGraphMapReq.getQuoteConfigs());


        Map<Integer,Long> modelDataBaseIdMap = new HashMap<>();
        List<DWGraphMap> graphMapList = new ArrayList<>();
        List<DWGraphMapRelationAttr> graphMapRelationAttrList = new ArrayList<>();
        for(SchemaQuoteReq schemaQuoteReq : quoteReqList){

            Long modelDataBaseId;
            if(modelDataBaseIdMap.containsKey(schemaQuoteReq.getModelId())){
                modelDataBaseId = modelDataBaseIdMap.get(schemaQuoteReq.getModelId());
            }else{
                DWPrebuildModel model = prebuildModelRepository.getOne(schemaQuoteReq.getModelId());
                modelDataBaseId = model.getDatabaseId();
                modelDataBaseIdMap.put(model.getId(),modelDataBaseId);
            }

            //非数仓模式不保存映射关系
            if(modelDataBaseId == null){
                continue;
            }

            DWPrebuildConcept modelConcept = prebuildConceptRepository.getOne(schemaQuoteReq.getModelConceptId());

            List<String> conceptTableName = modelConcept.getTables();


            for(String tableName : conceptTableName){
                DWGraphMap graphMap = new DWGraphMap();
                BeanUtils.copyProperties(schemaQuoteReq, graphMap);
                graphMap.setTableName(tableName);
                graphMap.setDataBaseId(modelDataBaseId);
                graphMap.setSchedulingSwitch(0);
                graphMap.setKgName(preBuilderGraphMapReq.getKgName());
                graphMapList.add(graphMap);
            }

            List<SchemaQuoteAttrReq> schemaQuoteAttrReqList = schemaQuoteReq.getAttrs();

            if(schemaQuoteAttrReqList == null || schemaQuoteAttrReqList.isEmpty()){
                continue;
            }

            for(SchemaQuoteAttrReq schemaQuoteAttrReq : schemaQuoteAttrReqList){

                DWPrebuildAttr prebuildAttr = prebuildAttrRepository.getOne(schemaQuoteAttrReq.getModelAttrId());

                List<String> tables = prebuildAttr.getTables();

                for(String tableName : tables){
                    DWGraphMap graphMap = new DWGraphMap();
                    BeanUtils.copyProperties(schemaQuoteReq, graphMap);
                    BeanUtils.copyProperties(schemaQuoteAttrReq,graphMap);
                    graphMap.setTableName(tableName);
                    graphMap.setDataBaseId(modelDataBaseId);
                    graphMap.setSchedulingSwitch(0);
                    graphMap.setKgName(preBuilderGraphMapReq.getKgName());
                    graphMapList.add(graphMap);
                }

                if(!schemaQuoteAttrReq.getAttrType().equals(1) && schemaQuoteAttrReq.getRelationAttrs() != null && !schemaQuoteAttrReq.getRelationAttrs().isEmpty()){
                    continue;
                }

                List<SchemaQuoteRelationAttrReq> schemaQuoteRelationAttrReqs = schemaQuoteAttrReq.getRelationAttrs();

                for(SchemaQuoteRelationAttrReq schemaQuoteRelationAttrReq : schemaQuoteRelationAttrReqs){

                    DWPrebuildRelationAttr relationAttr = prebuildRelationAttrRepository.getOne(schemaQuoteRelationAttrReq.getModelAttrId());
                    List<String> tableNames = relationAttr.getTables();

                    for(String tableName : tableNames){

                        DWGraphMapRelationAttr graphMapRelationAttr = new DWGraphMapRelationAttr();
                        BeanUtils.copyProperties(schemaQuoteRelationAttrReq,graphMapRelationAttr);
                        graphMapRelationAttr.setTableName(tableName);
                        graphMapRelationAttr.setDataBaseId(modelDataBaseId);
                        graphMapRelationAttr.setSchedulingSwitch(0);
                        graphMapRelationAttr.setKgName(preBuilderGraphMapReq.getKgName());
                        graphMapRelationAttr.setModelId(schemaQuoteReq.getModelId());
                        graphMapRelationAttrList.add(graphMapRelationAttr);

                    }

                }

            }

        }

        graphMapRepository.saveAll(graphMapList);
        graphMapRelationAttrRepository.saveAll(graphMapRelationAttrList);

        //生成订阅任务
        createSchedulingConfig(preBuilderGraphMapReq.getKgName());
        return ;
    }

    private void createSchedulingConfig(String kgName) {

        List<SchemaQuoteReq> schemaQuoteReqList = getGraphMap(SessionHolder.getUserId(),kgName);

        if(schemaQuoteReqList == null || schemaQuoteReqList.isEmpty()){
            return;
        }

        Map<Integer, List<SchemaQuoteReq>> schemaModelMap = new HashMap<>();
        for(SchemaQuoteReq schemaQuoteReq : schemaQuoteReqList){
            if(schemaModelMap.containsKey(schemaQuoteReq.getModelId())){
                schemaModelMap.get(schemaQuoteReq.getModelId()).add(schemaQuoteReq);
            }else{
                List<SchemaQuoteReq> schemaQuoteReqs = new ArrayList<>();
                schemaModelMap.put(schemaQuoteReq.getModelId(),schemaQuoteReqs);
            }
        }

        List<Integer> existModelTaskList = new ArrayList<>();


        for(SchemaQuoteReq schemaQuoteReq : schemaQuoteReqList){
            Integer modelId = schemaQuoteReq.getModelId();
            if(existModelTaskList.contains(modelId)){
                continue;
            }

            DWPrebuildModel model = prebuildModelRepository.getOne(modelId);

            List<String> tableNames =schemaQuoteReq.getTables();
            String kgTaskName = AccessTaskType.KG.getDisplayName()+"_"+kgName+"_"+modelId;

            for(String tableName : tableNames){
                accessTaskService.createKtrTask(tableName,model.getDatabaseId(),kgName,0);
                accessTaskService.createTransfer(tableName,model.getDatabaseId(),Lists.newArrayList(),null,null,null,kgName);
            }

            List<DataMapReq> dataMapReqList = quote2DataMap(schemaModelMap.get(modelId));
            existModelTaskList.add(modelId);

            DWTaskRsp kgTaskRsp = accessTaskService.getByTaskName(kgTaskName);

            if(kgTaskRsp == null){
                kgTaskRsp = new DWTaskRsp();
                kgTaskRsp.setName(kgTaskName);
                kgTaskRsp.setTaskType(AccessTaskType.KG.getDisplayName());
                kgTaskRsp.setUserId(SessionHolder.getUserId());
                kgTaskRsp.setStatus(1);
                kgTaskRsp.setOutputs(new ArrayList<>());
            }

            KgConfigRsp config = new KgConfigRsp();
            config.setKgName(kgName);
            config.setDataMapping(dataMapReqList);
            config.setIsScheduled(1);
            kgTaskRsp.setConfig(JacksonUtils.writeValueAsString(config));

            accessTaskService.saveTask(kgTaskRsp);
        }
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


        List<SchemaQuoteReq> needAddConcepts = new ArrayList<>();

        Map<String, Long> conceptNameIdMap = new HashMap<>();
        for (SchemaQuoteReq schemaQuoteReq : quoteConfigs) {

            if (schemaQuoteReq.getConceptId() == null) {
                //概念不存在，添加属性

                if (schemaQuoteReq.getPConceptId() == null) {

                    //父概念还未添加 需要先添加父概念才能添加该概念
                    needAddConcepts.add(schemaQuoteReq);
                } else {

                    if (conceptNameIdMap.containsKey(schemaQuoteReq.getConceptName())) {
                        continue;
                    }
                    ConceptAddReq conceptAddReq = new ConceptAddReq();
                    conceptAddReq.setName(schemaQuoteReq.getConceptName());
                    conceptAddReq.setParentId(schemaQuoteReq.getPConceptId());

                    Long conceptId = graphEditService.createConcept(kgName, conceptAddReq);
                    schemaQuoteReq.setConceptId(conceptId);
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

            if (schemaQuoteReq.getAttrs() == null || schemaQuoteReq.getAttrs().isEmpty()) {
                continue;
            }

            for (SchemaQuoteAttrReq attrReq : schemaQuoteReq.getAttrs()) {

                Integer attrId;

                if (attrReq.getAttrId() != null) {
                    attrId = attrReq.getAttrId();
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

                    attrId = attributeService.addAttrDefinition(kgName, attrDefinitionReq);
                }

                attrReq.setAttrId(attrId);

                if (attrReq.getAttrType().equals(1) && attrReq.getRelationAttrs() != null) {

                    for (SchemaQuoteRelationAttrReq relationAttrReq : attrReq.getRelationAttrs()) {

                        Integer relaAttrId;
                        if (relationAttrReq.getAttrId() == null) {

                            EdgeAttrDefinitionReq edgeAttrDefinitionReq = new EdgeAttrDefinitionReq();
                            edgeAttrDefinitionReq.setDataType(relationAttrReq.getDataType());
                            edgeAttrDefinitionReq.setName(relationAttrReq.getName());
                            edgeAttrDefinitionReq.setDataUnit(relationAttrReq.getUnit());
                            edgeAttrDefinitionReq.setType(0);
                            relaAttrId = attributeService.addEdgeAttr(kgName, attrId, edgeAttrDefinitionReq);

                        } else {

                            relaAttrId = relationAttrReq.getAttrId();

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
    public void createModel(DWDatabase database, List<PreBuilderConceptRsp> preBuilderConceptRspList,String modelType,String yamlContent) {

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
    public List<String> getTypes(String userId) {

        List<String> modelTypes;
        UserDetailRsp userDetailRsp = getUserDetail();

        if("admin".equals(userDetailRsp.getUsername())){
            modelTypes = prebuildModelRepository.getAdminModelTypes();
        }else{
            modelTypes = prebuildModelRepository.getModelTypes(userId);
        }
        return modelTypes;
    }

    @Override
    public void create(PreBuilderCreateReq req, MultipartFile file) {

        UserDetailRsp userDetailRsp = getUserDetail();
        if(!"admin".equals(userDetailRsp.getUsername())){
            throw BizException.of(KgmsErrorCodeEnum.PERMISSION_NOT_MODEL_UPLOAD_ERROR);
        }

        if (file == null || !file.getOriginalFilename().endsWith(".xls") || !file.getOriginalFilename().endsWith(".xlsx") || !file.getOriginalFilename().endsWith(".zip")) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }

        try {

            String result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            if(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")){
                //纯模式
                addModelByExcel(file);

            }else{
                //行业标准

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
        }
        return ;

    }

    private void addModelByExcel(MultipartFile file) {

        try {
            ExcelParser excelParser = new ExcelParser(file.getInputStream(), file.getOriginalFilename());

            Sheet sheet = excelParser.wb.getSheetAt(0);
            excelParser.checkTitle(sheet.getRow(0),ExcelParser.CONCEPT);
            int rows = sheet.getPhysicalNumberOfRows();
            List<PreBuilderConceptRsp> conceptRspList = Lists.newArrayList();

            Map<String,PreBuilderConceptRsp> conceptMap = new HashMap<>();
            for (int i = 1; i <= rows; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String parentName = excelParser.getCellValue(row.getCell(0));
                    String parentMeaningTag = excelParser.getCellValue(row.getCell(1));
                    String sonName = excelParser.getCellValue(row.getCell(2));
                    String sonMeaningTag = excelParser.getCellValue(row.getCell(3));
                    if (!org.springframework.util.StringUtils.hasText(parentName)) {
                        throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
                    }
                    if (!org.springframework.util.StringUtils.hasText(sonName)) {
                        throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
                    }
                    if (Objects.equals(parentMeaningTag, sonMeaningTag) && parentName.equals(sonMeaningTag)) {
                        throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
                    }

                }
            }


        }catch (Exception e){
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.MODEL_PARSER_ERROR);
        }


    }

    @Override
    public List<SchemaQuoteReq> getGraphMap(String userId, String kgName) {

        List<DWGraphMap> dwGraphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).build()));

        if(dwGraphMapList == null || dwGraphMapList.isEmpty()){
            return new ArrayList<>();
        }

        Map<String,SchemaQuoteReq> schemaQuoteReqMap = new HashMap<>();
        Map<String,Map<String,SchemaQuoteAttrReq>> schemaQuoteAttrMap = new HashMap<>();
        Map<String,Map<String,List<String>>> schemaAuoteRelationAttrMap = new HashMap<>();

        for(DWGraphMap graphMap : dwGraphMapList){

            SchemaQuoteReq schemaQuoteReq;
            String conceptKey = graphMap.getEntityName()+graphMap.getConceptName()+graphMap.getModelId();

            if(schemaQuoteReqMap.containsKey(conceptKey)){

                schemaQuoteReq = schemaQuoteReqMap.get(conceptKey);

                if(!schemaQuoteReq.getTables().contains(graphMap.getTableName())){
                    schemaQuoteReq.getTables().add(graphMap.getTableName());
                }
            }else{

                schemaQuoteReq = new SchemaQuoteReq();
                BeanUtils.copyProperties(graphMap,schemaQuoteReq);

                schemaQuoteReq.setAttrs(new ArrayList<>());
                schemaQuoteReq.setTables(Lists.newArrayList(graphMap.getTableName()));
                schemaQuoteAttrMap.put(conceptKey,new HashMap<>());
                schemaQuoteReqMap.put(conceptKey,schemaQuoteReq);
        }

            if(graphMap.getAttrId() == null){
                //概念映射 没有属性
                continue;
            }

            //设置属性
            SchemaQuoteAttrReq schemaQuoteAttrReq;

            if(!schemaQuoteAttrMap.get(conceptKey).containsKey(graphMap.getAttrName())){
                schemaQuoteAttrReq = new SchemaQuoteAttrReq();
                BeanUtils.copyProperties(graphMap,schemaQuoteAttrReq);

                if(schemaQuoteAttrReq.getAttrType().equals(1)){
                    schemaQuoteAttrReq.setRelationAttrs(new ArrayList<>());
                }

                schemaQuoteAttrReq.setTables(Lists.newArrayList(graphMap.getTableName()));

                schemaQuoteReq.getAttrs().add(schemaQuoteAttrReq);
                schemaQuoteAttrMap.get(conceptKey).put(graphMap.getAttrName(),schemaQuoteAttrReq);
            }else{
                schemaQuoteAttrReq =  schemaQuoteAttrMap.get(conceptKey).get(graphMap.getAttrName());

                if(!schemaQuoteAttrReq.getTables().contains(graphMap.getTableName())){
                    schemaQuoteAttrReq.getTables().add(graphMap.getTableName());
                }
            }

            //不是对象属性，不查边属性
            if(!schemaQuoteAttrReq.getAttrType().equals(1)){
                continue;
            }

            List<DWGraphMapRelationAttr> graphMapRelationAttrList = graphMapRelationAttrRepository.findAll(Example.of(DWGraphMapRelationAttr.builder().attrId(schemaQuoteAttrReq.getAttrId()).kgName(kgName).modelId(schemaQuoteReq.getModelId()).build()));
            if(graphMapRelationAttrList == null || graphMapRelationAttrList.isEmpty()){
                continue;
            }

            for(DWGraphMapRelationAttr graphMapRelationAttr : graphMapRelationAttrList){

                if(schemaAuoteRelationAttrMap.containsKey(conceptKey)
                        && schemaAuoteRelationAttrMap.get(conceptKey).containsKey(schemaQuoteAttrReq.getAttrName())
                        && schemaAuoteRelationAttrMap.get(conceptKey).get(schemaQuoteAttrReq.getAttrName()).contains(graphMapRelationAttr.getName())){
                    continue;
                }else{

                    SchemaQuoteRelationAttrReq schemaQuoteRelationAttrReq = new SchemaQuoteRelationAttrReq();
                    BeanUtils.copyProperties(graphMapRelationAttr,schemaQuoteRelationAttrReq);
                    schemaQuoteRelationAttrReq.setTables(Lists.newArrayList(graphMap.getTableName()));
                    schemaQuoteAttrReq.getRelationAttrs().add(schemaQuoteRelationAttrReq);
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
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());


        if(!req.isGraph() && req.isManage() && !req.isUser() && !req.isDw()){
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
                    tags.add(criteriaBuilder.isNotNull(root.get("databaseId")));;
                }

                if (req.isGraph()) {
                    tags.add(criteriaBuilder.isNull(root.get("databaseId")));
                }

                if (req.isManage()) {
                    tags.add(criteriaBuilder.equal(root.get("username").as(String.class),"admin"));
                }

                if (req.isUser()) {
                    tags.add(criteriaBuilder.notEqual(root.get("username").as(String.class),"admin"));
                }

                predicates.add(criteriaBuilder.or(tags.toArray(new Predicate[]{})));

                if (!StringUtils.isEmpty(req.getModelType())) {

                    Predicate modelTypeEq = criteriaBuilder.equal(root.get("modelType").as(String.class), req.getModelType());
                    predicates.add(modelTypeEq);
                }

                if("admin".equals(username)){
                    //管理员
                    if(!StringUtils.isEmpty(req.getUsername())){
                        Predicate username = criteriaBuilder.equal(root.get("username").as(String.class), req.getUsername());
                        predicates.add(username);
                    }

                }else{
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

        if("admin".equals(username)){
            prebuildModelRepository.deleteById(id);
        }else{
            prebuildModelRepository.delete(DWPrebuildModel.builder().userId(userId).id(id).build());
        }

    }

    @Override
    public void update(String userId, Integer id, String status) {

        String username = getUserDetail().getUsername();

        DWPrebuildModel model = prebuildModelRepository.getOne(id);
        model.setStatus(status);

        if("admin".equals(username) || model.getUserId().equals(userId)){
            prebuildModelRepository.save(model);
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
                        prebuildRelationAttrRepository.save(relationAttr);
                    }
                }
            }
        }
    }

}
