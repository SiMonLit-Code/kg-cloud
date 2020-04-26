package com.plantdata.kgcloud.domain.access.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.access.rsp.*;
import com.plantdata.kgcloud.sdk.rsp.CustomColumnRsp;
import com.plantdata.kgcloud.sdk.rsp.CustomRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.CustomTableRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.stream.Collectors;


public class YamlTransFunc {


    private static List<String> pbList = new ArrayList<>();
    public static String ENUM_CONCEPT = "$concept";
    private static Map<String,Integer> attDataTypeMap = new HashMap<>();

    static {
        pbList.add("name");
        pbList.add("meaningTag");
        pbList.add("img");
        pbList.add("abs");
        pbList.add("synonyms");


        attDataTypeMap.put("int",1);
        attDataTypeMap.put("float",2);
        attDataTypeMap.put("double",2);
        attDataTypeMap.put("datetime",4);
        attDataTypeMap.put("date",41);
        attDataTypeMap.put("time",42);
        attDataTypeMap.put("string",5);
        attDataTypeMap.put("map",8);
        attDataTypeMap.put("link",9);
        attDataTypeMap.put("text",10);
    }
    private static Yaml yaml = new Yaml();

    public static void main(String[] args) {
        String yaml = "{\n" +
        "        \"tableName\":\"t_relation\",\n" +
                "        \"typeEnum\":null,\n" +
                "        \"columns\":[\n" +
                "            {\n" +
                "                \"name\":\"system\",\n" +
                "                \"tag\":\"系统.name\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"系统\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"user\",\n" +
                "                \"tag\":\"用户.name\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"用户\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"dc\",\n" +
                "                \"tag\":\"数据中心.name\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"数据中心\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"idc\",\n" +
                "                \"tag\":\"机房.name\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"机房\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"rack\",\n" +
                "                \"tag\":\"机架.name\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"机架\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"camera_stand\",\n" +
                "                \"tag\":null,\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"equipment\",\n" +
                "                \"tag\":null,\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"设备\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"IP\",\n" +
                "                \"tag\":null,\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"ip\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\":\"seat\",\n" +
                "                \"tag\":\"机柜.$concept\",\n" +
                "                \"type\":\"text\",\n" +
                "                \"comment\":\"席位\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"relationRsps\":[\n" +
                "            {\n" +
                "                \"domain\":\"机架\",\n" +
                "                \"endTime\":\"\",\n" +
                "                \"name\":\"所属机房\",\n" +
                "                \"range\":[\n" +
                "                    \"机房\"\n" +
                "                ],\n" +
                "                \"relationAttrs\":[\n" +
                "                    {\n" +
                "                        \"comment\":\"\",\n" +
                "                        \"name\":\"IP\",\n" +
                "                        \"tag\":\"机柜IP\",\n" +
                "                        \"type\":\"\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"startTime\":\"\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }";



        System.out.println(JSON.toJSONString(tranConfig(JacksonUtils.readValue(yaml,CustomTableRsp.class))));
    }

