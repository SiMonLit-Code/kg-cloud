package com.plantdata.kgcloud.domain.pdd.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.pdd.service.PddService;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiezhenxiang 2019/12/10
 */
@Service
public class PddServiceImpl implements PddService {

    @Autowired
    private KgmsClient kgmsClient;

    @Override
    public Map getTag(Integer dmId) {

        DataSetRsp dataSetRsp = kgmsClient.dataSetFindById(Long.valueOf(dmId)).getData();
        // 实体标签
        Set<String> entityTag = Sets.newHashSet();
        // 数值属性标签
        Set<String> basicAttrTag = Sets.newHashSet();
        // 关系属性标签
        Set<String> objAttrTag = Sets.newHashSet();
        // 事件类型
        Set<String> eventType = Sets.newHashSet();
        // 事件元素
        Map<String, List<String>> eventElement = new HashMap<>(8);

        int taskId = Integer.parseInt(dataSetRsp.getDataName().substring(0, dataSetRsp.getDataName().indexOf("_")));
        JSONObject schema = getSchema(taskId);
        JSONArray entity = schema.getJSONArray("entity");
        entityTag.addAll(entity.toJavaList(String.class));

        JSONArray relation = schema.getJSONArray("relation");
        List<String> objAttrs = relation.toJavaList(JSONObject.class).stream().map(s -> s.getString("name")).collect(Collectors.toList());
        objAttrTag.addAll(objAttrs);

        JSONArray attr = schema.getJSONArray("attr");
        List<String> attrNames = attr.toJavaList(JSONObject.class).stream().map(s -> s.getString("name")).collect(Collectors.toList());
        basicAttrTag.addAll(attrNames);

        List<JSONObject> events = getEvents(taskId);
        for (JSONObject obj : events) {

            if (obj.get("element") == null) {
                continue;
            }
            String type = obj.getString("type");
            eventType.add(type);
            String element = obj.getString("element");
            List<String> elementLs = JSONArray.parseArray(element, String.class);
            if (!elementLs.isEmpty()) {
                elementLs.add("触发词");
            }
            eventElement.put(type, elementLs);
        }

        Map<String, Object> rs = new HashMap<>(6);
        rs.put("entityTag", entityTag);
        rs.put("basicAttrTag", basicAttrTag);
        rs.put("objAttrTag", objAttrTag);
        rs.put("eventType", eventType);
        rs.put("eventElement", eventElement);
        return rs;
    }

    private JSONObject getSchema(int taskId) {

        /*String url = appConfig.kgTextPath + "/plantdata-text/api/schema/get?taskId=" + taskId;
            String str = HttpUtil.sendGet(url);
            if (str.isEmpty()) {
                throw ThirdPartyException.newInstance();
            }

            Object schema = JSONPath.read(str, "$.data.schema");
            if (schema == null) {
                throw ServiceException.newInstance(50036, "未找到语料集的schema！");
            }

            return JSONObject.parseObject(schema.toString());*/

        return null;
    }

    private List<JSONObject> getEvents(Integer taskId) {

        List<JSONObject> ls = new ArrayList<>();

        /*String url = appConfig.kgTextPath + "/plantdata-text/api/event/get/list?pageNo=1&pageSize=100&taskId=" + taskId;
        String rs = HttpUtil.sendGet(url);

        if (!rs.isEmpty()) {
            Object rsData = JSONPath.read(rs, "$.data.rsData");
            if (rsData != null) {
                ls = (List<JSONObject>) rsData;
            }
        }*/
        return ls;
    }
}
