package com.plantdata.kgcloud.domain.dw.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaserYaml2SchemaUtil {

    private static Map<String,String> attSetMap = new HashMap<>();
    private static Map<String,Integer> attDataTypeMap = new HashMap<>();
    public static List<Integer> attrTypeList = Lists.newArrayList();
    static {
        attSetMap.put("名称", "name");
        attSetMap.put("消歧标识", "meaningTag");
        attSetMap.put("图片", "img");
        attSetMap.put("简介", "abs");
        attSetMap.put("同义词", "synonyms");

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

        attrTypeList.add(1);
        attrTypeList.add(2);
        attrTypeList.add(4);
        attrTypeList.add(41);
        attrTypeList.add(42);
        attrTypeList.add(5);
        attrTypeList.add(8);
        attrTypeList.add(9);
        attrTypeList.add(10);

    }

    public static void main(String[] args) {

        //yaml转json
        Yaml yaml = new Yaml();
        String document = "tables:\n" +
                "  - t_trans_log: 成交记录表\n" +
                "  - t_rack_a: 机柜信息表\n" +
                "  - t_dc: 数据中心表\n" +
                "  - t_equipment: 设备表\n" +
                "  - t_idc: 机房表\n" +
                "  - t_relation: 关系表\n" +
                "  - t_system: 系统表\n" +
                "t_trans_log:\n" +
                "  relation:\n" +
                "    - 席位 > 产生 > 成交记录\n" +
                "  columns:\n" +
                "    - seat: { tag: 席位.名称, type: string }\n" +
                "    - name: { tag: 成交记录.名称, type: string }\n" +
                "    - bug_num: { tag: 成交记录.买成交量, type: int }\n" +
                "    - sale_num: { tag: 成交记录.卖成交量, type: int }\n" +
                "    - trust_buy_num: { tag: 成交记录.委托买次数, type: int }\n" +
                "    - trust_sale_num: { tag: 成交记录.委托卖次数, type: int }\n" +
                "    - deal_time: { tag: 成交记录.成交时间, type: date }\n" +
                "    - deal_price: { tag: 成交记录.成交单价, type: float }\n" +
                "t_rack_a:\n" +
                "  relation:\n" +
                "    - 机柜 > 所属机房 > 机房\n" +
                "    - 机柜 > 所属数据中心 > 数据中心\n" +
                "  columns:\n" +
                "    - rack: { tag: 机柜.名称, type: string }\n" +
                "    - dc: { tag: 数据中心.名称, type: int }\n" +
                "    - idc: { tag: 机房.名称, type: string }\n" +
                "    - rack_capacity: { tag: 机柜.机柜容量, type: string }\n" +
                "    - row: { tag: <所属机房>.所属行, type: string }\n" +
                "    - column: { tag: <所属机房>.所属列, type: string }\n" +
                "    - enabled: { tag: 机柜.启用状态, type: string }\n" +
                "    - on_position: { tag: 机柜.通电状态, type: string }\n" +
                "    - power_type: { tag: 机柜.电源类型, type: string }\n" +
                "    - hz: { tag: 机柜.电流类型, type: int }\n" +
                "    - two_circuit_feed: { tag: 机柜.是否双路供电, type: string }\n" +
                "    - lease_type: { tag: 机柜.租赁类型, type: string }\n" +
                "    - kw: { tag: 机柜.最大额定功率, type: string }\n" +
                "t_system:\n" +
                "  relation:\n" +
                "  columns:\n" +
                "    - system: { tag: 系统.名称, type: string }\n" +
                "    - MAC: { tag: 系统.MAC, type: int }\n" +
                "    - type: { tag: 系统.类型, type: string }\n" +
                "    - status: { tag: 系统.状态, type: string }\n" +
                "t_dc:\n" +
                "  relation:\n" +
                "    - 数据中心 > 所在地区 > 地区\n" +
                "    - 数据中心 > 地址 > 地址\n" +
                "  columns:\n" +
                "    - dc: { tag: 数据中心.名称, type: string }\n" +
                "    - locality: { tag: 地区.名称, type: int }\n" +
                "    - type: { tag: 数据中心.类型, type: string }\n" +
                "    - network_access: { tag: 数据中心.网络接入, type: string }\n" +
                "    - ddress: { tag: 地址.名称, type: string }\n" +
                "t_equipment:\n" +
                "  relation:\n" +
                "  columns:\n" +
                "    - equipment: { tag: 设备.名称, type: string }\n" +
                "    - model: { tag: 设备.型号, type: string }\n" +
                "    - interspace: { tag: 设备.空间, type: string }\n" +
                "    - power: { tag: 设备.功率, type: string }\n" +
                "    - image_url: { tag: 设备.图片, type: string }\n" +
                "    - createDate: { tag: 设备.创建时间, type: date }\n" +
                "t_idc:\n" +
                "  relation:\n" +
                "    - 机房 > 所属数据中心 > 数据中心\n" +
                "  columns:\n" +
                "    - idc: { tag: 机房.名称, type: string }\n" +
                "    - dc: { tag: 数据中心.名称, type: string }\n" +
                "t_relation:\n" +
                "  relation:\n" +
                "    - 机房 > 所属数据中心 > 数据中心\n" +
                "    - 机柜 > 所属机房 > 机房\n" +
                "    - 设备 > 所属机柜 > 机柜\n" +
                "    - 系统 > 所属设备 > 设备\n" +
                "    - 会员 > 使用 > 系统\n" +
                "    - 会员 > 托管 > 设备\n" +
                "    - 会员 > 拥有 > 席位\n" +
                "    - 系统 > 席位 > 席位\n" +
                "\n" +
                "  columns:\n" +
                "    - system: { tag: 系统.名称, type: string }\n" +
                "    - user: { tag: 会员.名称, type: string }\n" +
                "    - dc: { tag: 数据中心.名称, type: string }\n" +
                "    - idc: { tag: 机房.名称, type: string }\n" +
                "    - rack: { tag: 机柜.名称, type: string }\n" +
                "    - camera_stand: { tag: 机位.名称, type: string }\n" +
                "    - equipment: { tag: 设备.名称, type: string }\n" +
                "    - IP: { tag: IP.名称, type: string }\n" +
                "    - seat: { tag: 席位.名称, type: string }";
        Map a = (Map<String, Object>) yaml.load(document);
//        System.out.println(JacksonUtils.writeValueAsString(a));
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(a);
        System.out.println(JSON.toJSONString(parserYaml2TagJson(jsonObject,null)));
    }

    public static List<PreBuilderConceptRsp> parserYaml2Schema(JSONObject json,List<DWTableRsp> tableRsps){

        if(json == null || json.isEmpty() || !json.containsKey("tables")){
            return new ArrayList<>();
        }

        Map<String,List<String>> tableFields = tableRsps.stream().collect(Collectors.toMap(DWTableRsp::getTableName,DWTableRsp::getFields));

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

            List<YamlColumn> columnList = convertColumn(columns,tableFields.get(tableName));
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

                        if(!concept.getTables().contains(tableName)){
                            concept.getTables().add(tableName);
                        }
                    }else{
                        concept = new PreBuilderConceptRsp();
                        concept.setName(column.getConceptOrRelationName());

                        List<String> t = new ArrayList<>();
                        t.add(tableName);
                        concept.setTables(t);

                        conceptRspMap.put(column.getConceptOrRelationName(),concept);
                    }

                    if(attSetMap.containsKey(column.getAttrName())){
                        //默认的属性不需要加属性定义
                        continue;
                    }
                    List<PreBuilderAttrRsp> attrs = concept.getAttrs() == null ? new ArrayList<>() : concept.getAttrs();
                    PreBuilderAttrRsp attrRsp = PreBuilderAttrRsp.builder().name(column.getAttrName()).attrType(0).dataType(attDataTypeMap.get(column.getType())).build();
                    attrRsp.setTables(Lists.newArrayList(tableName));

                    attrs.add(attrRsp);
                    concept.setAttrs(attrs);
                }
            }

            for(YamlRelation relation : relationList){

                String domain = relation.getDomain();
                String range = relation.getRange();

                if(!conceptRspMap.containsKey(domain)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_DOMAIN_NOT_EXIST_ERROR);
                }

                if(!conceptRspMap.containsKey(range)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_RANGE_NOT_EXIST_ERROR);
                }

                PreBuilderConceptRsp conceptRsp = conceptRspMap.get(domain);
                PreBuilderAttrRsp attrRsp = PreBuilderAttrRsp.builder()
                        .attrType(1)
                        .name(relation.getRelationName())
                        .rangeName(range)
                        .tables(Lists.newArrayList(tableName))
                        .build();

                if(relationColumn.containsKey(relation.getRelationName())){

                    List<YamlColumn> relationAtt = relationColumn.get(relation.getRelationName());

                    List<PreBuilderRelationAttrRsp> attrs = new ArrayList<>();

                    for(YamlColumn column : relationAtt){

                        attrs.add(PreBuilderRelationAttrRsp.builder()
                                .dataType(attDataTypeMap.get(column.getType()))
                                .name(column.getAttrName())
                                .tables(Lists.newArrayList(tableName))
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

        distinc(rsList);

        return rsList;
    }

    public static void distinc(List<PreBuilderConceptRsp> rsList) {

        if(rsList == null || rsList.isEmpty()){
            return ;
        }

        for(PreBuilderConceptRsp concept : rsList){

            List<PreBuilderAttrRsp> attrs = concept.getAttrs();
            if(attrs != null && !attrs.isEmpty()){

                Map<String,PreBuilderAttrRsp> existAttrList  = new HashMap<>();
                Iterator<PreBuilderAttrRsp> it = attrs.iterator();


                while (it.hasNext()){

                    PreBuilderAttrRsp attr = it.next();
                    if(existAttrList.containsKey(attr.getName())){


                        PreBuilderAttrRsp relation = existAttrList.get(attr.getName());
                        //引用的表合并
                        relation.getTables().addAll(attr.getTables());

                        //关系 合并边属性
                        if(attr.getAttrType().equals(1)){
                            List<PreBuilderRelationAttrRsp> relationAttrRsps = attr.getRelationAttrs();

                            if(relationAttrRsps != null && relationAttrRsps.isEmpty()){

                                if(relation.getRelationAttrs() == null){
                                    relation.setRelationAttrs(new ArrayList<>());
                                }

                                if(relation.getRelationAttrs().isEmpty()){
                                    relation.getRelationAttrs().addAll(relationAttrRsps);
                                }else{

                                    Map<String,PreBuilderRelationAttrRsp> existRelaAttrs = relation.getRelationAttrs().stream().collect(Collectors.toMap(PreBuilderRelationAttrRsp::getName, Function.identity()));
//                                    relation.getRelationAttrs().forEach(relaAttr -> existRelaAttrs.add(relaAttr.getName()));

                                    for(PreBuilderRelationAttrRsp relationAttr : relationAttrRsps){
                                        if(!existRelaAttrs.containsKey(relationAttr.getName())){
                                            relation.getRelationAttrs().add(relationAttr);
                                            existRelaAttrs.put(relationAttr.getName(),relationAttr);
                                        }else{
                                            if(relationAttr.getTables() == null || relationAttr.getTables().isEmpty()){
                                                continue;
                                            }

                                            PreBuilderRelationAttrRsp relationAttrRsp = existRelaAttrs.get(relationAttr.getName());
                                            if(relationAttrRsp.getTables() == null){
                                                relationAttrRsp.setTables(new ArrayList<>());
                                            }

                                            for(String tableName : relationAttr.getTables()){
                                                if(!relationAttr.getTables().contains(tableName)){
                                                    relationAttr.getTables().add(tableName);
                                                }
                                            }

                                        }
                                    }
                                }


                            }

                        }

                        it.remove();
                    }else{
                        existAttrList.put(attr.getName(),attr);
                    }

                }
            }

        }

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
                throw BizException.of(KgmsErrorCodeEnum.YAML_RELATION_PARSER_ERROR);
            }


            relation.add(YamlRelation.builder().domain(relationValue[0].trim()).range(relationValue[2].trim()).relationName(relationValue[1].trim()).build());
        }

        return relation;
    }


    public static List<YamlColumn> convertColumn(JSONArray columns,List<String> fields){

        if(columns == null ||columns.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_IS_EMTRY_ERROR);
        }

        List<YamlColumn> columnList = new ArrayList<>();
        for(int i=0; i<columns.size(); i++){

            JSONObject column = columns.getJSONObject(i);
            for (String key : column.keySet()) {

                if(!fields.contains(key)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMS_NOT_EXIST_IN_TABLE);
                }

                JSONObject columnValue = column.getJSONObject(key);
                String tag = columnValue.getString("tag");

                if(StringUtils.isEmpty(tag)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_NOT_EXIST);
                }

                String[] tags = tag.split("\\.");

                if(tags.length != 2){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_PARSER_ERROR);
                }

                String type = columnValue.getString("type");

                if(StringUtils.isEmpty(type)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_NOT_EXIST);
                }

                if(!attDataTypeMap.containsKey(type)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_PARSER_ERROR);
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

        if(columnList.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_IS_EMTRY_ERROR);
        }

        Map<String,Boolean> columnNameMap = new HashMap<>();

        for(YamlColumn yamlColumn : columnList){

            if(yamlColumn.getIsRelationAttr()){
                continue;
            }

            if(!columnNameMap.containsKey(yamlColumn.getConceptOrRelationName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),false);
            }

            if("名称".equals(yamlColumn.getAttrName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),true);
            }

        }

        if(columnNameMap.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_NOT_EXIST_CONCEPT);
        }

        columnNameMap.forEach((k,v) -> {
            if(!v){
                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_NOT_EXIST_CONCEPT_NAME);
            }
        });

        return columnList;
    }

    public static List<ModelSchemaConfigRsp> parserYaml2TagJson(JSONObject json, List<DWTableRsp> tableRsps) {
        if(json == null || json.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_FILE_EMTRY_ERROR);
        }

        if(!json.containsKey("tables")){
            throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_NOT_EXIST_ERROR);
        }

        Map<String,List<String>> tableFields = tableRsps.stream().collect(Collectors.toMap(DWTableRsp::getTableName,DWTableRsp::getFields));

        JSONArray tables = json.getJSONArray("tables");

        if(tables.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_IS_EMTRY_ERROR);
        }

        List<ModelSchemaConfigRsp> rsList = new ArrayList<>(tables.size());

        for(int i=0; i< tables.size(); i++){
            JSONObject tab = tables.getJSONObject(i);

            Set<String> key = tab.keySet();
            if(key.isEmpty()){
                continue;
            }

            String tableName = key.iterator().next();

            JSONObject tabJOSNObj = json.getJSONObject(tableName);

            if(tabJOSNObj == null){
                throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_CONFIG_IS_EMTRY_ERROR);
            }

            JSONArray columns = tabJOSNObj.getJSONArray("columns");
            JSONArray relations = tabJOSNObj.getJSONArray("relation");

            List<YamlColumn> columnList = convertColumn(columns,tableFields.get(tableName));
            List<YamlRelation> relationList = convertRelation(relations);

            Set<String> entity = new HashSet<>();
            Set<ModelSchemaConfigRsp.AttrBean> attrBeans = new HashSet<>();
            Set<ModelSchemaConfigRsp.RelationBean> relationBeans = new HashSet<>();

            Map<String,List<YamlColumn>> relationColumn = new HashMap<>();

            for(YamlColumn column : columnList){

                if(column.getIsRelationAttr()){
                    List<YamlColumn> relationAttrList = relationColumn.containsKey(column.getConceptOrRelationName()) ? relationColumn.get(column.getConceptOrRelationName()) : new ArrayList<>();
                    relationAttrList.add(column);
                    relationColumn.put(column.getConceptOrRelationName(),relationAttrList);
                }else{
                    entity.add(column.getConceptOrRelationName());

                    if(attSetMap.containsKey(column.getAttrName())){
                        //默认的属性不需要加属性定义
                        continue;
                    }
                    ModelSchemaConfigRsp.AttrBean attrBean = new ModelSchemaConfigRsp.AttrBean();
                    attrBean.setDomain(column.getConceptOrRelationName());
                    attrBean.setDataType(attDataTypeMap.get(column.getType()));
                    attrBean.setName(column.getAttrName());
                    attrBeans.add(attrBean);
                }
            }

            for(YamlRelation relation : relationList){

                String domain = relation.getDomain();
                String range = relation.getRange();

                if(!entity.contains(domain)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_DOMAIN_NOT_EXIST_ERROR);
                }

                if(!entity.contains(range)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_RANGE_NOT_EXIST_ERROR);
                }


                ModelSchemaConfigRsp.RelationBean relationBean = new ModelSchemaConfigRsp.RelationBean();
                relationBean.setName(relation.getRelationName());
                relationBean.setDomain(domain);
                relationBean.setRange(Sets.newHashSet(range));

                if(relationColumn.containsKey(relation.getRelationName())){

                    List<YamlColumn> relationAtt = relationColumn.get(relation.getRelationName());

                    Set<ModelSchemaConfigRsp.RelationAttr> attrs = new HashSet<>();

                    for(YamlColumn column : relationAtt){

                        ModelSchemaConfigRsp.RelationAttr relationAttr = new ModelSchemaConfigRsp.RelationAttr();
                        relationAttr.setName(column.getAttrName());
                        relationAttr.setDataType(attDataTypeMap.get(column.getType()));

                        attrs.add(relationAttr);
                    }
                    relationBean.setAttrs(attrs);

                }
                relationBeans.add(relationBean);
            }

            ModelSchemaConfigRsp modelSchemaConfigRsp = new ModelSchemaConfigRsp();
            modelSchemaConfigRsp.setEntity(entity);
            modelSchemaConfigRsp.setAttr(attrBeans);
            modelSchemaConfigRsp.setRelation(relationBeans);
            modelSchemaConfigRsp.setTableName(tableName);
            rsList.add(modelSchemaConfigRsp);
        }

        return rsList;
    }
}
