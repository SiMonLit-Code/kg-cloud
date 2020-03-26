package com.plantdata.kgcloud.domain.dw.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWGraphMap;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildAttr;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWGraphMapRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildAttrRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildModelRepository;
import com.plantdata.kgcloud.domain.dw.req.GraphMapReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.service.GraphMapService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DWDatabaseRepository databaseRepository;

    @Autowired
    private DWPrebuildAttrRepository attrRepository;

    @Autowired
    private DWPrebuildModelRepository modelRepository;

    @Autowired
    private AccessTaskService accessTaskService;

    @Override
    public List<GraphMapRsp> list(String userId, GraphMapReq graphMapReq) {

        List<DWGraphMap> graphMapList;

        if(graphMapReq.getAttrId() != null){

            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).attrId(graphMapReq.getAttrId()).build()));

        }else{
            graphMapList = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMapReq.getKgName()).dataBaseId(graphMapReq.getDatabaseId()).conceptId(graphMapReq.getConceptId()).build()));

            if(graphMapList != null && !graphMapList.isEmpty()){

                List<DWGraphMap> childGraphMaps = new ArrayList<>();
                for(DWGraphMap graphMap : graphMapList){
                    getChildConceptMap(childGraphMaps,graphMapReq.getKgName(),graphMapReq.getDatabaseId(),graphMap.getConceptId());
                }

                graphMapList.addAll(childGraphMaps);
            }

        }

        List<GraphMapRsp> rspList = graphMap2Rsp(graphMapList);

        return rspList;
    }

    @Override
    public void scheduleSwitch(Integer id, Integer status) {

        DWGraphMap graphMap = graphMapRepository.getOne(id);
        if(graphMap == null ){
            return ;
        }

        DWDatabase database = databaseRepository.getOne(graphMap.getDataBaseId());

        List<DWGraphMap> tabs = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMap.getKgName()).tableName(graphMap.getTableName()).build()));

        if(tabs == null || tabs.isEmpty()){
            return ;
        }

        tabs.forEach(t -> t.setSchedulingSwitch(status));

        graphMapRepository.saveAll(tabs);

        String kgTaskName = AccessTaskType.KG.getDisplayName()+"_"+graphMap.getKgName()+"_"+graphMap.getModelId();
        if(status.equals(1)){

            accessTaskService.createKtrTask(graphMap.getTableName(),graphMap.getDataBaseId(),graphMap.getKgName(),1);
            if(database.getDataFormat().equals(1)){
                accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), null,Lists.newArrayList(kgTaskName),null,null,graphMap.getKgName());
            }else{
                accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName),null,null,null,graphMap.getKgName());
            }
        }else{
            accessTaskService.createKtrTask(graphMap.getTableName(),graphMap.getDataBaseId(),graphMap.getKgName(),0);

            if(database.getDataFormat().equals(1)){
                accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), Lists.newArrayList(),null, null,Lists.newArrayList(kgTaskName),graphMap.getKgName());
            }else{
                accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), Lists.newArrayList(),null, Lists.newArrayList(kgTaskName),null,graphMap.getKgName());
            }
        }
    }

    @Override
    public void deleteSchedule(Integer id) {

        DWGraphMap graphMap = graphMapRepository.getOne(id);
        String kgTaskName = AccessTaskType.KG.getDisplayName()+"_"+graphMap.getKgName()+"_"+graphMap.getModelId();
        accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), Lists.newArrayList(),null,Lists.newArrayList(kgTaskName),null,graphMap.getKgName());


        graphMapRepository.deleteById(id);
    }

    private List<GraphMapRsp> graphMap2Rsp(List<DWGraphMap> graphMapList) {

        if(graphMapList == null || graphMapList.isEmpty()){
            return new ArrayList<>();
        }

        List<GraphMapRsp> rspList = new ArrayList<>(graphMapList.size());

        Map<Integer,String> modelMap = new HashMap<>();
        Map<Integer,String> attrMap = new HashMap<>();
        Map<Long,String> databaseMap = new HashMap<>();

        for(DWGraphMap graphMap : graphMapList){

            GraphMapRsp rsp = ConvertUtils.convert(GraphMapRsp.class).apply(graphMap);

            if(!databaseMap.containsKey(graphMap.getDataBaseId())){
                DWDatabase database = databaseRepository.getOne(graphMap.getDataBaseId());
                if(database == null){
                    databaseMap.put(graphMap.getDataBaseId(),null);
                }else{
                    databaseMap.put(graphMap.getDataBaseId(),database.getTitle());
                }
            }
            rsp.setDatabaseName(databaseMap.get(graphMap.getDataBaseId()));


            if(!modelMap.containsKey(graphMap.getModelId())){
                DWPrebuildModel model = modelRepository.getOne(graphMap.getModelId());
                if(model == null){
                    modelMap.put(graphMap.getModelId(),null);
                }else{
                    modelMap.put(graphMap.getModelId(),model.getName());
                }
            }
            rsp.setModelName(modelMap.get(graphMap.getModelId()));

            if(graphMap.getModelAttrId() != null){
                if(!attrMap.containsKey(graphMap.getModelAttrId())){
                    DWPrebuildAttr attr = attrRepository.getOne(graphMap.getModelAttrId());
                    if(attr == null){
                        attrMap.put(graphMap.getModelAttrId(),null);
                    }else{
                        attrMap.put(graphMap.getModelAttrId(),attr.getName());
                    }
                }
                rsp.setModelAttrName(attrMap.get(graphMap.getModelAttrId()));
            }
            rspList.add(rsp);
        }

        return rspList;
    }

    private void getChildConceptMap(List<DWGraphMap> graphMapList, String kgName, Long databaseId, Long conceptId) {

        List<DWGraphMap> graphMaps = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).dataBaseId(databaseId).pConceptId(conceptId).build()));
        if(graphMaps == null || graphMaps.isEmpty()){
            return ;
        }

        for(DWGraphMap graphMap : graphMaps){
            graphMapList.add(graphMap);
            if(graphMap.getConceptId() == null){
                continue;
            }
            getChildConceptMap(graphMapList,kgName,databaseId,graphMap.getConceptId());
        }

        return;
    }
}