    public static List tranConfig(CustomTableRsp label){
        List rs = new ArrayList<>();

        if(label == null || label.getColumns()== null || label.getColumns().isEmpty()){
            return rs;
        }


        Map<String,Map<String,TransPropertyRsp>> entityPropertyMap = new HashMap<>();
        Map<String, TransInsConfigRsp> entityTypeMap = new HashMap<>();
        Map<String,Map<String,TransAttrRsp>> attrMap = new HashMap<>();
        for(CustomColumnRsp column : label.getColumns()){
            if(column.getTag() == null || column.getTag().isEmpty()){
                continue;
            }

            String[] tags = column.getTag().split("\\.");
            if(tags.length != 2){
                continue;
            }

            if(pbList.contains(tags[1])){
                Map<String,TransPropertyRsp> propertyRsps = entityPropertyMap.get(tags[0]);

                if(propertyRsps == null){
                    propertyRsps = new HashMap<>();
                }

                if(propertyRsps.containsKey(tags[1])){
                    TransPropertyRsp props = propertyRsps.get(tags[1]);
                    props.getMapField().add(column.getName());
                }else{
                    TransPropertyRsp props = new TransPropertyRsp();
                    props.setProperty(tags[1]);
                    props.setMapField(Lists.newArrayList(column.getName()));
                    propertyRsps.put(tags[1],props);
                }

                entityPropertyMap.put(tags[0],propertyRsps);
            }else if(ENUM_CONCEPT.equals(tags[1])){
                TransInsConfigRsp entityType = new TransInsConfigRsp();
                entityType.setName(Lists.newArrayList(column.getName()));
                entityType.setMeaningTag(Lists.newArrayList());
                entityType.setNameIsEnum(true);

                entityTypeMap.put(tags[0],entityType);
            }else{
                Map<String,TransAttrRsp> propertyRsps = attrMap.get(tags[0]);

                if(propertyRsps == null){
                    propertyRsps = new HashMap<>();
                    attrMap.put(tags[0],propertyRsps);
                }

                if(propertyRsps.containsKey(tags[1])){
                    TransAttrRsp props = propertyRsps.get(tags[1]);
                    props.getMapField().add(column.getName());
                }else{
                    TransAttrRsp props = new TransAttrRsp();
                    props.setProperty(tags[1]);
                    props.setDataType(attDataTypeMap.get(column.getType()));
                    props.setMapField(Lists.newArrayList(column.getName()));
                    propertyRsps.put(tags[1],props);
                }
            }

        }

        Collection<Map<String,TransPropertyRsp>> ents = entityPropertyMap.values();
        if(ents == null ||ents.isEmpty()){
            return rs;
        }

        for(Map.Entry<String,Map<String,TransPropertyRsp>> entry : entityPropertyMap.entrySet()){

            TransEntityConfigRsp entity = new TransEntityConfigRsp();
            List<TransPropertyRsp> entTypes = Lists.newArrayList(entry.getValue().values());
            entity.setEntity(entTypes);
            entity.setDataType("entity");
            if(attrMap.get(entry.getKey()) != null && attrMap.get(entry.getKey()).values() != null){
                Collection c = attrMap.get(entry.getKey()).values();
                entity.setAttrs(new ArrayList<>(c));
            }else{
                entity.setAttrs(Lists.newArrayList());
            }

            if(entityTypeMap.containsKey(entry.getKey())){

                entity.setEntityType(entityTypeMap.get(entry.getKey()));
            }else{

                TransInsConfigRsp type = new TransInsConfigRsp();
                type.setName(Lists.newArrayList(entry.getKey()));
                type.setMeaningTag(Lists.newArrayList());
                type.setNameIsEnum(false);
                entity.setEntityType(type);
            }

            rs.add(entity);
        }


        if(label.getRelationRsps() != null && !label.getRelationRsps().isEmpty()){

            for(CustomRelationRsp relationRsp : label.getRelationRsps()){

                if(relationRsp.getName() == null || relationRsp.getName().isEmpty()){
                    continue;
                }

                String domain = relationRsp.getDomain();

                TransInsConfigRsp domainType = entityTypeMap.get(domain);
                if(domainType == null){
                    TransInsConfigRsp type = new TransInsConfigRsp();
                    type.setName(Lists.newArrayList(domain));
                    type.setMeaningTag(Lists.newArrayList());
                    type.setNameIsEnum(false);
                    domainType = type;
                }


                List<String> ranges = relationRsp.getRange();
                if(ranges == null || ranges.isEmpty()){
                    continue;
                }

                for(String range : ranges){

                    TransInsConfigRsp rangeType = entityTypeMap.get(range);
                    if(rangeType == null){
                        TransInsConfigRsp type = new TransInsConfigRsp();
                        type.setName(Lists.newArrayList(range));
                        type.setMeaningTag(Lists.newArrayList());
                        type.setNameIsEnum(false);
                        rangeType = type;
                    }


                    TransRelationConfigRsp relationConfigRsp = new TransRelationConfigRsp();
                    relationConfigRsp.setTimeFrom(relationRsp.getStartTime());
                    relationConfigRsp.setTimeTo(relationRsp.getEndTime());
                    relationConfigRsp.setDataType("relation");
                    relationConfigRsp.setEntityType(domainType);


                    relationConfigRsp.setEntity(entityPropertyMap.get(domain).values().stream().map( s -> ConvertUtils.convert(TransPropertyRsp.class).apply(s)).collect(Collectors.toList()));
                    relationConfigRsp.setValue(entityPropertyMap.get(range).values().stream().map( s -> ConvertUtils.convert(TransPropertyRsp.class).apply(s)).collect(Collectors.toList()));
                    relationConfigRsp.setValueType(rangeType);

                    List<CustomColumnRsp> relaAttrs = relationRsp.getRelationAttrs();
                    List<TransAttrRsp> relas = new ArrayList<>();
                    if(relaAttrs != null && !relaAttrs.isEmpty()){

                        for(CustomColumnRsp relaAttr : relaAttrs){
                            if(relaAttr.getTag() == null || relaAttr.getTag().isEmpty()){
                                continue;
                            }

                            TransAttrRsp a = new TransAttrRsp();
                            a.setProperty(relaAttr.getTag());
                            a.setMapField(Lists.newArrayList(relaAttr.getName()));
                            a.setDataType(attDataTypeMap.get(relaAttr.getType()));

                            relas.add(a);
                        }
                    }


                    TransInsConfigRsp rela = new TransInsConfigRsp();
                    rela.setName(Lists.newArrayList(relationRsp.getName()));
                    rela.setNameIsEnum(false);
                    relationConfigRsp.setRela(rela);
                    relationConfigRsp.setRelaAttrs(relas);

                    rs.add(relationConfigRsp);
                }

            }

        }

        return rs;
    }

    public static Map<String, JSONArray> tranTagConfig(String yamlStr) {
        //yaml转json

        JSONObject jsonObject = new JSONObject(yaml.load(yamlStr));

        HashMap<String, String> tbTagMap = new HashMap<>();

        Map<String, JSONArray> tableTasksMap = new HashMap<>();

        JSONArray tables = jsonObject.getJSONArray("tables");

        for (Object table : tables) {
            JSONObject json = new JSONObject((Map<String, Object>) table);
            String tbname = json.keySet().iterator().next();
            String tbTag = json.getString(tbname);
            tbTagMap.put(tbname, tbTag);
        }
        for (String tbname : tbTagMap.keySet()) {
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

        if(jsonObject.getJSONArray("columns") != null){
            for (Object o : jsonObject.getJSONArray("columns")) {
                if(o == null){
                    continue;
                }
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
        }

        if (jsonObject.get("relation") != null) {
            for (Object o : jsonObject.getJSONArray("relation")) {
                if(o == null){
                    continue;
                }
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
            relationMap.put(relaname+relation.getString("entityTypeName"), relation);
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
            case "datetime":
                return 4;
            case "date":
                return 41;
            case "time":
                return 42;
            case "string":
                return 5;
            case "map":
                return 8;
            case "link":
                return 9;
            case "text":
                return 10;
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