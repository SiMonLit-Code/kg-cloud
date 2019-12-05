package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.TaskGraphService;
import org.springframework.stereotype.Service;

/**
 * @author xiezhenxiang 2019/8/16
 */
@Service
public class TaskGraphServiceImpl implements TaskGraphService {

//    @Resource
//    private ConfigManage configManage;
//    @Resource
//    private JerseyHttp jerseyHttp;
//    @Resource
//    private UserSetMapper userSetMapper;
//    @Resource
//    private DataSetService myDataService;
//    @Resource
//    private MyDataMapper myDataMapper;
//
//    @Autowired
//    private QATemplateMapper qaTemplateMapper;
//
//    @Override
//    public RestResp searchStatus(String userId, String kgName) {
//
//        Integer taskId = -1;
//
//        UserSetBean userSet = userSetMapper.selectByKgName(userId, kgName);
//
//        if (userSet != null) {
//
//            String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/get?userId=" + userId + "&id=" + userSet.getTaskId();
//            String rs = jerseyHttp.sendGet(url, null, new MultivaluedHashMap<>());
//
//            if ("0".equals(JSONPath.read(rs, "ErrorCode").toString()) && !taskIsSchedule(userId, taskId)) {
//                taskId = userSet.getTaskId();
//            }
//        }
//
//        return new RestResp<>(taskId);
//    }
//
//    @Override
//    public Integer openSearch(String userId, String kgName) {
//
//        UserSetBean userSet = userSetMapper.selectByKgName(userId, kgName);
//        Integer taskId;
//
//        if (userSet != null) {
//
//            taskId = userSet.getTaskId();
//            String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/get?userId=" + userId + "&id=" + userSet.getTaskId();
//            String rs = jerseyHttp.sendGet(url, null, new MultivaluedHashMap<>());
//
//            // task not exists
//            if (!"0".equals(JSONPath.read(rs, "ErrorCode").toString())) {
//
//                taskId = createSearchTask(userId, kgName);
//            }
//        } else {
//            taskId = createSearchTask(userId, kgName);
//        }
//
//        // no schedule
//        if (!taskIsSchedule(userId, taskId)) {
//            scheduleTask(taskId, userId, "0 0 0 ? * *");
//        }
//
//        return taskId;
//    }
//
//    /**
//     * 任务是否在调度中
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private boolean taskIsSchedule(String userId, Integer taskId) {
//
//        String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/get?userId=" + userId + "&id=" + taskId;
//        String rs = jerseyHttp.sendGet(url, null, new MultivaluedHashMap<>());
//        Object scheduleStatus = JSONPath.read(rs, "data.status");
//
//        return "NORMAL".equals(scheduleStatus == null ? "" : scheduleStatus.toString());
//    }
//
//    /**
//     * 单词执行调度任务
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private void runTask(Integer taskId, String userId) {
//
//        String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/run";
//        MultivaluedHashMap<String, Object> para = new MultivaluedHashMap<>();
//        para.putSingle("id", taskId);
//        para.putSingle("userId", userId);
//        jerseyHttp.sendPost(url, null, para);
//    }
//
//    /**
//     * 调度任务
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private void scheduleTask(Integer taskId, String userId, String cronExp) {
//
//        String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/schedule";
//        MultivaluedHashMap<String, Object> para = new MultivaluedHashMap<>();
//        para.putSingle("id", taskId);
//        para.putSingle("userId", userId);
//        para.putSingle("cronExp", cronExp);
//
//        jerseyHttp.sendPost(url, null, para);
//    }
//
//    /**
//     * 停止调度
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private void stopSchedule(Integer taskId, String userId) {
//
//        String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/unschedule";
//        MultivaluedHashMap<String, Object> para = new MultivaluedHashMap<>();
//        para.putSingle("id", taskId);
//        para.putSingle("userId", userId);
//        jerseyHttp.sendPost(url, null, para);
//    }
//
//    /**
//     * 删除任务
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private void dropTask(Integer taskId, String userId) {
//
//        String url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/delete";
//        MultivaluedHashMap<String, Object> para = new MultivaluedHashMap<>();
//        para.putSingle("id", taskId);
//        para.putSingle("userId", userId);
//        jerseyHttp.sendPost(url, null, para);
//    }
//
//    /**
//     * 创建智能提示任务
//     *
//     * @author xiezhenxiang 2019/8/16
//     **/
//    private Integer createSearchTask(String userId, String kgName) {
//
//        dropMyDataPinYin(userId);
//
//        String url = configManage.getKgServicePublicPath() + "/kg01/concept/all/" + kgName + "?readMetaData=0";
//        String rs = jerseyHttp.sendGet(url, null, new MultivaluedHashMap<>());
//        List<Long> conceptIds = JSONArray.parseArray(JSONPath.read(rs, "$.data.id").toString(), Long.class);
//        conceptIds.remove(0L);
//
//        JSONObject config = new JSONObject()
//                .fluentPut("title", "拼音检索导出_" + System.currentTimeMillis())
//                .fluentPut("dataName", "pinyin_" + System.currentTimeMillis())
//                .fluentPut("kgName", kgName)
//                .fluentPut("type", 1)
//                .fluentPut("conceptIds", conceptIds);
//
//        JSONObject task = new JSONObject()
//                .fluentPut("kgName", kgName)
//                .fluentPut("name", "拼音检索导出_" + System.currentTimeMillis())
//                .fluentPut("taskType", "kg_export")
//                .fluentPut("userId", userId)
//                .fluentPut("config", config.toJSONString());
//
//        url = configManage.getXxlJobAdminUrl() + "/schedule/sdk/add?userId=" + userId;
//        MultivaluedHashMap<String, Object> formPara = new MultivaluedHashMap<>();
//        formPara.putSingle("task", task.toJSONString());
//
//        rs = jerseyHttp.sendPost(url, null, formPara);
//        Integer taskId = Integer.parseInt(JSONPath.read(rs, "data").toString());
//
//        UserSetBean userSetBean = new UserSetBean();
//        userSetBean.setUserId(userId);
//        userSetBean.setTaskId(taskId);
//        userSetBean.setKgName(kgName);
//        userSetMapper.delete(userId, kgName);
//        userSetMapper.insertSelective(userSetBean);
//
//        runTask(taskId, userId);
//        return taskId;
//    }
//
//    @Override
//    public void closeSearch(String userId, String kgName) {
//
//        UserSetBean userSet = userSetMapper.selectByKgName(userId, kgName);
//
//        if (userSet != null) {
//
//            userSetMapper.deleteByPrimaryKey(userSet.getId());
//            stopSchedule(userSet.getTaskId(), userId);
//            dropTask(userSet.getTaskId(), userId);
//
//            dropMyDataPinYin(userId);
//        }
//    }
//
//    private void dropMyDataPinYin(String userId) {
//
//        List<MyDataBean> ls = myDataMapper.getPinpinDm(userId);
//        if (ls != null) {
//
//            ls.forEach(s -> {
//                myDataService.delPrivateData(s.getId(), userId);
//            });
//        }
//    }
//
//
//    @Override
//    public RestResp flushSearch(String userId, String kgName) {
//
//        UserSetBean userSet = userSetMapper.selectByKgName(userId, kgName);
//
//        if (userSet != null) {
//            runTask(userSet.getTaskId(), userId);
//            return new RestResp<>(userSet);
//        } else {
//            return new RestResp<>(51023,"任务不存在");
//        }
//    }
//
//    @Override
//    public RestResp findQATemplate(String kgName) {
//        Gson gson = new Gson();
//        List<QATemplateBean> all = qaTemplateMapper.find(kgName);
//        List<QATemplateVo> list = new ArrayList<>();
//        for (QATemplateBean qaTemplateBean : all) {
//            QATemplateVo qaTemplateVo = new QATemplateVo();
//            BeanUtils.copyProperties(qaTemplateBean, qaTemplateVo);
//            List<Long> o = gson.fromJson(qaTemplateBean.getConceptIds(), new TypeToken<List<Long>>() {
//            }.getType());
//            qaTemplateVo.setConceptIds(o);
//            list.add(qaTemplateVo);
//        }
//        return new RestResp<>(new RestData<>(list, all.size()));
//    }
//
//    @Override
//    public RestResp saveQATemplate(String kgName, List<QATemplateVo> qaTemplate) {
//        Gson gson = new Gson();
//        List<QATemplateBean> v = new ArrayList<>();
//        qaTemplateMapper.delete(kgName);
//        for (QATemplateVo qaTemplateVo : qaTemplate) {
//            QATemplateBean qaTemplateBean = new QATemplateBean();
//            String question = qaTemplateVo.getQuestion();
//            int index = question.indexOf("$entity");
//            int count = 0;
//            while (index >= 0) {
//                index = question.indexOf("$entity", index + 1);
//                count++;
//            }
//            qaTemplateVo.setCount(count);
//            BeanUtils.copyProperties(qaTemplateVo, qaTemplateBean);
//            String s = gson.toJson(qaTemplateVo.getConceptIds());
//            qaTemplateBean.setConceptIds(s);
//            qaTemplateBean.setKgName(kgName);
//            qaTemplateBean.setUpdateTime(new Date());
//            qaTemplateBean.setCreateTime(new Date());
//            qaTemplateMapper.insert(qaTemplateBean);
//
//            v.add(qaTemplateBean);
//        }
//
//        return new RestResp<>(v);
//    }


}
