package com.plantdata.kgcloud.domain.app.service.impl;

import cn.hiboot.mcn.core.exception.ServiceException;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hiekn.data.storage.DataDriver;
import com.hiekn.data.storage.DefaultDataDriverImpl;
import com.mongodb.client.MongoDatabase;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 13:55
 */
public class WrapperServiceImpl {

//    @Override
//    public RestResp tagging(String kgName, Integer dataSetId, String fieldsAndWeights, String targetConcepts, String traceConfig, String algorithms) {
//
//        List<EntityLinkingResultBean> resultList = new ArrayList<>();
//        DataDriver dataDriver = null;
//        List<Map<String, Object>> dataList;
//
//        try {
//            dataDriver = new DefaultDataDriverImpl(dataSetId);
//            dataList = dataDriver.read(1, 10);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw ServiceException.newInstance(57022);
//        } finally {
//            if (dataDriver != null)
//                dataDriver.close();
//        }
//        Gson gson = new Gson();
//        Map<String, Double> filedWeightMap = JacksonUtils.readValue(fieldsAndWeights, new TypeReference() {
//        });
//        Set<Map.Entry<String, Double>> entries = filedWeightMap.entrySet();
//
//        for (Map.Entry<String, Double> one : entries) {
//            Double values = one.getValue();
//            if (values == null || values < 0 || values > 1) {
//                throw ServiceException.newInstance(ErrorCodes.WEIGHT_ERROR);
//            }
//        }
//
//        List<Long> conceptIdList = null;
//        if (targetConcepts != null && !targetConcepts.equals("")) {
//            conceptIdList = new ArrayList<>();
//            JsonArray configJsonArray = gson.fromJson(targetConcepts, JsonArray.class);
//
//            for (int i = 0; i < configJsonArray.size(); i++) {
//                conceptIdList.add(configJsonArray.get(i).getAsJsonObject().get("conceptId").getAsLong());
//            }
//        }
//
//
//        Set<Long> toReadNameIdSet = new HashSet<>();
//
//
//        Map<Long, double[]> scoreAndClassIdMap;
//        Map<Long, Double[]> classIdAndScore;
//
//        List<String> list = JsonUtils.readToList(algorithms, String.class);
//        List<String> strings = Lists.newArrayList("1", "2", "3");
//        for (String s : list) {
//            if (!strings.contains(s)) {
//                throw BizException.of(AppErrorCodeEnum.ALGORITHM_PARAM_ERROR);
//            }
//        }
//
//        if (algorithms.contains("1")) {
//            for (Map<String, Object> record : dataList) {
//                StringBuilder builder = new StringBuilder();
//                for (Map.Entry<String, Double> entry : filedWeightMap.entrySet()) {
//                    //这里的entry.getkey()应该是关键词。
//                    if (record.containsKey(entry.getKey())) {
//                        builder.append(record.get(entry.getKey()).toString());
//                    }
//                }
//                //todo 查询scoreAndClassIdMap
//                for (Map.Entry<Long, double[]> entry : scoreAndClassIdMap.entrySet()) {
//                    EntityLinkingResultBean bean = new EntityLinkingResultBean();
//                    bean.setDocumentId(record.get("_id").toString());
//                    bean.setDocument(gson.toJson(record));
//                    bean.setEntityId(entry.getKey());
//                    toReadNameIdSet.add(entry.getKey());
//                    bean.setScore(entry.getValue()[0]);
//                    bean.setAlgorithm("1");
//                    resultList.add(bean);
//                }
//                resultList.addAll(linkingUtil.simpleEntityLinkingByMemory(kgName, builder.toString(), conceptIdList));
//            }
//        }
//        if (algorithms.contains("2")) {
//            CalculateIdfUtil calculateIdfUtil = new CalculateIdfUtil(new ConfigBean(dbKgms, user, password));
//            calculateIdfUtil.idf(kgName, dataSetId, fieldsAndWeights, conceptIdList);
//            for (Map<String, Object> record : dataList) {
//                DataWeightUtil dataWeightUtil = new DataWeightUtil();
//                List<WeightBean> dataContent = dataWeightUtil.dateConfig(record, fieldsAndWeights);
//                classIdAndScore = linkingUtil.tfIdfEntityLinkingByDb(kgName, dataContent, conceptIdList, "entity");
//                for (Map.Entry<Long, Double[]> entry : classIdAndScore.entrySet()) {
//                    EntityLinkingResultBean bean = new EntityLinkingResultBean();
//                    bean.setEntityId(entry.getKey());
//                    toReadNameIdSet.add(entry.getKey());
//                    bean.setScore(entry.getValue()[0]);
//                    bean.setAlgorithm("2");
//                    bean.setType(1);
//                    resultList.add(bean);
//                }
//            }
//        }
//        Map<Long, SimpleEntityItem> seiMap = new KGCacheUtil().getNameAndMeaningOfIds(kgName, toReadNameIdSet);
//
//        for (EntityLinkingResultBean bean : resultList) {
//            if (seiMap.containsKey(bean.getEntityId())) {
//                bean.setEntityName(seiMap.get(bean.getEntityId()).getName());
//            }
//        }
//        return new RestResp<>(resultList);
//    }
}
