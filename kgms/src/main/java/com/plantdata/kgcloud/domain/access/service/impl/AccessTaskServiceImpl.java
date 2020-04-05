
package com.plantdata.kgcloud.domain.access.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.config.KafkaProperties;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.ChannelRedisEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.entity.DWTask;
import com.plantdata.kgcloud.domain.access.repository.DWTaskRepository;
import com.plantdata.kgcloud.domain.access.req.EtlConfigReq;
import com.plantdata.kgcloud.domain.access.req.IndexConfigReq;
import com.plantdata.kgcloud.domain.access.req.KgConfigReq;
import com.plantdata.kgcloud.domain.access.req.ResourceReq;
import com.plantdata.kgcloud.domain.access.rsp.ChannelRedisArrangeRsp;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.access.util.CreateKtrFile;
import com.plantdata.kgcloud.domain.access.util.YamlTransFunc;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWGraphMap;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWGraphMapRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildModelRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class AccessTaskServiceImpl implements AccessTaskService {

    @Autowired
    private DWService dwService;

    @Autowired
    private DWTaskRepository taskRepository;

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private DWGraphMapRepository graphMapRepository;

    @Autowired
    private DWTableRepository tableRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DWPrebuildModelRepository modelRepository;

    private static Map<String,String> cronMap = new HashMap<>();

    static {
        cronMap.put("每分钟","0 * * * * ? ");
        cronMap.put("每10分钟","0 0/10 * * * ? ");
        cronMap.put("每小时","0 0 * * * ? ");
        cronMap.put("每天","0 0 0 * * ? ");

    }


    @Override
    public Integer run(String userId,List<DataAccessTaskConfigReq> reqs,Integer taskId) {
        if(reqs == null){
            return null;
        }

        Map<String,String> taskId2TypeMap = new HashMap<>();

        for(DataAccessTaskConfigReq config : reqs){
            taskId2TypeMap.put(config.getId(),config.getType());
        }

        List<ResourceReq> resourceReqList = new ArrayList<>();
        for(DataAccessTaskConfigReq config : reqs){
            resourceReqList.addAll(transformConfig( config,taskId2TypeMap));
        }


        DWTask task = new DWTask();

        if(taskId != null){
            task.setId(taskId);
        }

        task.setConfig(JacksonUtils.writeValueAsString(resourceReqList));
        task.setName("数据接入任务");
        task.setUserId(SessionHolder.getUserId());
        task = taskRepository.save(task);

        return task.getId();
    }

    @Override
    public DWTaskRsp getTask(Integer id) {
        Optional<DWTask> task = taskRepository.findById(id);

        DWTaskRsp rsp = new DWTaskRsp();

        BeanUtils.copyProperties(task.get(),rsp);

        return rsp;
    }

    @Override
    public DWTaskRsp getByTaskName(String taskName) {
        Optional<DWTask> task = taskRepository.findOne(Example.of(DWTask.builder().name(taskName).build()));
        if(task.isPresent()){
            return ConvertUtils.convert(DWTaskRsp.class).apply(task.get());
        }
        return null;
    }

    @Override
    public void saveTask(DWTaskRsp taskRsp) {
        saveTask(taskRsp,null);
    }

    @Override
    public void saveTask(DWTaskRsp taskRsp,Long timeout) {
        taskRepository.save(ConvertUtils.convert(DWTask.class).apply(taskRsp));

        ChannelRedisArrangeRsp arrangeRsp = new ChannelRedisArrangeRsp();
        arrangeRsp.setResourceName(taskRsp.getName());
        arrangeRsp.setResourceType(taskRsp.getTaskType());
        arrangeRsp.setOutputs(taskRsp.getOutputs());
        arrangeRsp.setDistributeOriginalData(taskRsp.getDistributeOriginalData());
        arrangeRsp.setUpdateTime(System.currentTimeMillis());

        if(taskRsp.getTaskType().equals(AccessTaskType.KTR.getDisplayName())){

            JSONObject config = JSON.parseObject(taskRsp.getConfig());
            String ktrFile = config.getString("fileText");
            cacheManager.getCache(ChannelRedisEnum.KTR_KEY.getType()).put(taskRsp.getName(),ktrFile);


            config.remove("fileText");
            cacheManager.getCache(ChannelRedisEnum.KTR_CONFIG_KEY.getType()).put(taskRsp.getName(),config.toJSONString());
        }else{
            cacheManager.getCache(ChannelRedisEnum.CONFIG_KEY.getType()).put(taskRsp.getName(),taskRsp.getConfig());
        }

        cacheManager.getCache(ChannelRedisEnum.ARRANGE_KEY.getType()).put(taskRsp.getName(), JSON.parseObject(JSON.toJSONString(arrangeRsp)));

    }

    @Override
    public String getKtrConfig(Long databaseId, String tableName,String isAllKey,Integer isScheduled,String target) {

        DWDatabaseRsp database = dwService.getDetail(databaseId);

        if(database == null){
            return null;
        }

        DWTableRsp table = dwService.findTableByTableName(SessionHolder.getUserId(),databaseId,tableName);

        if(table == null){
            return null;
        }

        JSONObject configJson = new JSONObject();

        String taskKey= databaseId+"_"+tableName + "_"+isAllKey;

        String ktrTaskName = AccessTaskType.KTR.getDisplayName()+"_"+taskKey;
        String transferTaskName = AccessTaskType.TRANSFER.getDisplayName()+"_"+taskKey;

        //生成ktr文件
        String ktrTxt = CreateKtrFile.getKettleXmlPath(database, table,isAllKey,ktrTaskName,kafkaProperties.getServers(),mongoProperties.getAddrs(),mongoProperties.getUsername(),mongoProperties.getPassword(),SessionHolder.getUserId());
        configJson.put("fileText",ktrTxt);
        configJson.put("updateTime",System.currentTimeMillis());
        configJson.put("cron",cronMap.get(table.getCron()));
        configJson.put("isAll",table.getIsAll() == null ? 1 : table.getIsAll());

        configJson.put("resourceName",ktrTaskName);
        configJson.put("outputs",Lists.newArrayList(transferTaskName));
        configJson.put("isScheduled",isScheduled == null? 0: isScheduled);


        JSONObject resourceConfig = new JSONObject();
        resourceConfig.put("target",target);
        resourceConfig.put("dataName",database.getDataName());
        resourceConfig.put("dbTitle",database.getTitle());
        resourceConfig.put("tableName",tableName);
        resourceConfig.put("dbId",databaseId);
        resourceConfig.put("userId",SessionHolder.getUserId());
        configJson.put("resourceConfig_",resourceConfig);

        return configJson.toString();
    }

    @Override
    public String getDwConfig(Long databaseId, DWTable table,Integer isScheduled) {

        DWDatabaseRsp database = dwService.getDetail(databaseId);

        if(database == null){
            return null;
        }

        JSONObject dwJson = new JSONObject();

        JSONObject dwTbJson = new JSONObject();
        dwTbJson.put("tbName",table.getTableName());
        dwTbJson.put("dbName",database.getDataName());

        dwJson.put("dwType", "mongo");
        dwJson.put("dwConfig", dwTbJson);
        dwJson.put("isScheduled", isScheduled);
        return dwJson.toJSONString();
    }

    @Override
    public String getTransferConfig(Boolean isGraph, Integer modelId, Long databaseId, String tableName,Integer isScheduled) {

        DWDatabaseRsp database = dwService.getDetail(databaseId);
        if(database == null){
            return null;
        }

        JSONObject transferJson = new JSONObject();
        transferJson.put("isScheduled",isScheduled);

        //自定义
        if(database.getDataFormat().equals(3)){

            if(isGraph){
                Optional<DWPrebuildModel> modelOpt = modelRepository.findById(modelId);
                if(!modelOpt.isPresent()){
                    throw BizException.of(KgmsErrorCodeEnum.PRE_BUILD_MODEL_NOT_EXIST);
                }
                DWPrebuildModel model = modelOpt.get();
                String yamlContent = model.getYamlContent();
                if(yamlContent != null && !yamlContent.isEmpty()){
                    Map<String, JSONArray> yamlTagMap = YamlTransFunc.tranTagConfig(yamlContent);
                    JSONArray tableTransfer = yamlTagMap.get(tableName);
                    transferJson.put("transferConfig", tableTransfer);
                }
            }

            if(transferJson.containsKey("transferConfig") && transferJson.getJSONArray("transferConfig") != null && !transferJson.getJSONArray("transferConfig").isEmpty()){
                transferJson.put("transferType","d2r");
            }else{
                transferJson.put("transferType","");
                transferJson.put("transferConfig","");
            }
        }else if(database.getDataFormat().equals(1)){
            //行业标准
            transferJson.put("transferType","industry");
            transferJson.put("transferConfig","");
        }else if(database.getDataFormat().equals(2)){
            //pddoc
            transferJson.put("transferType","pddoc");
            transferJson.put("transferConfig","");
        }else{
            transferJson.put("transferType","");
            transferJson.put("transferConfig","");
        }

        return transferJson.toJSONString();
    }

    @Override
    public String createKtrTask(String tableName,Long databaseId,String isAllKey,Integer isSchedue,String target) {
        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().tableName(tableName).dwDataBaseId(databaseId).build()));
        if(!tableOpt.isPresent()){
            return null;
        }

        String taskKey= databaseId+"_"+tableName + "_" +isAllKey;

        Long timeout = 600L;

        String ktrTaskName = AccessTaskType.KTR.getDisplayName()+"_"+taskKey;
        String transferTaskName = AccessTaskType.TRANSFER.getDisplayName()+"_"+taskKey;

        DWTaskRsp ktrTaskRsp = getByTaskName(ktrTaskName);

        if(ktrTaskRsp == null){
            ktrTaskRsp = new DWTaskRsp();
            ktrTaskRsp.setTaskType(AccessTaskType.KTR.getDisplayName());
            ktrTaskRsp.setName(ktrTaskName);
            ktrTaskRsp.setOutputs(Lists.newArrayList(transferTaskName));
            ktrTaskRsp.setUserId(SessionHolder.getUserId());
        }

        ktrTaskRsp.setConfig(getKtrConfig(databaseId,tableName,isAllKey,isSchedue,target));

        ktrTaskRsp.setStatus(isSchedue == null? 0 : isSchedue);
        /*if(table.getCreateWay() == null || table.getCreateWay().equals(2) || table.getIsAll() == null ||table.getIsAll().equals(1)){

            //本地库或者全量按指定


        }else{

            //增量的调度跟表一起
            ktrTaskRsp.setStatus(table.getSchedulingSwitch());
        }*/

        saveTask(ktrTaskRsp,timeout);

        return ktrTaskName;
    }

    @Override
    public String createTransfer(Boolean isGraph,Integer modelId,String tableName, Long databaseId, List<String> outputs,List<String>distributeOriginalData,List<String> deleteOutputs,List<String> deleteDistributeOriginalData,String isAllKey) {

        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().tableName(tableName).dwDataBaseId(databaseId).build()));
        if(!tableOpt.isPresent()){
            return null;
        }

        String taskKey = databaseId+"_"+tableName + "_"+isAllKey;
        Long timeout = 600L;

        String transferTaskName = AccessTaskType.TRANSFER.getDisplayName()+"_"+taskKey;

        DWTaskRsp transferRsp = getByTaskName(transferTaskName);
        if(transferRsp == null){

            transferRsp = new DWTaskRsp();
            transferRsp.setTaskType(AccessTaskType.TRANSFER.getDisplayName());
            transferRsp.setName(transferTaskName);
            transferRsp.setUserId(SessionHolder.getUserId());
            transferRsp.setOutputs(new ArrayList<>());
            transferRsp.setDistributeOriginalData(new ArrayList<>());

        }
        transferRsp.setStatus(1);
        transferRsp.setConfig(getTransferConfig(isGraph,modelId, databaseId,tableName,1));
        List<String> outs = transferRsp.getOutputs();

        if(outputs != null && !outputs.isEmpty()){

            for(String output : outputs){
                if(!outs.contains(output)){
                    outs.add(output);
                }
            }
        }

        if(deleteOutputs != null && !deleteOutputs.isEmpty() && outputs != null){
            outputs.removeAll(deleteOutputs);
        }

        if(deleteDistributeOriginalData != null && !deleteDistributeOriginalData.isEmpty() && distributeOriginalData != null){
            distributeOriginalData.removeAll(deleteDistributeOriginalData);
        }

        List<String> distributeOriginals = transferRsp.getDistributeOriginalData();

        if(distributeOriginalData != null && !distributeOriginalData.isEmpty()){

            for(String dis : distributeOriginalData){
                if(!distributeOriginals.contains(dis)){
                    distributeOriginals.add(dis);
                }
            }
        }

        saveTask(transferRsp,timeout);

        return transferTaskName;
    }

    @Override
    public String createDwTask(String tableName,Long databaseId) {

        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().tableName(tableName).dwDataBaseId(databaseId).build()));
        if(!tableOpt.isPresent()){
            return null;
        }

        DWTable table = tableOpt.get();

        String dwTaskName = AccessTaskType.DW.getDisplayName()+"_"+databaseId+"_"+tableName;

        DWTaskRsp dwRsp = getByTaskName(dwTaskName);
        if(dwRsp == null){
            dwRsp = new DWTaskRsp();
            dwRsp.setTaskType(AccessTaskType.DW.getDisplayName());
            dwRsp.setName(dwTaskName);
            dwRsp.setOutputs(new ArrayList<>());

        }
        dwRsp.setConfig(getDwConfig(databaseId,table,table.getSchedulingSwitch()));
        dwRsp.setStatus(table.getSchedulingSwitch());

        saveTask(dwRsp);
        return dwTaskName;
    }

    @Override
    public void updateTableSchedulingConfig(DWDatabaseRsp database, DWTableRsp table, String ktrTaskName, String cron, Integer isAll, String field) {

        Specification<DWTask> specification = new Specification<DWTask>() {
            @Override
            public Predicate toPredicate(Root<DWTask> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), ktrTaskName + "%");
                predicates.add(likename);

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        List<DWTask> all = taskRepository.findAll(specification);

        if(all == null || all.isEmpty()){
            return;
        }

        for(DWTask task : all){

            DWTaskRsp taskRsp = ConvertUtils.convert(DWTaskRsp.class).apply(task);

            String resourceTerget= taskRsp.getName().replaceFirst(ktrTaskName,"");

            JSONObject configJson = JSON.parseObject(taskRsp.getConfig());
            String ktrTxt = CreateKtrFile.getKettleXmlPath(database, table,resourceTerget,taskRsp.getName(),kafkaProperties.getServers(),mongoProperties.getAddrs(),mongoProperties.getUsername(),mongoProperties.getPassword(),SessionHolder.getUserId());
            configJson.put("fileText",ktrTxt);
            configJson.put("updateTime",System.currentTimeMillis());
            configJson.put("cron",cronMap.get(table.getCron()));
            configJson.put("isAll",table.getIsAll() == null ? 1 : table.getIsAll());

            taskRsp.setConfig(configJson.toJSONString());

            saveTask(taskRsp);

        }

    }

    private List<ResourceReq> transformConfig(DataAccessTaskConfigReq config,Map<String,String> taskId2TypeMap) {

        List<ResourceReq> list = new ArrayList<>();

        switch (config.getType()){
            case "etl":

                EtlConfigReq etlConfigReq = JacksonUtils.readValue(config.getConfig(), EtlConfigReq.class);

                DWDatabaseRsp database = dwService.getDetail(etlConfigReq.getDatabaseId());

                String yamlContent = database.getYamlContent();
                Map<String, JSONArray> yamlTagMap = YamlTransFunc.tranTagConfig(yamlContent);


                List<DWTableRsp> tables = dwService.findTableAll(SessionHolder.getUserId(),etlConfigReq.getDatabaseId());

                for(DWTableRsp table : tables){

                    String name;
                    if(table.getCreateWay().equals(1)){
                        name = table.getTbName();
                    }else{
                        name = table.getTableName();
                    }

                    //数仓有打标文件，生成打标任务
                    ResourceReq tranfResourceReq = new ResourceReq();
                    tranfResourceReq.setResourceName(config.getId()+"_transfer"+ UUIDUtils.getShortString().substring(0,5));
                    tranfResourceReq.setResourceType("transfer");

                    List<String> nextTasks = new ArrayList<>();
                    List<String> saveOriginalData = new ArrayList<>();
                    if(config.getOutputs() != null){
                        for(String output : config.getOutputs()){
                            if("kg".equals(taskId2TypeMap.get(output))){
                                nextTasks.add(output);
                            }else{
                                saveOriginalData.add(output);
                            }
                        }
                    }

                    ResourceReq dwResourceReq = new ResourceReq();
                    dwResourceReq.setResourceName(config.getId()+"_dw_"+table.getId());
                    dwResourceReq.setResourceType("dw");

                    JSONObject dwConfig = new JSONObject();
                    dwConfig.put("dbName",database.getDataName());
                    dwConfig.put("tbName", table.getTableName());

                    JSONObject dwJson = new JSONObject();
                    dwJson.put("dwType","mongo");
                    dwJson.put("dwConfig",dwConfig);

                    dwResourceReq.setConfig(dwJson);
                    dwResourceReq.setOutputs(Lists.newArrayList());
                    saveOriginalData.add(dwResourceReq.getResourceName());



                    JSONObject transferJson = new JSONObject();
                    transferJson.put("saveOriginalData",saveOriginalData);
                    transferJson.put("transferType","d2r");
                    transferJson.put("transferConfig", yamlTagMap.get(name));
                    tranfResourceReq.setConfig(transferJson);

                    //更新下一个任务
                    tranfResourceReq.setOutputs(nextTasks);


                    JSONObject configJson = new JSONObject();
                    configJson.put("cron",cronMap.get(table.getCron()));
                    configJson.put("isAll",table.getIsAll() == null ? 1 : table.getIsAll());
                    configJson.put("isScheduled",1);

                      //生成ktr文件
                    String ktrTxt = CreateKtrFile.getKettleXmlPath(database, table,null,config.getId()+"_"+table.getId(),kafkaProperties.getServers(),mongoProperties.getAddrs(),mongoProperties.getUsername(),mongoProperties.getPassword(),SessionHolder.getUserId());
                    configJson.put("fileText",ktrTxt);

                    ResourceReq etlResourceReq = new ResourceReq();
                    etlResourceReq.setResourceName(config.getId()+"_"+table.getId());
                    etlResourceReq.setResourceType(AccessTaskType.KTR.getDisplayName());
                    etlResourceReq.setConfig(configJson);
                    etlResourceReq.setOutputs(Lists.newArrayList(tranfResourceReq.getResourceName()));

                    list.add(etlResourceReq);
                    list.add(tranfResourceReq);
                    list.add(dwResourceReq);
                }

                break;
            case "kg":

                KgConfigReq kgConfigReq = JacksonUtils.readValue(config.getConfig(), KgConfigReq.class);

                Optional<DWGraphMap> graphMapOpt = graphMapRepository.findById(kgConfigReq.getGraphMapId());

                if(!graphMapOpt.isPresent()){
                    break;
                }
                DWGraphMap graphMap = graphMapOpt.get();


                JSONObject kgConfig = new JSONObject();
                kgConfig.put("kgName",graphMap.getKgName());

                ResourceReq kgResourceReq = new ResourceReq();
                kgResourceReq.setResourceName(config.getId());
                kgResourceReq.setResourceType(AccessTaskType.KG.getDisplayName());
                kgResourceReq.setConfig(kgConfig);
                kgResourceReq.setOutputs(config.getOutputs() == null? new ArrayList<>():config.getOutputs());

                list.add(kgResourceReq);
                break;

            case "index":

                IndexConfigReq indexConfigReq = JacksonUtils.readValue(config.getConfig(), IndexConfigReq.class);

                JSONObject indexConfig = new JSONObject();
                indexConfig.put("mapping",indexConfigReq.getSetting());

                JSONObject esConfig = new JSONObject();
                esConfig.put("index",indexConfigReq.getIndex());
                esConfig.put("type",indexConfigReq.getType());
                indexConfig.put("esConfig",esConfig);

                ResourceReq indexResourceReq = new ResourceReq();
                indexResourceReq.setResourceName(config.getId());
                indexResourceReq.setResourceType(AccessTaskType.SEARCH.getDisplayName());
                indexResourceReq.setConfig(indexConfig);
                indexResourceReq.setOutputs(config.getOutputs() == null? new ArrayList<>():config.getOutputs());

                list.add(indexResourceReq);
                break;
            default:
                break;
        }

        return list;
    }

}
