package com.plantdata.kgcloud.domain.dw.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderAttrRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderConceptRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderRelationAttrRsp;
import com.plantdata.kgcloud.exception.BizException;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class PaserYaml2SchemaUtil {

    private static Map<String,String> attSetMap = new HashMap<>();
    private static Map<String,Integer> attDataTypeMap = new HashMap<>();
    static {
        attSetMap.put("名称", "name");
        attSetMap.put("消歧标识", "meaningTag");
        attSetMap.put("图片", "img");
        attSetMap.put("简介", "abs");
        attSetMap.put("同义词", "synonyms");

        attDataTypeMap.put("int",2);
        attDataTypeMap.put("string",5);
    }

    public static void main(String[] args) {
        //yaml转json
        Yaml yaml = new Yaml();
        String document = "tables:\n" +
                "  - student: 学生表\n" +
                "student:\n" +
                "  relation:\n" +
                "    - 学生 > 选课 > 课程\n" +
                "  columns:\n" +
                "    - name: { tag: 学生.名称, type: string }\n" +
                "    - age: { tag: 学生.年龄, type: int }\n" +
                "    - class: { tag: 课程.名称, type: string }\n" +
                "    - time: { tag: <选课>.时间, type: string }";
        Map a = (Map<String, Object>) yaml.load(document);
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(a);

        System.out.println(JSON.toJSONString(parserYaml2Schema(jsonObject)));
    }

    public static List<PreBuilderConceptRsp> parserYaml2Schema(JSONObject json){

        if(json == null || json.isEmpty() || !json.containsKey("tables")){
            return new ArrayList<>();
        }

        JSONArray tables = json.getJSONArray("tables");

        Map<String, PreBuilderConceptRsp> conceptRspMap = new HashMap<>();

        for(int i=0; i< tables.size(); i++){
            JSONObject tab = tables.getJSONObject(i);

            Set<String> key = tab.keySet();
            if(key.isEmpty()){
                continue;
            }

            String tableName = key.iterator().next();

            JSONObject tabJOSNObj = json.getJSONObject(tableName);

            if(tabJOSNObj == null){
                continue;
            }

            JSONArray columns = tabJOSNObj.getJSONArray("columns");
            JSONArray relations = tabJOSNObj.getJSONArray("relation");

            List<YamlColumn> columnList = convertColumn(columns);
            List<YamlRelation> relationList = convertRelation(relations);

            Map<String,List<YamlColumn>> relationColumn = new HashMap<>();

            for(YamlColumn column : columnList){

                if(column.getIsRelationAttr()){
                    List<YamlColumn> relationAttrList = relationColumn.containsKey(column.getConceptOrRelationName()) ? relationColumn.get(column.getConceptOrRelationName()) : new ArrayList<>();
                    relationAttrList.add(column);
                    relationColumn.put(column.getConceptOrRelationName(),relationAttrList);
                }else{
                    PreBuilderConceptRsp concept = null;
                    if(conceptRspMap.containsKey(column.getConceptOrRelationName())){
                        concept = conceptRspMap.get(column.getConceptOrRelationName());
                    }else{
                        concept = new PreBuilderConceptRsp();
                        concept.setName(column.getConceptOrRelationName());

                        conceptRspMap.put(column.getConceptOrRelationName(),concept);
                    }

                    if(attSetMap.containsKey(column.getAttrName())){
                        //默认的属性不需要加属性定义
                        continue;
                    }
                    List<PreBuilderAttrRsp> attrs = concept.getAttrs() == null ? new ArrayList<>() : concept.getAttrs();
                    attrs.add(PreBuilderAttrRsp.builder().name(column.getAttrName()).attrType(0).dataType(attDataTypeMap.get(column.getType())).build());
                    concept.setAttrs(attrs);
                }
            }

            for(YamlRelation relation : relationList){

                String domain = relation.getDomain();
                String range = relation.getRange();

                if(!conceptRspMap.containsKey(domain) || !conceptRspMap.containsKey(range)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
                }

                PreBuilderConceptRsp conceptRsp = conceptRspMap.get(domain);
                PreBuilderAttrRsp attrRsp = PreBuilderAttrRsp.builder()
                        .attrType(1)
                        .name(relation.getRelationName())
                        .rangeName(range)
                        .build();

                if(relationColumn.containsKey(relation.getRelationName())){

                    List<YamlColumn> relationAtt = relationColumn.get(relation.getRelationName());

                    List<PreBuilderRelationAttrRsp> attrs = new ArrayList<>();

                    for(YamlColumn column : relationAtt){

                        attrs.add(PreBuilderRelationAttrRsp.builder()
                                .dataType(attDataTypeMap.get(column.getType()))
                                .name(column.getAttrName())
                                .build());
                    }
                    attrRsp.setRelationAttrs(attrs);

                }
                List<PreBuilderAttrRsp> attrs = conceptRsp.getAttrs() == null ? new ArrayList<>() : conceptRsp.getAttrs();
                attrs.add(attrRsp);
                conceptRsp.setAttrs(attrs);
            }
        }

        List<PreBuilderConceptRsp> rsList = new ArrayList();

        rsList.addAll(conceptRspMap.values());
        return rsList;
    }



    private static List<YamlRelation> convertRelation(JSONArray relations) {

        if(relations == null ||relations.isEmpty()){
            return new ArrayList<>();
        }

        List<YamlRelation> relation = new ArrayList<>();

        for(int i = 0; i< relations.size(); i++){
            String rela = relations.getString(i);

            String[] relationValue = rela.split(">");

            if(relationValue.length != 3){
                throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
            }

            relation.add(YamlRelation.builder().domain(relationValue[0].trim()).range(relationValue[2].trim()).relationName(relationValue[1].trim()).build());
        }

        return relation;
    }


    public static List<YamlColumn> convertColumn(JSONArray columns){

        if(columns == null ||columns.isEmpty()){
            return new ArrayList<>();
        }

        List<YamlColumn> columnList = new ArrayList<>();
        for(int i=0; i<columns.size(); i++){

            JSONObject column = columns.getJSONObject(i);
            for (String key : column.keySet()) {

                JSONObject columnValue = column.getJSONObject(key);
                String tag = columnValue.getString("tag");
                String type = columnValue.getString("type");

                String[] tags = tag.split("\\.");

                if(tags.length != 2){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
                }

                YamlColumn col = YamlColumn.builder().attrName(tags[1]).type(type).columnName(key).conceptOrRelationName(tags[0]).build();
                if (tags[0].startsWith("<")) {
                    col.setIsRelationAttr(true);
                    col.setConceptOrRelationName(tags[0].substring(1, tags[0].length() - 1));
                } else {
                    col.setIsRelationAttr(false);
                }

                columnList.add(col);
                break;
            }

        }

        return columnList;
    }
}
