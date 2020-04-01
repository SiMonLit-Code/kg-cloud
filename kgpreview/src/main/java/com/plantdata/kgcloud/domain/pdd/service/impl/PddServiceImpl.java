package com.plantdata.kgcloud.domain.pdd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.pdd.service.PddService;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.KgtextClient;
import com.plantdata.kgcloud.sdk.bo.SchemaConfigBO;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.EventRsp;
import com.plantdata.kgcloud.sdk.rsp.ModelSchemaRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.plantdata.kgcloud.config.Constants.KG_MYSQL;

/**
 * @author xiezhenxiang 2019/12/10
 */
@Service
public class PddServiceImpl implements PddService {

    @Autowired
    private KgmsClient kgmsClient;
    @Autowired
    private KgtextClient kgtextClient;

    @Override
    public Map getTag(Long dmId) {

        String sql = "select * from data_set where id = " + dmId;
        JSONObject dm = KG_MYSQL.findOne(sql);
        String tbName = dm.getString("tb_name");
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
        long taskId = Long.parseLong(tbName.substring(0, tbName.indexOf("_")));
        ModelSchemaRsp schemaRsp = kgtextClient.getCorpusDetails(taskId).getData();

        entityTag.addAll(schemaRsp.getSchemaConfig().getEntity());
        List<String> relTags = schemaRsp.getSchemaConfig().getRelation().stream().map(SchemaConfigBO.DomainBean::getName).collect(Collectors.toList());
        objAttrTag.addAll(relTags);;

        List<String> attrTags = schemaRsp.getSchemaConfig().getAttr().stream().map(SchemaConfigBO.DomainBean::getName).collect(Collectors.toList());
        basicAttrTag.addAll(attrTags);

        List<EventRsp> eventRsps = kgtextClient.listByCorpusId(taskId).getData();
        for (EventRsp obj : eventRsps) {
            if (obj.getEventElement() == null) {
                continue;
            }
            String type = obj.getEventType();
            eventType.add(type);
            if (obj.getEventElement() != null) {
                if (!obj.getEventElement().isEmpty()) {
                    obj.getEventElement().add("触发词");
                }
                eventElement.put(type, obj.getEventElement());
            }
        }

        Map<String, Object> rs = new HashMap<>(6);
        rs.put("entityTag", entityTag);
        rs.put("basicAttrTag", basicAttrTag);
        rs.put("objAttrTag", objAttrTag);
        rs.put("eventType", eventType);
        rs.put("eventElement", eventElement);
        return rs;
    }
}
