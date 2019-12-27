package com.plantdata.kgcloud.domain.j2r.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.common.util.JsonPathUtil;
import com.plantdata.kgcloud.domain.j2r.entity.AttrConfig;
import com.plantdata.kgcloud.domain.j2r.entity.ConceptConfig;
import com.plantdata.kgcloud.domain.j2r.entity.EntityBean;
import com.plantdata.kgcloud.domain.j2r.entity.Setting;
import com.plantdata.kgcloud.domain.j2r.service.J2rService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Service
public class J2rServiceImpl implements J2rService {

    @Autowired
    private KgmsClient kgmsClient;

    @Override
    public boolean checkSetting(Setting setting) {

        if (setting.getDataSetId() == null) {
            throw new BizException(50010, "dataSetId can not be empty!");
        } else if (StringUtils.isBlank(setting.getKgName())) {
            throw new BizException(50011, "kgName can not be empty!");
        } else if (setting.getConfig() == null) {
            throw new BizException(50012, "config can not be empty!");
        } else {
            for (ConceptConfig config : setting.getConfig()) {
                List<String> idPath = config.getIdPath();
                String entityPath = config.getEntityPath();
                boolean idPathCheck = idPath == null || idPath.isEmpty() && StringUtils.isNotBlank(entityPath);
                if (idPathCheck) {
                    throw new BizException(50013, String.format("实体名称为【%s】的映射未设置实体ID", entityPath));
                }
                List<AttrConfig> attrs = config.getAttrConfigs();
                if (attrs != null) {
                    attrs.forEach(s -> {
                        Integer type = s.getType();
                        String valuePath = s.getJsonPath();
                        if (type == 1) {
                            long pathConfigNum = setting.getConfig().stream().filter(a -> a.getEntityPath().equals(valuePath)).count();
                            if (pathConfigNum <= 0) {
                                throw new BizException(50014, String.format("关系值域为【%s】的映射不存在", valuePath));
                            }
                        }
                    });
                }
            }
        }
        return true;
    }

