package com.plantdata.kgcloud.domain.access.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class YamlTransFunc {
    private static Yaml yaml = new Yaml();

    public static Map<String, JSONArray> tranTagConfig(String yamlStr) {
        //yaml转json

        JSONObject jsonObject = new JSONObject(yaml.load(yamlStr));

        HashMap<String, String> tbtagMap = new HashMap<>();

        Map<String, JSONArray> tableTasksMap = new HashMap<>();

        JSONArray tables = jsonObject.getJSONArray("tables");
        for (Object table : tables) {
            JSONObject json = new JSONObject((Map<String, Object>) table);
            String tbname = json.keySet().iterator().next();
            String tbTag = json.getString(tbname);
            tbtagMap.put(tbname, tbTag);
        }
        for (String tbname : tbtagMap.keySet()) {
            JSONObject jsonObject1 = jsonObject.getJSONObject(tbname);
            JSONArray transConfig = getTransConfig(jsonObject1);
            tableTasksMap.put(tbname, transConfig);
        }

        return tableTasksMap;
    }

    private static JSONArray getTransConfig(JSONObject jsonObject) {

        HashMap<String, JSONObject> entityMap = new HashMap<>();
        HashMap<String, JSONObject> relationMap = new HashMap<>();


        Map<String, ArrayList<TaskColumn>> columns = new HashMap<>();
        List<TaskRelation> relations = new ArrayList<>();
        HashMap<String, ArrayList<TaskColumn>> relaAttrs = new HashMap<>();
        for (Object o : jsonObject.getJSONArray("columns")) {
            TaskColumn column = new TaskColumn(new JSONObject((Map<String, Object>) o).toJSONString());
            if (column.isRelaAttr) {
                ArrayList<TaskColumn> orDefault = relaAttrs.getOrDefault(column.entityNameOrRelationName, new ArrayList<>());
                orDefault.add(column);
                relaAttrs.put(column.entityNameOrRelationName, orDefault);
            } else {
                ArrayList<TaskColumn> orDefault = columns.getOrDefault(column.entityNameOrRelationName, new ArrayList<>());
                orDefault.add(column);
                columns.put(column.entityNameOrRelationName, orDefault);
            }
        }
        if (jsonObject.get("relation") != null) {
            for (Object o : jsonObject.getJSONArray("relation")) {
                TaskRelation relation = new TaskRelation(o.toString());
                relations.add(relation);
            }
        }


        for (Map.Entry<String, ArrayList<TaskColumn>> entry : columns.entrySet()) {
            String entityName = entry.getKey();
            ArrayList<TaskColumn> value = entry.getValue();
            JSONObject entity = getEntity();
            entity.put("entityTypeName", entityName);
            for (TaskColumn column : value) {
                JSONObject tmp = new JSONObject();
                if (pbSet.containsKey(column.attrName)) {
                    tmp.put("property", pbSet.get(column.attrName));
                    JSONArray arr = new JSONArray();
                    arr.add(column.field);
                    tmp.put("mapField", arr);
                    entity.getJSONArray("ent").add(tmp);
                } else {
                    tmp.put("attrName", column.attrName);
                    tmp.put("dataType", column.type);
                    JSONArray arr = new JSONArray();
                    arr.add(column.field);
                    tmp.put("mapField", arr);

                    entity.getJSONArray("attrs").add(tmp);
                }
            }
            entityMap.put(entityName, entity);
        }


        for (TaskRelation rela : relations) {
            JSONObject relation = getRelation();
            String fromEntityName = rela.from;
            String toEntityName = rela.to;
            String relaname = rela.name;

            JSONObject fromObj = entityMap.get(fromEntityName);
            relation.put("entityTypeName", fromEntityName);
            relation.put("valueTypeName", toEntityName);

            List<Object> from = fromObj.getJSONArray("ent")
                    .stream()
                    .filter(j -> ((JSONObject) j).getString("property").equals("name")
                            || ((JSONObject) j).getString("property").equals("meaningTag"))
                    .collect(Collectors.toList());

            for (Object o : from) {
                relation.getJSONArray("ent").add(JSONObject.parseObject(o.toString()));
            }


            JSONObject toObj = entityMap.get(toEntityName);

            List<Object> to = toObj.getJSONArray("ent")
                    .stream()
                    .filter(j -> ((JSONObject) j).getString("property").equals("name")
                            || ((JSONObject) j).getString("property").equals("meaningTag")).collect(Collectors.toList());

            for (Object o : to) {
                relation.getJSONArray("relaValue").add(JSONObject.parseObject(o.toString()));
            }


            ArrayList<TaskColumn> orDefault = relaAttrs.getOrDefault(relaname, new ArrayList<>());
            for (TaskColumn column : orDefault) {
                JSONObject tmp = new JSONObject();
                tmp.put("attrName", column.attrName);
                tmp.put("dataType", column.type);
                JSONArray arr = new JSONArray();
                arr.add(column.field);
                tmp.put("mapField", arr);

                relation.getJSONArray("relaAttrs").add(tmp);
            }

            relation.getJSONObject("rela").put("relationName", relaname);
            relationMap.put(relaname, relation);
        }
        JSONArray re = new JSONArray();

        re.addAll(entityMap.values());
        re.addAll(relationMap.values());

        return re;
    }


    private static JSONObject getEntity() {
        String entityStr = "{\n" +
                "    \"dataType\": \"entity\",\n" +
                "    \"entityId\": \"\",\n" +
                "    \"entityTypeName\": \"\",\n" +
                "    \"entId\": [],\n" +
                "    \"ent\": [],\n" +
                "    \"attrs\": [],\n" +
                "    \"trace\": {\n" +
                "        \"reliability\": {\n" +
                "            \"type\": 1,\n" +
                "            \"source\": \"\"\n" +
                "        },\n" +
                "        \"source\": {\n" +
                "            \"type\": 2,\n" +
                "            \"source\": \"\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        return JSON.parseObject(entityStr);
    }

    private static JSONObject getRelation() {

        String relaStr = "{\n" +
                "    \"dataType\": \"relation\",\n" +
                "    \"entityTypeName\": \"\",\n" +
                "    \"entId\": [],\n" +
                "    \"ent\": [],\n" +
                "    \"relaAttrs\": [],\n" +
                "    \"rela\": {\n" +
                "        \"relationName\": \"\",\n" +
                "        \"mapField\": [],\n" +
                "        \"isEnum\": false\n" +
                "    },\n" +
                "    \"timeFrom\": \"\",\n" +
                "    \"timeTo\": \"\",\n" +
                "    \"valueTypeName\": \"\",\n" +
                "    \"relaValueId\": [],\n" +
                "    \"relaValue\": [],\n" +
                "    \"trace\": {\n" +
                "        \"reliability\": {\n" +
                "            \"type\": 1,\n" +
                "            \"source\": \"\"\n" +
                "        },\n" +
                "        \"source\": {\n" +
                "            \"type\": 2,\n" +
                "            \"source\": \"\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        return JSON.parseObject(relaStr);
    }

    public static Integer changeType(String type) {
        switch (type) {
            case "int":
                return 1;
            case "float":
            case "double":
                return 2;
            case "date":
                return 4;
            default:
                return 5;
        }
    }

    private static HashMap<String, String> pbSet = new HashMap<>();

    static {
        pbSet.put("名称", "name");
        pbSet.put("消歧标识", "meaningTag");
        pbSet.put("图片", "img");
        pbSet.put("简介", "abs");
        pbSet.put("同义词", "synonyms");

    }

}

class TaskRelation {
    String from;
    String name;
    String to;

    public TaskRelation(String config) {
        String[] split = config.split(">");
        this.from = split[0].trim();
        this.name = split[1].trim();
        this.to = split[2].trim();
    }
}

class TaskColumn {
    String field;
    String attrName;
    Integer type;
    String entityNameOrRelationName;
    Boolean isRelaAttr = false;

    public TaskColumn(String config) {
        JSONObject jsonObject = JSON.parseObject(config);
        this.field = jsonObject.keySet().iterator().next();
        String tag = jsonObject.getJSONObject(this.field).getString("tag");
        String[] split = tag.split("\\.");
        this.attrName = split[1];

        if (split[0].startsWith("<")) {
            this.isRelaAttr = true;
            this.entityNameOrRelationName = split[0].substring(1, split[0].length() - 1);
        } else {
            this.entityNameOrRelationName = split[0];
        }
        this.type = YamlTransFunc.changeType(jsonObject.getJSONObject(this.field).getString("type"));
    }
}