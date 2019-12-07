package com.plantdata.kgcloud.domain.document.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdEntityBase;
import com.hiekn.pddocument.bean.element.PdRelation;
import com.hiekn.pddocument.bean.element.PdValue;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.document.entity.Attr;
import com.plantdata.kgcloud.domain.document.entity.Concept;
import com.plantdata.kgcloud.domain.document.entity.Document;
import com.plantdata.kgcloud.domain.document.repository.AttrRepository;
import com.plantdata.kgcloud.domain.document.repository.ConceptRepository;
import com.plantdata.kgcloud.domain.document.repository.DocumentRepository;
import com.plantdata.kgcloud.domain.document.req.AttrReq;
import com.plantdata.kgcloud.domain.document.req.ConceptReq;
import com.plantdata.kgcloud.domain.document.rsp.AttrRsp;
import com.plantdata.kgcloud.domain.document.rsp.ConceptRsp;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.rsp.ModelAnalysisRsp;
import com.plantdata.kgcloud.domain.scene.service.SceneService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private ConceptRepository conceptRepository;

    @Autowired
    private AttrRepository attrRepository;


    @Override
    public ApiReturn<List<ConceptRsp>> getModel(Integer sceneId, Integer id) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        Document doc = Document.builder().id(id).sceneId(scene.getId()).build();
        Optional<Document> document = documentRepository.findOne(Example.of(doc));

        if(document == null || !document.isPresent()){
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_NOT_EXISTS);
        }

        List<ConceptRsp> conceptRspList = null;

        if(document.get().getModelStatus() == 0){

            document.get().setModelStatus(1);
            documentRepository.save(document.get());
            //模式未提取
            List<ModelAnalysisRsp> modelAnalysisRsps = scene.getModelAnalysis();
            if(modelAnalysisRsps == null || modelAnalysisRsps.isEmpty()){
                return ApiReturn.success(Lists.newArrayList());
            }



            File file = new File(document.get().getSource());
            FileSystemResource resource = new FileSystemResource(file);
            PdDocument pdDocument = null;
            for(ModelAnalysisRsp model : modelAnalysisRsps){

                try {
                    String url = model.getUrl();
                    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
                    param.add("multiRequest", resource);
                    JSONObject rs = restTemplate.postForObject(url, param, JSONObject.class);
                    ApiReturn<PdDocument> apiReturn = new Gson().fromJson(JSON.toJSONString(rs), new TypeToken<ApiReturn<PdDocument>>(){}.getType());
                    if(apiReturn == null || apiReturn.getErrCode() != 200){
//                    throw BizException.of(KgDocumentErrorCodes.DOCUMENT_PARSE_ERROR);
                        System.out.println("请求解析接口报错:"+apiReturn.getErrCode()+"-"+apiReturn.getMessage());
                    }

                    pdDocument = apiReturn.getData();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(pdDocument != null && (pdDocument.getPdEntity() != null || pdDocument.getPdRelation() != null)){
                    break;
                }

            }

            if(pdDocument == null || (pdDocument.getPdEntity() == null && pdDocument.getPdRelation() == null)){
                return ApiReturn.success();
            }

            conceptRspList = pdDocumentParseSchema(pdDocument,document.get().getId());
            if(conceptRspList != null){

                Map<String, Integer> conceptIdMap = Maps.newHashMap();
                for(ConceptRsp conceptRsp : conceptRspList){
                    Concept concept = ConvertUtils.convert(Concept.class).apply(conceptRsp);
                    conceptRepository.save(concept);

                    conceptRsp.setId(concept.getId());
                    conceptIdMap.put(concept.getName(),concept.getId());
                }
                for(ConceptRsp conceptRsp : conceptRspList) {
                    List<AttrRsp> attrRspList = conceptRsp.getAttrs();
                    if (attrRspList != null && !attrRspList.isEmpty()) {
                        for (AttrRsp attrRsp : attrRspList) {
                            attrRsp.setConceptId(conceptRsp.getId());
                            Attr attr = ConvertUtils.convert(Attr.class).apply(attrRsp);

                            if(attr.getType().equals(1)){
                                Integer conceptId = conceptIdMap.get(attr.getRangeTo());
                                attr.setRangeTo(conceptId+"");
                                attrRsp.setRangeTo(conceptId+"");
                            }

                            attrRepository.save(attr);
                            attrRsp.setId(attr.getId());
                        }
                    }
                }

            }

        }else{
            //模式已提取
            List<Concept> conceptList = conceptRepository.findAll(Example.of(Concept.builder().documentId(id).build()));
            conceptRspList = new ArrayList<>(conceptList.size());
            for(Concept concept : conceptList){
                List<Attr> attrs = attrRepository.findAll(Example.of(Attr.builder().conceptId(concept.getId()).build()));

                if(attrs == null || attrs.isEmpty()){
                    continue;
                }

                List<AttrRsp> attrRspList = attrs.stream().map(ConvertUtils.convert(AttrRsp.class)).collect(Collectors.toList());
                ConceptRsp conceptRsp = ConvertUtils.convert(ConceptRsp.class).apply(concept);
                conceptRsp.setAttrs(attrRspList);
                conceptRspList.add(conceptRsp);
            }

        }

        if(conceptRspList == null || conceptRspList.isEmpty()){
            return ApiReturn.success();
        }

        return parseSchemaStatus(conceptRspList,scene);
    }

    private ApiReturn<List<ConceptRsp>> parseSchemaStatus(List<ConceptRsp> conceptRspList, Scene scene) {

        SchemaRsp schemaRsp = scene.getLabelModel();
        Map<String,Long> conceptIdMap = Maps.newHashMap();
        if(schemaRsp != null){

            for (ConceptRsp conceptBean : conceptRspList){
                Long conceptId = null;
                for (BaseConceptRsp typeBean : schemaRsp.getTypes()){
                    if (typeBean.getName().equals(conceptBean.getName())){
                        conceptId = typeBean.getId();
                        conceptBean.setConceptId(conceptId);
                    }
                }


                if(conceptId == null){
                    continue;
                }
                conceptIdMap.put(conceptBean.getId()+"",conceptId);
                if(schemaRsp.getAttrs() != null && !schemaRsp.getAttrs().isEmpty()){
                    for (AttrRsp dataBean : conceptBean.getAttrs()){
                        dataBean.setAttId(null);
                        if(dataBean.getIsIgnore()){
                            continue;
                        }
                        for(AttributeDefinitionRsp attBean : schemaRsp.getAttrs()){
                            if(attBean.getName().equals(dataBean.getName())){
                                //匹配到原图谱属性
                                dataBean.setAttName(attBean.getName());
                                dataBean.setAttId(attBean.getId().intValue());
                                dataBean.setAttType(attBean.getType().intValue());
                                dataBean.setAttDataType(attBean.getDataType());
                                dataBean.setAttRange(attBean.getRange());
                            }
                        }

                    }
                }
            }
        }

        for (ConceptRsp conceptBean : conceptRspList){
            if (conceptBean.getConceptId() == null){
                conceptBean.setConceptStatus("新增");
            } else {
                conceptBean.setConceptStatus("已匹配");
            }
            for (AttrRsp dataBean : conceptBean.getAttrs()){
                //已忽略状态
                if(dataBean.getIsIgnore()){
                    dataBean.setAttStatus("已忽略");
                    continue;
                }
                //未匹配到图谱的属性 为新增状态
                if(dataBean.getAttId() == null) {
                    dataBean.setAttStatus("新增");
                    continue;
                }
                //匹配到图谱属性 且类型都一致
                if(dataBean.getType().equals(dataBean.getAttType())){
                    if(dataBean.getType().equals(0)){
                        if (dataBean.getDataType().equals(dataBean.getAttDataType())){
                            if(!dataBean.getOldName().equals(dataBean.getName())){
                                dataBean.setAttStatus("已合并");//改过名的为已合并
                            }else{
                                dataBean.setAttStatus("已匹配");//没改过的为已匹配
                            }
                        }else {
                            dataBean.setAttStatus("冲突,属性类型不匹配");
                        }
                    }else if(dataBean.getType().equals(1)){
                        List<Long> rangeList = dataBean.getAttRange();
                        if(rangeList.contains(conceptIdMap.get(dataBean.getRangeTo()))){
                            if(!dataBean.getOldName().equals(dataBean.getName())){
                                dataBean.setAttStatus("已合并");
                            }else{
                                dataBean.setAttStatus("已匹配");
                            }
                        }else {
                            dataBean.setAttStatus("冲突,值域不匹配");
                        }
                    }
                }else if(dataBean.getType().equals(2)){
                    dataBean.setAttStatus("私有");
                }else {
                    dataBean.setAttStatus("冲突,属性类别不匹配");
                }
            }
        }

        Map<String, ConceptRsp> conceptMap = Maps.newHashMap();
        for (ConceptRsp conceptBean : conceptRspList) {
            if (conceptMap.containsKey(conceptBean.getName())){
                ConceptRsp concept = conceptMap.get(conceptBean.getName());
                for (AttrRsp data : conceptBean.getAttrs()){
                    concept.getAttrs().add(data);
                }
            } else {
                conceptMap.put(conceptBean.getName(),conceptBean);
            }
        }
        List<ConceptRsp> conceptBeanList = Lists.newArrayList();
        for (String str : conceptMap.keySet()){
            conceptBeanList.add(conceptMap.get(str));
        }

        for (ConceptRsp conceptBean : conceptBeanList) {
            List<String> nameList = Lists.newArrayList();
            for (AttrRsp dataBean : conceptBean.getAttrs()) {
                if (nameList.contains(dataBean.getName())){
                    dataBean.setAttStatus("冲突,存在多个同名属性");
                }
                nameList.add(dataBean.getName());
            }
        }

        return ApiReturn.success(conceptRspList);
    }

    private List<ConceptRsp> pdDocumentParseSchema(PdDocument pdDocument, Integer documentId) {

        List<PdEntity> entityList = pdDocument.getPdEntity();
        if(entityList == null || entityList.isEmpty()){
            return Lists.newArrayList();
        }

        Map<String, ConceptRsp> conceptRspMap = Maps.newHashMap();
        Map<String,Map<String, AttrRsp>> attrRspMap = Maps.newHashMap();
        for(PdEntity entity : entityList){
            ConceptRsp conceptRsp = null;
            if(conceptRspMap.containsKey(entity.getConceptName())){
                conceptRsp = conceptRspMap.get(entity.getConceptName());
            }else{
                conceptRsp = ConceptRsp.builder().oldName(entity.getConceptName()).Name(entity.getConceptName()).isImportGraph(false)
                        .attrs(Lists.newArrayList()).documentId(documentId).build();
                conceptRspMap.put(entity.getConceptName(),conceptRsp);
            }



            if(!attrRspMap.containsKey(conceptRsp.getName())){
                attrRspMap.put(conceptRsp.getName(), Maps.newHashMap());
            }
            Map<String, AttrRsp> rspMap = attrRspMap.get(conceptRsp.getName());

            List<PdValue> elements = entity.getElements();
            if(elements != null && !elements.isEmpty()){
                elements.forEach(element -> {

                    if(!rspMap.containsKey(element.getAttName())){
                        AttrRsp attrRsp = AttrRsp.builder().name(element.getAttName()).oldName(element.getAttName())
                                .type(0).dataType(Integer.parseInt(element.getDataType())).isIgnore(false)
                                .isImportGraph(false).count(1).ps(element.getName()).build();
                        rspMap.put(element.getAttName(),attrRsp);
                    }else{
                        AttrRsp attrRsp = rspMap.get(element.getAttName());
                        attrRsp.setCount(attrRsp.getCount() + 1);
                        if(attrRsp.getPs() == null || attrRsp.getPs().isEmpty()){
                            attrRsp.setPs(element.getName());
                        }
                    }
                });

            }

        }

        List<PdRelation> relationList = pdDocument.getPdRelation();
        if(relationList != null && !relationList.isEmpty()){

            for(PdRelation relation : relationList){
                PdEntityBase subject = relation.getSubject();
                PdEntityBase object = relation.getObject();
                String attName = relation.getAttName();
                Map<String, AttrRsp> rspMap = attrRspMap.get(subject.getConceptName());
                if(rspMap.containsKey(attName)){
                    AttrRsp attrRsp = rspMap.get(attName);
                    attrRsp.setCount(attrRsp.getCount() + 1);
                    if(attrRsp.getPs() == null || attrRsp.getPs().isEmpty()){
                        attrRsp.setPs(object.getName());
                    }
                }else{
                    AttrRsp attrRsp = AttrRsp.builder().name(attName).oldName(attName)
                            .type(1).isIgnore(false).isImportGraph(false).count(1).rangeTo(object.getConceptName())
                            .ps(object.getName()).build();
                    rspMap.put(attName,attrRsp);
                }

            }

        }

        List<ConceptRsp> conceptList = new ArrayList<>(conceptRspMap.size());
        for(Map.Entry<String, ConceptRsp> entry : conceptRspMap.entrySet()){

            String key = entry.getKey();
            Map<String, AttrRsp> rspMap = attrRspMap.get(key);
            if(rspMap != null){

                List<AttrRsp> attrRspList = new ArrayList<>(rspMap.size());
                for(Map.Entry<String, AttrRsp> attrRspEntry : rspMap.entrySet()){
                    attrRspList.add(attrRspEntry.getValue());
                }

                entry.getValue().setAttrs(attrRspList);
            }

            conceptList.add(entry.getValue());
        }

        return conceptList;
    }

    @Override
    public ApiReturn saveModelConcept(ConceptReq conceptReq) {

        Optional<Document> document = documentRepository.findById(conceptReq.getDocumentId());
        Concept concept = conceptRepository.getOne(conceptReq.getId());
        if(concept.getIsImportGraph()){
            throw BizException.of(KgDocumentErrorCodes.MODEL_IS_IMPORT_GROUP);
        }
        conceptRepository.save(ConvertUtils.convert(Concept.class).apply(conceptReq));
        return ApiReturn.success();
    }

    @Override
    public ApiReturn saveModelAttr(AttrReq attrReq) {

        Optional<Document> document = documentRepository.findById(attrReq.getDocumentId());

        Attr attr = attrRepository.getOne(attrReq.getId());
        if(attr.getIsImportGraph()){
            throw BizException.of(KgDocumentErrorCodes.MODEL_IS_IMPORT_GROUP);
        }
        attrRepository.save(ConvertUtils.convert(Attr.class).apply(attrReq));
        return ApiReturn.success();
    }

    @Override
    public ApiReturn importGroup(Integer sceneId, Integer id) {
        return null;
    }

}
