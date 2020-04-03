package com.plantdata.kgcloud.domain.dw.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.dw.entity.*;
import com.plantdata.kgcloud.domain.dw.repository.*;
import com.plantdata.kgcloud.domain.dw.req.GraphMapReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.GraphMapService;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
    private DWService dwService;
    @Override
    public List<GraphMapRsp> list(String userId, GraphMapReq graphMapReq) {

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

        List<DWGraphMap> tabs = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(graphMap.getKgName()).tableName(graphMap.getTableName()).build()));

        if (tabs == null || tabs.isEmpty()) {
            return;
        }

        tabs.forEach(t -> t.setSchedulingSwitch(status));

        graphMapRepository.saveAll(tabs);

        String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + graphMap.getKgName() + "_" + graphMap.getModelId();
        if (status.equals(1)) {

            accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 1,graphMap.getKgName());
            if (database.getDataFormat().equals(1)) {
                accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), null, Lists.newArrayList(kgTaskName), null, null, graphMap.getKgName());
            } else {
                accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName), null, null, null, graphMap.getKgName());
            }
        } else {
            accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 0,graphMap.getKgName());

            if (database.getDataFormat().equals(1)) {
                accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, null, Lists.newArrayList(kgTaskName), graphMap.getKgName());
            } else {
                accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, Lists.newArrayList(kgTaskName), null, graphMap.getKgName());
            }
        }

        //更新订阅任务
        preBuilderService.createSchedulingConfig(graphMap.getKgName(), false,status);
    }

    @Override
    public void deleteSchedule(Integer id) {

        DWGraphMap graphMap = graphMapRepository.getOne(id);
        String kgName = graphMap.getKgName();
//        String kgTaskName = AccessTaskType.KG.getDisplayName()+"_"+graphMap.getKgName()+"_"+graphMap.getModelId();
//        accessTaskService.createTransfer(graphMap.getTableName(),graphMap.getDataBaseId(), Lists.newArrayList(),null,Lists.newArrayList(kgTaskName),null,graphMap.getKgName());


        graphMapRepository.deleteById(id);

        //更新订阅任务
        preBuilderService.createSchedulingConfig(kgName, false,0);
    }

    @Override
    public void scheduleSwitchByKgName(String kgName, Integer status) {
        List<DWGraphMap> graphMaps = graphMapRepository.findAll(Example.of(DWGraphMap.builder().kgName(kgName).build()));
        if (graphMaps == null || graphMaps.isEmpty()) {
            return;
        }

        graphMaps.forEach(t -> t.setSchedulingSwitch(status));

        graphMapRepository.saveAll(graphMaps);

        List<String> exists = Lists.newArrayList();
        for(DWGraphMap graphMap : graphMaps){

            if(exists.contains(graphMap.getDataBaseId()+graphMap.getTableName())){
                continue;
            }

            exists.add(graphMap.getDataBaseId()+graphMap.getTableName());
            DWDatabaseRsp database = dwService.getDetail(graphMap.getDataBaseId());

            if(database == null){
                continue;
            }

            String kgTaskName = AccessTaskType.KG.getDisplayName() + "_" + graphMap.getKgName() + "_" + graphMap.getModelId();
            if (status.equals(1)) {

                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 1,graphMap.getKgName());
                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), null, Lists.newArrayList(kgTaskName), null, null, graphMap.getKgName());
                } else {
                    accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(kgTaskName), null, null, null, graphMap.getKgName());
                }
            } else {
                accessTaskService.createKtrTask(graphMap.getTableName(), graphMap.getDataBaseId(), graphMap.getKgName(), 0,graphMap.getKgName());

                if (database.getDataFormat().equals(1)) {
                    accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, null, Lists.newArrayList(kgTaskName), graphMap.getKgName());
                } else {
                    accessTaskService.createTransfer(graphMap.getTableName(), graphMap.getDataBaseId(), Lists.newArrayList(), null, Lists.newArrayList(kgTaskName), null, graphMap.getKgName());
                }
            }

            //更新订阅任务
        }
        preBuilderService.createSchedulingConfig(kgName, false,status);

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
                DWPrebuildModel model = modelRepository.getOne(graphMap.getModelId());
                if (model == null) {
                    modelMap.put(graphMap.getModelId(), null);
                } else {
                    modelMap.put(graphMap.getModelId(), model.getName());
                }
            }
            rsp.setModelName(modelMap.get(graphMap.getModelId()));

            if (graphMap.getModelAttrId() != null) {
                if (!attrMap.containsKey(graphMap.getModelAttrId())) {
                    DWPrebuildAttr attr = attrRepository.getOne(graphMap.getModelAttrId());
                    if (attr == null) {
                        attrMap.put(graphMap.getModelAttrId(), null);
                    } else {
                        attrMap.put(graphMap.getModelAttrId(), attr.getName());
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
        if (graphMaps == null || graphMaps.isEmpty()) {
            return;
        }

        for (DWGraphMap graphMap : graphMaps) {
            graphMapList.add(graphMap);
            if (graphMap.getConceptId() == null) {
                continue;
            }
            getChildConceptMap(graphMapList, kgName, databaseId, graphMap.getConceptId());
        }

        return;
    }
}