    @Override
    public Object pathReview(String jsonStr, List<String> jsonPathLs) {

        List<Object> rs = Lists.newArrayList();
        for (String path : jsonPathLs) {
            String result = JsonPathUtil.read(jsonStr, path);
            if (result != null) {
                try {
                    if (result.startsWith("[{")) {
                        rs.add(JSONArray.parseArray(result, JSONObject.class));
                    } else if (result.startsWith("[")) {
                        rs.add(JSONArray.parseArray(result, String.class));
                    } else if (result.startsWith("{")) {
                        rs.add(JSONObject.parseObject(result));
                    } else {
                        rs.add(result);
                    }
                } catch (Exception e) {
                    rs.add(result);
                }
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> preview(String jsonId, Setting setting) {

        checkSetting(setting);
        Map<String, Object> rsMap = new HashMap<>(20);
        ApiReturn<Map<String, Object>> apiReturn = kgmsClient.dataOptFindById(setting.dataSetId, jsonId);
        Map<String, Object> m = apiReturn.getErrCode() == 200 ? apiReturn.getData() : new HashMap<>();
        String jsonStr = JacksonUtils.writeValueAsString(m);
        List<String> entityNames = Lists.newArrayList();
        List<Map<String, Object>> attrs = Lists.newArrayList();

        List<ConceptConfig> conceptConfigs = setting.getConfig();
        List<ConceptConfig> basicConfigs = conceptConfigs.stream().filter(s -> StringUtils.isNotBlank(s.getEntityPath())).collect(toList());
        List<ConceptConfig> tableConfigs = conceptConfigs.stream().filter(
                s -> s.getTableEntityRow() != null || s.getTableEntityCol() != null).collect(toList());
        // <jsonPath-entity> cache
        Map<String, List<EntityBean>> pathCache = new HashMap<>(20);
        // handle entity
        handleEntity(basicConfigs, jsonStr, pathCache, entityNames);
        // handle attrs
        handleAttr(basicConfigs, jsonStr, entityNames, attrs, pathCache);
        // handle table
        handleTable(tableConfigs, jsonStr, attrs, entityNames);

        rsMap.put("entityNames", Sets.newHashSet(entityNames));
        rsMap.put("attrs", attrs);
        return rsMap;
    }

    private void handleEntity(List<ConceptConfig> jsonConfig, String jsonStr, Map<String, List<EntityBean>> pathCache, List<String> entityNames) {

        for (int i = 0; i < jsonConfig.size(); i++) {
            ConceptConfig conceptConfig = jsonConfig.get(i);
            String entityPath = conceptConfig.getEntityPath();

            if (StringUtils.isNotBlank(entityPath)) {
                List<String> names = JsonPathUtil.getValeByPath(jsonStr, entityPath);
                entityNames.addAll(names);
                List<EntityBean> tempEntityLs = names.stream().map(s -> {
                    EntityBean bean = new EntityBean();
                    bean.setName(s);
                    return bean;
                }).collect(toList());
                if (!tempEntityLs.isEmpty()) {
                    pathCache.put(entityPath, tempEntityLs);
                }
            }
        }
    }

    private void handleAttr(List<ConceptConfig> jsonConfig, String jsonStr, List<String> entityNames,
                            List<Map<String, Object>> attrs, Map<String, List<EntityBean>> pathCache) {

        for (int i = 0; i < jsonConfig.size(); i++) {
            ConceptConfig conceptConfig = jsonConfig.get(i);
            String entityPath = conceptConfig.getEntityPath();

            // 处理数值属性和关系属性
            if (StringUtils.isNotBlank(entityPath)) {
                List<EntityBean> domainBeans = pathCache.get(entityPath);
                if (domainBeans == null) {
                    continue;
                }
                List<AttrConfig> attrConfigs = conceptConfig.getAttrConfigs();
                for (int j = 0; attrConfigs != null && j < attrConfigs.size(); j++) {
                    AttrConfig attrConfig = attrConfigs.get(j);
                    List<String> values = JsonPathUtil.getValeByPath(jsonStr, attrConfig.getJsonPath());
                    // 全映射
                    if (attrConfig.getMapType() == null || attrConfig.getMapType() == 1) {
                        for (int k = 0; k < domainBeans.size(); k++) {
                            for (int l = 0; l < values.size(); l++) {
                                EntityBean entityBean = domainBeans.get(k);
                                String value = values.get(l);
                                saveRelationMapAttr(entityBean, value, attrConfig, attrs, entityNames);
                            }
                        }
                    } else { // 索引映射
                        for (int k = 0; k < domainBeans.size() && k < values.size(); k++) {
                            EntityBean entityBean = domainBeans.get(k);
                            String value = values.get(k);
                            saveRelationMapAttr(entityBean, value, attrConfig, attrs, entityNames);
                        }
                    }
                }
            }
        }
    }

    private void saveRelationMapAttr(EntityBean entityBean, String value, AttrConfig attrConfig, List<Map<String, Object>> attrs,
                                     List<String> entityNames) {

        if (StringUtils.isBlank(value)) {
            return;
        }
        Integer type = attrConfig.getType();
        Map<String, Object> m = new HashMap<>();
        m.put("entityName", entityBean.getName());
        m.put("attrName", attrConfig.getName());
        m.put("attrValue", value);
        m.put("attrId", attrConfig.getId());
        if (type == 1) {
            // 关系映射
            entityNames.add(value);
        } else if (type == 2) {
            // 标引映射
            try {
                JSONObject annotationTag = JSONObject.parseObject(value);
                value = annotationTag.getString("name");
                m.put("attrValue", value);
                entityNames.add(value);
            } catch (Exception e) {
                // todo
            }
        }
        attrs.add(m);
    }

    private void handleTable(List<ConceptConfig> tableConfigs, String jsonStr, List<Map<String, Object>> attrs, List<String> entityNames) {

        if (tableConfigs == null) {
            return;
        }
        for (int i = 0; i < tableConfigs.size(); i++) {
            ConceptConfig conceptConfig = tableConfigs.get(i);
            List<AttrConfig> attrConfigs = conceptConfig.getAttrConfigs();
            JSONArray tables = getTables(conceptConfig, jsonStr);
            Integer entityRow = conceptConfig.getTableEntityRow();
            Integer entityCol = conceptConfig.getTableEntityCol();
            Integer entityIndex = conceptConfig.getTableEntityIndex();
            // 提取Table中的实体
            for (int j = 0; j < tables.size(); j++) {
                JSONObject table = tables.getJSONObject(j);
                // 把表格数据转为二维数组
                Object tableArr[][] = conventTableArr(table, entityCol);
                if (entityCol != null) { //行列反转过
                    entityRow = entityCol;
                }
                JSONArray entityArr = table.getJSONArray("row" + entityRow);
                if (entityArr == null) {
                    continue;
                }
                List<String> names = entityArr.toJavaList(String.class);
                names = names.subList(entityIndex - 1, names.size());
                entityNames.addAll(names);

                for (int k = 0; attrConfigs != null && k < attrConfigs.size(); k++) {
                    AttrConfig attrConfig = attrConfigs.get(k);
                    Integer attrRow = attrConfig.getTableAttrRow();
                    if (entityCol != null) {
                        attrRow = attrConfig.getTableAttrCol();
                    }
                    if (attrRow == null || attrRow <= entityRow) {
                        continue;
                    }

                    List<Object> valueArr = Lists.newArrayList(tableArr[attrRow - 1]);
                    List<Object> values = valueArr.subList(attrConfig.getTableAttrIndex() - 1, valueArr.size());
                    if (values.isEmpty()) {
                        continue;
                    }

                    for (int l = 0; l < names.size(); l++) {
                        EntityBean entityBean = new EntityBean();
                        entityBean.setName(names.get(i));
                        if (attrConfig.getMapType() == 1) {
                            // 全映射
                            for (Object value : values) {
                                saveRelationMapAttr(entityBean, value.toString(), attrConfig, attrs, entityNames);
                            }
                        } else if (attrConfig.getMapType() == 2 && values.size() > l) {
                            // 索引映射
                            saveRelationMapAttr(entityBean, values.get(l).toString(), attrConfig, attrs, names);
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据path或name定位提取Table部分
     * @author xiezhenxiang 2019/5/9
     **/
    private JSONArray getTables(ConceptConfig conceptConfig, String jsonStr) {

        JSONArray arr = new JSONArray();
        String tableStr = null;
        if (StringUtils.isNotBlank(conceptConfig.getTableJsonPath())) {
            tableStr = JsonPathUtil.read(jsonStr, conceptConfig.getTableJsonPath());
        } else if (StringUtils.isNotBlank(conceptConfig.getTableName())) {
            tableStr = JsonPathUtil.read(jsonStr, "Table.*[?(@.name=~ /.*" + conceptConfig.getTableName() + ".*/)]");
        }

        if (StringUtils.isNotBlank(tableStr) && tableStr.startsWith("[")) {
            arr = JSONArray.parseArray(tableStr);
        } else if (StringUtils.isNotBlank(tableStr) && tableStr.startsWith("{")) {
            arr.add(JSONObject.parseObject(tableStr));
        }
        return arr;
    }

    /**
     * 把表格转为二维数组
     * @author xiezhenxiang 2019/4/2
     **/
    private Object[][] conventTableArr(JSONObject table, Integer entityCol) {

        Object[][] arr = new Object[table.size()][];
        for (int row = 1; ; row++) {
            JSONArray rowArr = table.getJSONArray("row" + row);
            if (rowArr != null) {
                List<String> ls = rowArr.toJavaList(String.class);
                arr[row - 1] = ls.toArray();
            } else {
                break;
            }
        }
        // 如果实体在列，反转表格，使实体在行
        if (entityCol != null) {
            Object[][] arr2 = new Object[arr[0].length][arr.length];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    arr2[j][i] = arr[i][j];
                }

            }
            arr = arr2;
        }
        return arr;
    }
}
