package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.dw.entity.*;
import com.plantdata.kgcloud.domain.dw.repository.*;
import com.plantdata.kgcloud.domain.dw.req.GraphMapReq;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.GraphMapService;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BaseConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-24 17:52
 **/
@Service
public class GraphMapServiceImpl implements GraphMapService {

    @Autowired
    private DWGraphMapRepository graphMapRepository;

    @Autowired
    private DWGraphMapRelationAttrRepository graphMapRelationAttrRepository;

    @Autowired
    private DWPrebuildAttrRepository attrRepository;

    @Autowired
    private DWPrebuildModelRepository modelRepository;

    @Autowired
    private AccessTaskService accessTaskService;

    @Autowired
    private PreBuilderService preBuilderService;
    @Autowired
    private KettleLogStatisticService kettleLogStatisticService;

    @Autowired
    private DWTableRepository tableRepository;

    @Autowired
    private GraphApplicationService graphApplicationService;

    @Autowired
    private DWDatabaseRepository dwDatabaseRepository;

    @Autowired
    private DWService dwService;
    @Override
    public List<GraphMapRsp> list(String userId, GraphMapReq graphMapReq) {

        List<DWGraphMap> graphMapList;

        deleteDataByNotExistConcept(graphMapReq.getKgName());

        if (graphMapReq.getAttrId() != null) {

            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).attrId(graphMapReq.getAttrId()).build()));

        } else {
            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).conceptId(graphMapReq.getConceptId()).build()));

            if (graphMapList == null) {
                graphMapList = Lists.newArrayList();
            }

            if(graphMapReq.getConceptId() != null){
                List<DWGraphMap> childGraphMaps = new ArrayList<>();
                getChildConceptMap(childGraphMaps, graphMapReq.getKgName(), graphMapReq.getDatabaseId(), graphMapReq.getConceptId());
                graphMapList.addAll(childGraphMaps);
            }


        }
        List<GraphMapRsp> rspList = graphMap2Rsp(graphMapList);

        BasicConverter.consumerIfNoNull(rspList, kettleLogStatisticService::fillGraphMapRspCount);
        return rspList;
    }



    @Override
    public void scheduleSwitch(Integer id, Integer status) {

        Optional<DWGraphMap> graphMapOpt = graphMapRepository.findById(id);
        if (!graphMapOpt.isPresent()) {
            return;
        }

        DWGraphMap graphMap = graphMapOpt.get();

        DWDatabaseRsp database = dwService.getDetail(graphMap.getDataBaseId());

        if(database == null){
            return ;
        }

        List<DWGraphMap> tabs = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMap.getKgName()).dataBaseId(graphMap.getDataBaseId()).tableName(graphMap.getTableName()).build()));

        if (tabs == null || tabs.isEmpty()) {
            return;
        }

        Optional<DWPrebuildModel> modelOptional = modelRepository.findById(graphMap.getModelId());
        if(!modelOptional.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.PRE_BUILD_MODEL_NOT_EXIST);
        }

        tabs.forEach(t -> t.setSchedulingSwitch(status));

        graphMapRepository.saveAll(tabs);

        String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + graphMap.getKgName() + "_" + graphMap.getModelId();
        if (status.equals(1)) {

            accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 1,graphMap.getKgName());
            if (database.getDataFormat().equals(1)) {
                accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), null, Lists.newArrayList(kgTaskName), null, null, graphMap.getKgName());
            } else {
                accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName), null, null, null, graphMap.getKgName());
            }
        } else {
            accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 0,graphMap.getKgName());

            if (database.getDataFormat().equals(1)) {
                accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, null, Lists.newArrayList(kgTaskName), graphMap.getKgName());
            } else {
                accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, Lists.newArrayList(kgTaskName), null, graphMap.getKgName());
            }
        }

        //更新订阅任务
        preBuilderService.createSchedulingConfig(graphMap.getKgName(), false,status);
    }

    @Override
    public void deleteSchedule(Integer id) {

        Optional<DWGraphMap> graphMap = graphMapRepository.findById(id);

        if(graphMap.isPresent()) {
            String kgName = graphMap.get().getKgName();

            graphMapRepository.deleteById(id);

            //更新订阅任务
            preBuilderService.createSchedulingConfig(kgName, false, 0);
        }
    }

    @Override
    public void scheduleSwitchByKgName(GraphMapReq graphMapReq) {

        List<DWGraphMap> graphMapList;
        if (graphMapReq.getAttrId() != null) {

            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).attrId(graphMapReq.getAttrId()).build()));

        } else {
            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).conceptId(graphMapReq.getConceptId()).build()));

            if (graphMapList != null && !graphMapList.isEmpty()) {

                List<DWGraphMap> childGraphMaps = new ArrayList<>();
                for (DWGraphMap graphMap : graphMapList) {
                    getChildConceptMap(childGraphMaps, graphMapReq.getKgName(), graphMapReq.getDatabaseId(), graphMap.getConceptId());
                }

                graphMapList.addAll(childGraphMaps);
            }

        }
        if (graphMapList == null || graphMapList.isEmpty()) {
            return;
        }

        graphMapList.forEach(t -> t.setSchedulingSwitch(graphMapReq.getStatus()));

        graphMapRepository.saveAll(graphMapList);

        List<String> exists = Lists.newArrayList();
        for(DWGraphMap graphMap : graphMapList){

            if(exists.contains(graphMap.getDataBaseId()+graphMap.getTableName())){
                continue;
            }

            exists.add(graphMap.getDataBaseId()+graphMap.getTableName());
            DWDatabaseRsp database = dwService.getDetail(graphMap.getDataBaseId());

            if(database == null){
                continue;
            }

            String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + graphMap.getKgName() + "_" + graphMap.getModelId();
            if (graphMapReq.getStatus().equals(1)) {

                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 1,graphMap.getKgName());
                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), null, Lists.newArrayList(kgTaskName), null, null, graphMap.getKgName());
                } else {

                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName), null, null, null, graphMap.getKgName());
                }
            } else {
                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 0,graphMap.getKgName());

                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, null, Lists.newArrayList(kgTaskName), graphMap.getKgName());
                } else {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, Lists.newArrayList(kgTaskName), null, graphMap.getKgName());
                }
            }

            //更新订阅任务
        }
        preBuilderService.createSchedulingConfig(graphMapReq.getKgName(), false,graphMapReq.getStatus());

    }

    @Override
    public void deleteDataByNotExistConcept(String kgName) {

        //查询这个图谱订阅的那些数据
        List<DWGraphMap> graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).build()));

        if(graphMapList == null || graphMapList.isEmpty()){
            return ;
        }

        //如果图谱现在没有概念，删除所有的订阅
        SchemaRsp schemaRsp = graphApplicationService.querySchema(kgName);
        if(schemaRsp == null || schemaRsp.getTypes() == null || schemaRsp.getTypes().isEmpty()){
            deleteConcept(graphMapList);
            return ;
        }

        //获取现在图谱已有的概念
        List<Long> existConceptIds = schemaRsp.getTypes().stream().map(BaseConceptRsp::getId).collect(Collectors.toList());
        List<Long> deleteConceptIds = Lists.newArrayList();

        List<String> existMapList = Lists.newArrayList();

        for(Iterator<DWGraphMap> it = graphMapList.iterator(); it.hasNext();){
            DWGraphMap graphMap = it.next();

            //图谱概念不存在，删除所有该概念的订阅
            if(!existConceptIds.contains(graphMap.getConceptId())){
                deleteConcept(graphMap);
                deleteConceptIds.add(graphMap.getConceptId());
                it.remove();
                continue;
            }

            //唯一标识这个映射的key,重复引入的删除
            String key = graphMap.getModelId()+graphMap.getModelConceptId()+graphMap.getConceptName()+graphMap.getTableName()+graphMap.getModelAttrId()+graphMap.getAttrId();
            if(existMapList.contains(key)){
                deleteConcept(graphMap);
                deleteConceptIds.add(graphMap.getConceptId());
                it.remove();
            }else{
                existMapList.add(key);
            }
        }

        if(graphMapList.isEmpty()){
            return;
        }

        //如果图谱属性都不存在，删除所有属性的订阅
        if(schemaRsp == null || schemaRsp.getAttrs() == null || schemaRsp.getAttrs().isEmpty()){
            List<DWGraphMap> attrMapList = graphMapList.stream().filter(map -> map.getAttrId() != null).collect(Collectors.toList());
            if(attrMapList != null && !attrMapList.isEmpty()){
                deleteConcept(attrMapList);
            }

            return ;
        }

        Map<Integer,AttributeDefinitionRsp> existAttrIds = schemaRsp.getAttrs().stream().collect(Collectors.toMap(AttributeDefinitionRsp::getId, Function.identity()));

        for(Iterator<DWGraphMap> it = graphMapList.iterator(); it.hasNext();){

            DWGraphMap graphMap = it.next();

            //实体的订阅可以保留
            if(graphMap.getAttrId() == null){
                continue;
            }

            //图谱中已不存在该属性，删除所有的订阅
            if(!existAttrIds.containsKey(graphMap.getAttrId())){
                deleteConcept(graphMap);
            }else if(graphMap.getAttrType().equals(1)){

                //图谱中存在该对象属性，查看边属性订阅关系
                AttributeDefinitionRsp attr = existAttrIds.get(graphMap.getAttrId());

                //图谱中边属性都不存在，所有该属性下边属性订阅都删除
                if(attr.getExtraInfos() == null || attr.getExtraInfos().isEmpty()){
                    deleteRelationAttr(kgName,attr.getId());
                }else{


                    List<String> relationAttrNames = attr.getExtraInfos().stream().map(AttrExtraRsp::getName).collect(Collectors.toList());
                    List<DWGraphMapRelationAttr> relationAttrList = graphMapRelationAttrRepository.findAll(Example.of(DWGraphMapRelationAttr.builder().kgName(kgName).modelAttrId(graphMap.getModelAttrId()).build()));

                    //没有边属性订阅，跳过
                    if(relationAttrList == null || relationAttrList.isEmpty()){
                        continue;
                    }

                    List<String> existRelationAttList = Lists.newArrayList();

                    //图谱中该属性的边属性不存在，删除订阅记录
                    for(DWGraphMapRelationAttr relationAttr : relationAttrList){
                        if(!relationAttrNames.contains(relationAttr.getName())){
                            if(graphMapRelationAttrRepository.existsById(relationAttr.getId())){
                                graphMapRelationAttrRepository.deleteById(relationAttr.getId());
                            }
                        }

                        if(existRelationAttList.contains(relationAttr.getName()+relationAttr.getModelId()+relationAttr.getModelAttrId())){
                            if(graphMapRelationAttrRepository.existsById(relationAttr.getId())) {
                                graphMapRelationAttrRepository.deleteById(relationAttr.getId());
                            }
                        }else{
                            existRelationAttList.add(relationAttr.getName()+relationAttr.getModelId()+relationAttr.getModelAttrId());
                        }
                    }

                }
            }
        }

    }

    @Override
    public List<JSONObject> listDatabase(String userId, String kgName) {

        List<Long> databaseIds = graphMapRepository.getDatabaseList(kgName);

        if(databaseIds == null || databaseIds.isEmpty()){
            return new ArrayList<>();
        }

        List<DWDatabase> databases = dwDatabaseRepository.findAllById(databaseIds);

        if(databases == null || databases.isEmpty()){
            return new ArrayList<>();
        }

        List<JSONObject> database = new ArrayList<>();
        databases.forEach(d -> {

            JSONObject data = new JSONObject();
            data.put("databaseName",d.getTitle());
            data.put("databaseId",d.getId());
            database.add(data);
        });

        return database;
    }

    @Override
    public void batchScheduleSwitch(List<Integer> ids, Integer status) {

        if(ids == null || ids.isEmpty()){
            return;
        }

        List<String> taskList = new ArrayList<>();
        for(Integer id : ids){

            Optional<DWGraphMap> graphMapOpt = graphMapRepository.findById(id);
            if (!graphMapOpt.isPresent()) {
                return;
            }

            DWGraphMap graphMap = graphMapOpt.get();

            if(taskList.contains(graphMap.getDataBaseId()+graphMap.getTableName())){
                continue;
            }else{
                taskList.add(graphMap.getDataBaseId()+graphMap.getTableName());
            }

            DWDatabaseRsp database = dwService.getDetail(graphMap.getDataBaseId());

            if(database == null){
                return ;
            }

            List<DWGraphMap> tabs = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMap.getKgName()).dataBaseId(graphMap.getDataBaseId()).tableName(graphMap.getTableName()).build()));

            if (tabs == null || tabs.isEmpty()) {
                return;
            }

            Optional<DWPrebuildModel> modelOptional = modelRepository.findById(graphMap.getModelId());
            if(!modelOptional.isPresent()){
                throw BizException.of(KgmsErrorCodeEnum.PRE_BUILD_MODEL_NOT_EXIST);
            }

            tabs.forEach(t -> t.setSchedulingSwitch(status));

            graphMapRepository.saveAll(tabs);

            String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + graphMap.getKgName() + "_" + graphMap.getModelId();
            if (status.equals(1)) {

                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 1,graphMap.getKgName());
                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), null, Lists.newArrayList(kgTaskName), null, null, graphMap.getKgName());
                } else {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName), null, null, null, graphMap.getKgName());
                }
            } else {
                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 0,graphMap.getKgName());

                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, null, Lists.newArrayList(kgTaskName), graphMap.getKgName());
                } else {
                    accessTaskService.createTransfer(true,graphMap.getModelId(),graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, Lists.newArrayList(kgTaskName), null, graphMap.getKgName());
                }
            }

            //更新订阅任务
            preBuilderService.createSchedulingConfig(graphMap.getKgName(), false,status);
        }
    }

    @Override
    public void batchDeleteSchedule(List<Integer> ids) {
        if(ids == null || ids.isEmpty()){
            return;
        }

        for(Integer id : ids){
            deleteSchedule(id);
        }
    }

    private void deleteConcept(List<DWGraphMap> graphMapList) {

        for(DWGraphMap graphMap : graphMapList){
            deleteConcept(graphMap);
        }
    }

    private void deleteConcept(DWGraphMap graphMap) {

        Integer attrId = graphMap.getAttrId();

        if(attrId != null){
            deleteRelationAttr(graphMap.getKgName(),attrId);
        }

        synchronized (this){
            if(graphMapRepository.existsById(graphMap.getId())){
                graphMapRepository.deleteById(graphMap.getId());
            }
        }
    }

    private void deleteRelationAttr(String kgName, Integer attrId) {

        List<DWGraphMapRelationAttr> relationAttrList = graphMapRelationAttrRepository.findAll(Example.of(DWGraphMapRelationAttr.builder().kgName(kgName).attrId(attrId).build()));
        if(relationAttrList != null && !relationAttrList.isEmpty()){
            relationAttrList.forEach(attr -> {
                synchronized (this) {
                    if (graphMapRelationAttrRepository.existsById(attr.getId())) {
                        graphMapRelationAttrRepository.deleteById(attr.getId());
                    }
                }
            });
        }

    }

    private List<GraphMapRsp> graphMap2Rsp(List<DWGraphMap> graphMapList) {

        if (graphMapList == null || graphMapList.isEmpty()) {
            return new ArrayList<>();
        }

        List<GraphMapRsp> rspList = new ArrayList<>(graphMapList.size());

        Map<Integer, String> modelMap = new HashMap<>();
        Map<Integer, String> attrMap = new HashMap<>();
        Map<Long, DWDatabaseRsp> databaseMap = new HashMap<>();
        Map<String, DWTable> tableMap = new HashMap<>();

        for (DWGraphMap graphMap : graphMapList) {

            GraphMapRsp rsp = ConvertUtils.convert(GraphMapRsp.class).apply(graphMap);

            if (!databaseMap.containsKey(graphMap.getDataBaseId())) {
                DWDatabaseRsp database = dwService.getDetail(graphMap.getDataBaseId());

                if (database == null) {
                    databaseMap.put(graphMap.getDataBaseId(), null);
                } else {
                    databaseMap.put(graphMap.getDataBaseId(), database);
                }


            }

            if(!tableMap.containsKey(graphMap.getTableName()+graphMap.getDataBaseId())){
                if(StringUtils.hasText(graphMap.getTableName())){
                    Optional<DWTable> table = tableRepository.findOne(Example.of(DWTable.builder().tableName(graphMap.getTableName()).dwDataBaseId(graphMap.getDataBaseId()).build()));
                    if(table.isPresent()){
                        tableMap.put(table.get().getTableName()+graphMap.getDataBaseId(),table.get());
                    }
                }
            }

            if (databaseMap.get(graphMap.getDataBaseId()) != null) {
                rsp.setDataBaseId(graphMap.getDataBaseId());
                rsp.setDatabaseName(databaseMap.get(graphMap.getDataBaseId()).getTitle());
                rsp.setDataName(databaseMap.get(graphMap.getDataBaseId()).getDataName());

                DWTable tab = tableMap.get(graphMap.getTableName()+graphMap.getDataBaseId());
                if(tab != null && StringUtils.hasText(tab.getTbName())){
                    rsp.setDbName(databaseMap.get(graphMap.getDataBaseId()).getDbName());
                }

            }


            if (!modelMap.containsKey(graphMap.getModelId())) {
                Optional<DWPrebuildModel> modelOpt = modelRepository.findById(graphMap.getModelId());
                if (modelOpt.isPresent()) {
                    modelMap.put(graphMap.getModelId(), modelOpt.get().getName());
                } else {
                    modelMap.put(graphMap.getModelId(), null);
                }
            }
            rsp.setModelName(modelMap.get(graphMap.getModelId()));

            if (graphMap.getModelAttrId() != null) {
                if (!attrMap.containsKey(graphMap.getModelAttrId())) {
                    Optional<DWPrebuildAttr> attrOpt = attrRepository.findById(graphMap.getModelAttrId());
                    if (attrOpt.isPresent()) {
                        attrMap.put(graphMap.getModelAttrId(), attrOpt.get().getName());
                    } else {
                        attrMap.put(graphMap.getModelAttrId(), null);
                    }
                }
                rsp.setModelAttrName(attrMap.get(graphMap.getModelAttrId()));
            }
            rspList.add(rsp);
        }

        return rspList;
    }

    private void getChildConceptMap(List<DWGraphMap> graphMapList, String kgName, Long databaseId, Long conceptId) {

        SchemaRsp types = graphApplicationService.querySchema(kgName);

        if(types.getTypes() ==  null || types.getTypes().isEmpty()){
            return;
        }

        List<BaseConceptRsp> conceptTree = Lists.newArrayList();
        getChildConcept(types.getTypes(),conceptTree,conceptId);

        for(BaseConceptRsp info : conceptTree){
            if(info.getId() == null || info.getId().equals(conceptId)){
                continue;
            }

            List<DWGraphMap> graphMaps = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).dataBaseId(databaseId).conceptId(info.getId()).build()));
            if (graphMaps == null || graphMaps.isEmpty()) {
                continue;
            }

            graphMapList.addAll(graphMaps);
        }

        return;
    }

    private void getChildConcept(List<BaseConceptRsp> types,List<BaseConceptRsp> childs, Long conceptId) {

        if(types == null || types.isEmpty()){
            return ;
        }

        for(BaseConceptRsp conceptRsp : types){
            if(conceptRsp.getParentId().equals(conceptId)){
                childs.add(conceptRsp);
                getChildConcept(types,childs,conceptRsp.getId());
            }
        }
    }
}
