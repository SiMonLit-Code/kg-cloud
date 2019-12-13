package com.plantdata.kgcloud.domain.document.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdEntityBase;
import com.hiekn.pddocument.bean.element.PdRelation;
import com.hiekn.pddocument.bean.element.PdValue;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.ConvertConstent;
import com.plantdata.kgcloud.domain.common.service.MongoDriver;
import com.plantdata.kgcloud.domain.common.service.SDKService;
import com.plantdata.kgcloud.domain.document.req.PdDocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.DataCheckRsp;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.service.SceneService;
import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BatchRelationRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private SceneService sceneService;

    @Autowired
    private MongoDriver mongoDriver;

    @Autowired
    private SDKService sdkService;

    @Override
    public ApiReturn<List<DataCheckRsp>> check(Integer sceneId, Integer id) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        if(!scene.getLabelModelType().equals(1) || scene.getLabelModel() == null ||scene.getLabelModel().getKgName() == null){
            //非图谱引入不做冲突检测
            return ApiReturn.success();
        }

        ApiReturn<List<PdDocument>> apiReturn = getPddocument(sceneId,id);
        if(apiReturn.getData() == null || apiReturn.getData().isEmpty()){
            return ApiReturn.success();
        }

        List<PdDocument> pdDocumentList = apiReturn.getData();
        List<DataCheckRsp> rsList = Lists.newArrayList();

        String kgName = scene.getLabelModel().getKgName();

        for(PdDocument pdDocument : pdDocumentList){
            List<PdEntity> entityList = pdDocument.getPdEntity();
            if(entityList == null || entityList.isEmpty()){
                continue;
            }

            for(PdEntity entity : entityList){

                List<PdValue> elements = entity.getElements();
                if(elements == null || elements.isEmpty()){
                    continue;
                }

                String name = entity.getName();
                Long conceptId = entity.getClassId();
                Long entityId = entity.getId();

                if(entityId == null){
                    //实体没入过图 从图谱里面查
                    entityId = sdkService.getEntityIdByName(kgName, name,conceptId);

                }

                if(entityId == null){
                    //图谱中不存在同名实体 没有冲突
                    for(PdValue element : elements){

                        DataCheckRsp bean = DataCheckRsp.builder().name(name).conceptId(conceptId).attName(element.getAttName())
                                .status("1").pdDocumentId(pdDocument.getId()).build();
                        rsList.add(bean);

                    }
                }else{
                    //查询图谱中的同名实体 判断属性值是否冲突

                    InfoBoxReq infoBoxReq = new InfoBoxReq();
                    infoBoxReq.setEntityIdList(Stream.of(entityId).collect(Collectors.toList()));


                    List<String> existAttrIdList = Lists.newArrayList();
                    InfoBoxRsp infoBoxRsp = sdkService.infobox(kgName,entityId);
                    EntityLinksRsp entityLinksRsp = infoBoxRsp.getSelf();
                    List<EntityLinksRsp.ExtraRsp> extra = entityLinksRsp.getExtraList();
                    if(Objects.nonNull(extra) && !extra.isEmpty()){
                        for(EntityLinksRsp.ExtraRsp ex :extra){
                            if(ex.getValue() != null){
                                if(ex.getAttrId() == 0){
                                    //私有属n
                                    existAttrIdList.add(ex.getName());
                                }else{
                                    existAttrIdList.add(ex.getAttrId()+"");
                                }
                            }
                        }
                    }


                    for(PdValue element : elements){

                        //已入图的属性不监测
                        if("已入图".equals(element.getPredicate())){
                            continue;
                        }
                        DataCheckRsp bean = DataCheckRsp.builder().name(name).conceptId(conceptId).attName(element.getAttName()).entityId(entityId)
                                .status("1").pdDocumentId(pdDocument.getId()).build();
                        if(!existAttrIdList.contains(element.getAttId()+"") && !existAttrIdList.contains(element.getAttName())){
                            bean.setStatus("1");
                        }else{
                            bean.setStatus("0");
                        }

                        rsList.add(bean);
                    }

                }
            }

            if(pdDocument.getPdRelation() != null && !pdDocument.getPdRelation().isEmpty()) {
                for (PdRelation relation : pdDocument.getPdRelation()) {
                    //已入图的属性不监测
                    if ("已入图".equals(relation.getPredicate())) {
                        continue;
                    }
                    DataCheckRsp bean =  DataCheckRsp.builder().name(relation.getSubject().getName()).conceptId(relation.getSubject().getClassId())
                            .attName(relation.getAttName()).entityId(relation.getSubject().getId())
                            .status("1").pdDocumentId(pdDocument.getId()).build();
                    rsList.add(bean);
                }
            }
        }

        return ApiReturn.success(rsList);
    }

    @Override
    public void importGroup(Integer sceneId, Integer id,Integer model) {

        String userId = SessionHolder.getUserId();
        Scene scene = sceneService.checkScene(sceneId,userId);

        if(!scene.getLabelModelType().equals(1) || scene.getLabelModel() == null ||scene.getLabelModel().getKgName() == null){
            //非图谱引入不入图
            return ;
        }

        List<PdDocument> pdDocumentList = getPddocument(sceneId,id).getData();
        if(pdDocumentList == null || pdDocumentList.isEmpty()){
            return ;
        }

        String kgName = scene.getLabelModel().getKgName();

        List<DataCheckRsp> checkList = check(sceneId,id).getData();
        Map<String, DataCheckRsp> checkMap = Maps.newHashMap();
        if(checkList != null && !checkList.isEmpty()){
            for(DataCheckRsp dataCheckBean : checkList){
                if(dataCheckBean.getStatus().equals("0")){
                    checkMap.put(dataCheckBean.getPdDocumentId()+"_"+dataCheckBean.getConceptId()+"_"+dataCheckBean.getName()+"_"+dataCheckBean.getAttName(),dataCheckBean);
                }
            }
        }

        List<OpenBatchSaveEntityRsp> importEntityList = Lists.newArrayList();
        List<String> existEntityNameList = Lists.newArrayList();
        for(PdDocument pdDocument : pdDocumentList){

            //基本数值属性
            if(pdDocument.getPdEntity() != null && !pdDocument.getPdEntity().isEmpty()){
                for(PdEntity entity : pdDocument.getPdEntity()){

                    OpenBatchSaveEntityRsp importEntityBean = new OpenBatchSaveEntityRsp();
                    importEntityBean.setConceptId(entity.getClassId());
                    importEntityBean.setName(entity.getName());
                    existEntityNameList.add(entity.getClassId()+"_"+entity.getName());
                    Long entityId =entity.getId();

                    if(entityId == null) {
                        entityId = sdkService.getEntityIdByName(kgName,entity.getName(),entity.getClassId());
                    }


                    importEntityBean.setId(entityId);
                    entity.setId(entityId);
                    Map<Integer, String> attMap = Maps.newHashMap();
                    Map<String,String> privateAttributes = Maps.newHashMap();

                    List<PdValue> elements = entity.getElements();
                    if(elements != null && !elements.isEmpty()){

                        for(PdValue element : elements){
                            Integer type = element.getType();
                            //数值属性在这边操作
                            if(type.equals(0)){

                                //已入图的属性不重复入图
                                if("已入图".equals(element.getPredicate())){
                                    continue;
                                }

                                Integer attId = element.getAttId();
                                String value = element.getName();
                                String attName = element.getAttName();
                                if(attId == null){
                                    //私有属性暂时不能通过这个添加
                                    privateAttributes.put(attName,value);
                                    //补充入图状态
                                    element.setPredicate("已入图");
                                    continue;
                                }
                                String key = pdDocument.getId()+"_"+entity.getClassId()+"_"+entity.getName()+"_"+attName;
                                switch (model){
                                    case 0:
                                        if(!checkMap.containsKey(key)){
                                            //没有冲突才添加
                                            attMap.put(attId,value);

                                            //补充入图状态
                                            element.setPredicate("已入图");
                                        }
                                        break;
                                    case 1:
                                        //冲突覆盖
                                        attMap.put(attId,value);
                                        //补充入图状态
                                        element.setPredicate("已入图");
                                        break;
                                    case 2:
                                        //冲突保留
                                        if(!checkMap.containsKey(key)){
                                            //没有冲突才添加
                                            attMap.put(attId,value);
                                        }
                                        //补充入图状态
                                        element.setPredicate("已入图");
                                        break;
                                    default:
                                        break;
                                }

                            }

                        }
                    }

                    if(!attMap.isEmpty()){
                        importEntityBean.setAttributes(attMap);
                    }

                    if(!privateAttributes.isEmpty()){
                        importEntityBean.setPrivateAttributes(privateAttributes);
                    }

                    importEntityList.add(importEntityBean);
                }
            }

            if(pdDocument.getPdRelation() != null && !pdDocument.getPdRelation().isEmpty()){
                for(PdRelation relation : pdDocument.getPdRelation()){
                    if(relation.getObject().getId() == null){
                        if(existEntityNameList.contains(relation.getObject().getClassId()+"_"+relation.getObject().getName())){
                            continue;
                        }
                        OpenBatchSaveEntityRsp importEntityBean = new OpenBatchSaveEntityRsp();
                        importEntityBean.setConceptId(relation.getObject().getClassId());
                        importEntityBean.setName(relation.getObject().getName());
                        importEntityList.add(importEntityBean);
                        existEntityNameList.add(relation.getObject().getClassId()+"_"+relation.getObject().getName());
                    }
                }
            }

        }

        Map<String,Long> idMap = Maps.newHashMap();
        if(!importEntityList.isEmpty()){
            List<OpenBatchSaveEntityRsp> success = sdkService.addBatchEntity(kgName,importEntityList);
            if(success != null && !success.isEmpty()){
                for(OpenBatchSaveEntityRsp entityBean : success){
                    idMap.put(entityBean.getConceptId()+"_"+entityBean.getName(),entityBean.getId());
                }
            }

            for(PdDocument pdDocument : pdDocumentList){

                if(pdDocument.getPdEntity() == null || pdDocument.getPdEntity().isEmpty()){
                    continue;
                }

                for(PdEntity entity : pdDocument.getPdEntity()) {

                    if (entity.getId() != null) {
                        continue;
                    }

                    Long entityId = idMap.get(entity.getClassId()+"_"+entity.getName());
                    if(entityId == null){

                        entityId = sdkService.getEntityIdByName(kgName,entity.getName(),entity.getClassId());
                        idMap.put(entity.getClassId()+"_"+entity.getName(),entityId);
                    }

                    /*List<PdValue> elements = entity.getElements();
                    if(elements != null && !elements.isEmpty()){
                        for(PdValue element : elements){

                            //已入图的属性不重复入图
                            if("已入图".equals(element.getPredicate())){
                                continue;
                            }
                            if(element.getAttId() != null){
                                //仅处理私有属性
                                continue;
                            }

                            String attName = element.getAttName();
                            Integer attType = 0;//私有属性默认都为数值属性
                            String value = element.getName();
                            element.setPredicate("已入图");
//                            sdkSerivce.addPrivateAtt(taskBean.getGraphName(),id,attName,attType,value);
                        }
                    }*/

                    entity.setId(entityId);
                }
            }
        }


        List<BatchRelationRsp> importRelationList = Lists.newArrayList();
        for( PdDocument pdDocument  : pdDocumentList){

            if(pdDocument.getPdRelation() == null || pdDocument.getPdRelation().isEmpty()){
                continue;
            }

            for(PdRelation relation : pdDocument.getPdRelation()) {

                if ("已入图".equals(relation.getPredicate())) {
                    continue;
                }
                PdEntityBase sub = relation.getSubject();
                PdEntityBase obj = relation.getObject();

                if(!idMap.containsKey(sub.getClassId()+"_"+sub.getName()) || !idMap.containsKey(obj.getClassId()+"_"+obj.getName())){
                    System.out.println("出错关系："+ JSON.toJSONString(relation));
                    continue;
                }
                relation.setPredicate("已入图");
                BatchRelationRsp importRelationBean = new BatchRelationRsp();
                importRelationBean.setAttrId(relation.getAttId());
                importRelationBean.setEntityConcept(sub.getClassId());
                importRelationBean.setEntityName(sub.getName());
                importRelationBean.setEntityId(idMap.get(sub.getClassId()+"_"+sub.getName()));
                importRelationBean.setAttrValueConcept(obj.getClassId());
                importRelationBean.setAttrValueId(idMap.get(obj.getClassId()+"_"+obj.getName()));
                importRelationBean.setAttrValueName(obj.getName());
                importRelationList.add(importRelationBean);
            }
        }

        //新增关系
        if(!importRelationList.isEmpty()){
            List<BatchRelationRsp> success = sdkService.addBatchRelation(scene.getLabelModel().getKgName(),importRelationList);

            //返回关系id写回去
            Map<String, BatchRelationRsp> relationMap = Maps.newHashMap();
            if(success != null && !success.isEmpty()){
                for(BatchRelationRsp relation : success){
                    relationMap.put(relation.getEntityName()+"_"+relation.getEntityConcept()+"_"+relation.getAttrValueName()+"_"+relation.getAttrValueConcept(),relation);
                }
            }

            if(!relationMap.isEmpty()){
                for( PdDocument pdDocument  : pdDocumentList){


                    if(pdDocument.getPdRelation() == null ||pdDocument.getPdRelation().isEmpty()){
                        continue;
                    }

                    for(PdRelation pdRelation : pdDocument.getPdRelation()){
                        PdEntityBase sub = pdRelation.getSubject();
                        PdEntityBase obj = pdRelation.getObject();
                        String key = sub.getName()+"_"+sub.getClassId()+"_"+obj.getName()+"_"+obj.getClassId();
                        BatchRelationRsp relationBean = relationMap.get(key);
                        if(relationBean != null){
                            pdRelation.getSubject().setId(relationBean.getEntityId());
                            pdRelation.getObject().setId(relationBean.getAttrValueId());
                            pdRelation.setTripleId(relationBean.getId());
                        }
                    }
                }
            }
        }

        //数据写回mongo
        updatePddocument(sceneId,id,pdDocumentList);

        return;
    }

    @Override
    public ApiReturn<List<PdDocument>> getPddocument(Integer sceneId, Integer id) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        List<Document> list = mongoDriver.get(ConvertConstent.databases,scene.getId()+"_"+ConvertConstent.collection_pddocument,new Document("doc_id",id));

        if(list == null || list.isEmpty()){
            return ApiReturn.success();
        }

        Document doc = list.get(0);

        List rs = doc.get("pd_document", List.class);

        if(rs != null && !rs.isEmpty()){
            List<PdDocument> pdDocumentList = new ArrayList<>(rs.size());
            for(Object document : rs){
                PdDocument pdDocument = JSON.parseObject(JSON.toJSONString(document),PdDocument.class);
                pdDocumentList.add(pdDocument);
            }

            return ApiReturn.success(pdDocumentList);
        }

        return ApiReturn.success(Lists.newArrayList());
    }

    @Override
    public ApiReturn updatePddocument(Integer sceneId,Integer id, List<PdDocument> pdDocumentList) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        Document filter = new Document("doc_id",id);

        Document doc = new Document("pd_document",JSON.toJSON(pdDocumentList));

        System.out.println(JSON.toJSONString(filter));
        System.out.println(JSON.toJSONString(doc));
        mongoDriver.update(ConvertConstent.databases,scene.getId()+"_"+ConvertConstent.collection_pddocument,filter,doc);

        return ApiReturn.success();
    }


}
