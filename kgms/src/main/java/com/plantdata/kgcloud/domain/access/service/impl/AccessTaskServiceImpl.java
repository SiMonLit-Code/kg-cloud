
package com.plantdata.kgcloud.domain.access.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.access.entity.DWTask;
import com.plantdata.kgcloud.domain.access.entity.EtlTask;
import com.plantdata.kgcloud.domain.access.entity.KgTask;
import com.plantdata.kgcloud.domain.access.repository.DWTaskRepository;
import com.plantdata.kgcloud.domain.access.repository.EtlTaskRepository;
import com.plantdata.kgcloud.domain.access.repository.KgTaskRepository;
import com.plantdata.kgcloud.domain.access.req.EtlConfigReq;
import com.plantdata.kgcloud.domain.access.req.IndexConfigReq;
import com.plantdata.kgcloud.domain.access.req.KgConfigReq;
import com.plantdata.kgcloud.domain.access.req.ResourceReq;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.access.util.CreateKtrFile;
import com.plantdata.kgcloud.domain.access.util.YamlTransFunc;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWGraphMap;
import com.plantdata.kgcloud.domain.dw.repository.DWGraphMapRepository;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccessTaskServiceImpl implements AccessTaskService {


    @Autowired
    private EtlTaskRepository etlTaskRepository;

    @Autowired
    private KgTaskRepository kgTaskRepository;

    @Autowired
    private DWService dwService;

    @Autowired
    private DWTaskRepository taskRepository;

    @Value("${kafka.servers}")
    private String kafkaServers;

    @Autowired
    private DWGraphMapRepository graphMapRepository;

    @Override
    public String saveEtlTask(String userId, EtlConfigReq req) {


        EtlTask etlTask = new EtlTask();
        BeanUtils.copyProperties(req,etlTask);

        if(req.getTaskId() != null){

            String taskId = UUID.randomUUID().toString().replaceAll("-","");
            etlTask.setTaskId(taskId);

        }else{

           Optional<EtlTask> opt = etlTaskRepository.findOne(Example.of(EtlTask.builder().taskId(req.getTaskId()).build()));

           if(opt.isPresent()){
               etlTask.setId(opt.get().getId());
           }
        }

        etlTaskRepository.save(etlTask);
        return etlTask.getTaskId();
    }

    @Override
    public String saveKgTask(String userId, KgConfigReq req) {
        KgTask kgTask = new KgTask();
        BeanUtils.copyProperties(req,kgTask);

        if(req.getTaskId() != null){

            String taskId = UUID.randomUUID().toString().replaceAll("-","");
            kgTask.setTaskId(taskId);

        }else{

            Optional<KgTask> opt = kgTaskRepository.findOne(Example.of(KgTask.builder().taskId(req.getTaskId()).build()));

            if(opt.isPresent()){
                kgTask.setId(opt.get().getId());
            }
        }

        kgTaskRepository.save(kgTask);
        return kgTask.getTaskId();
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

    private List<ResourceReq> transformConfig(DataAccessTaskConfigReq config,Map<String,String> taskId2TypeMap) {

        List<ResourceReq> list = new ArrayList<>();

        switch (config.getType()){
            case "etl":

                EtlConfigReq etlConfigReq = JacksonUtils.readValue(config.getConfig(), EtlConfigReq.class);

                DWDatabase database = dwService.getDetail(etlConfigReq.getDatabaseId());

                String yamlContent = database.getYamlContent();
                Map<String, JSONArray> yamlTagMap = YamlTransFunc.tranTagConfig(yamlContent);


                List<DWTableRsp> tables = dwService.findTableAll(SessionHolder.getUserId(),etlConfigReq.getDatabaseId());

                for(DWTableRsp table : tables){

                    String name;
                    if(table.getCreateType().equals(1)){
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
                    configJson.put("cron",table.getCron());
                    configJson.put("isAll",table.getIsAll() == null ? 1 : table.getIsAll());
                    configJson.put("isScheduled",1);

                      //生成ktr文件
                    String ktrTxt = CreateKtrFile.getKettleXmlPath(database, table,kafkaServers);
                    configJson.put("fileText",ktrTxt);

                    ResourceReq etlResourceReq = new ResourceReq();
                    etlResourceReq.setResourceName(config.getId()+"_"+table.getId());
                    etlResourceReq.setResourceType("ktr");
                    etlResourceReq.setConfig(configJson);
                    etlResourceReq.setOutputs(Lists.newArrayList(tranfResourceReq.getResourceName()));

                    list.add(etlResourceReq);
                    list.add(tranfResourceReq);
                    list.add(dwResourceReq);
                }

                break;
            case "kg":

                KgConfigReq kgConfigReq = JacksonUtils.readValue(config.getConfig(), KgConfigReq.class);

                DWGraphMap graphMap = graphMapRepository.getOne(kgConfigReq.getGraphMapId());

                JSONObject kgConfig = new JSONObject();
                kgConfig.put("kgName",graphMap.getKgName());
                kgConfig.put("dataMapping",graphMap.getMapJson());

                ResourceReq kgResourceReq = new ResourceReq();
                kgResourceReq.setResourceName(config.getId());
                kgResourceReq.setResourceType("kg");
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
                indexResourceReq.setResourceType("index");
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
