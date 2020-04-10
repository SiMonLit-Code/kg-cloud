package com.plantdata.kgcloud.domain.dw.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.util.Date;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-01 17:35
 **/
public class ExampleYaml {

    private final static String JSON_START = "{";
    private final static String ARRAY_START = "[";
    private final static String ARRAY_STRING_START = "[\"";
    private final static String LONG = "bigint";
    private final static String INT = "int";
    private final static String VARCHAR = "varchar";
    private final static String CHAR = "char";
    private final static String TEXT = "text";
    private final static String LONGTEXT = "longtext";
    private final static String DATETIME = "datetime";
    private final static String SHORT = "tinyint";
    private final static String DATE = "date";
    private final static String TIMESTAMP = "timestamp";



    public static byte[] create(List<DWTableRsp> tableRspList){

        StringBuilder yaml = new StringBuilder();

        yaml.append("/*根据当前数据表以及字段生成的打标文件；\n" +
                "tables：当前数仓所拥有的表及表中文名；\n" +
                "relation：关系表有此字段，格式“-概念名>关系名>概念名”；\n" +
                "columns：数据表中的字段\n" +
                "columns.tag：该字段表示数值属性，格式“模式名.属性名”；\n" +
                "columns.type：该字段的属性值类型，包括“string”、“float”、“int”；\n" +
                "columns.explain：字段描述；\n" +
                "*/\n");

        yaml.append("tables:").append("\r\n");

        addTables(yaml,tableRspList);

        for(DWTableRsp tableRsp : tableRspList){
            addTableColumns(yaml,tableRsp);
        }

        return yaml.toString().getBytes();
    }

    private static void addTableColumns(StringBuilder yaml, DWTableRsp tableRsp) {
        yaml.append(tableRsp.getTableName()).append(":").append("\r\n");
        yaml.append(" relation:").append("\r\n");
        yaml.append(" columns:").append("\r\n");

        List<String> fields = tableRsp.getFields();
        if(fields != null && !fields.isEmpty()){
            for(String field : fields){
                yaml.append("    - ").append(field).append(": { tag:   , type: string }").append("\r\n");
            }
        }

    }

    private static void addTables(StringBuilder yaml, List<DWTableRsp> tableRspList) {
        for(DWTableRsp tableRsp : tableRspList){
            yaml.append(" - ").append(tableRsp.getTableName()).append(": ").append("\r\n");
        }
    }


    public static FieldType readType(Object val) {
        FieldType type;
        String string = val.toString();
        if (string.startsWith(JSON_START)) {
            try {
                JacksonUtils.getInstance().readValue(string, ObjectNode.class);
                type = FieldType.OBJECT;
            } catch (Exception e) {
                type = FieldType.STRING;
            }
        } else if (string.startsWith(ARRAY_START)) {
            if (string.startsWith(ARRAY_STRING_START)) {
                try {
                    JacksonUtils.getInstance().readValue(string, new TypeReference<List<String>>() {
                    });
                    type = FieldType.STRING_ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            } else {
                try {
                    JacksonUtils.getInstance().readValue(string, ArrayNode.class);
                    type = FieldType.ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            }
        } else if (val instanceof Integer) {
            type = FieldType.INTEGER;
        } else if (val instanceof Long) {
            type = FieldType.LONG;
        } else if (val instanceof Date) {
            type = FieldType.DATE;
        } else if (val instanceof Double) {
            type = FieldType.DOUBLE;
        } else if (val instanceof Float) {
            type = FieldType.FLOAT;
        } else {
            type = FieldType.STRING;
        }
        return type;
    }

    public static FieldType readMysqlType(String string) {
        FieldType type;
        if (string.startsWith(LONG)) {
            type = FieldType.LONG;
        } else if (string.startsWith(INT)) {
            type = FieldType.INTEGER;
        } else if (string.startsWith(VARCHAR)) {
            try {
                Integer size = Integer.parseInt(string.substring(string.indexOf("(")+1,string.lastIndexOf(")")));
                if(size > 50){
                    type = FieldType.TEXT;
                }else{
                    type = FieldType.STRING;
                }
            }catch (Exception e){
                type = FieldType.STRING;
            }
        } else if (string.startsWith(CHAR)) {
            try {
                Integer size = Integer.parseInt(string.substring(string.indexOf("(")+1,string.lastIndexOf(")")));
                if(size > 50){
                    type = FieldType.TEXT;
                }else{
                    type = FieldType.STRING;
                }
            }catch (Exception e){
                type = FieldType.STRING;
            }
        } else if (string.startsWith(TEXT)) {
            type = FieldType.TEXT;
        } else if (string.startsWith(LONGTEXT)) {
            type = FieldType.TEXT;
        } else if (string.startsWith(DATETIME)) {
            type = FieldType.DATE;
        } else if (string.startsWith(SHORT)) {
            type = FieldType.INTEGER;
        } else if (string.startsWith(DATE)) {
            type = FieldType.DATE;
        } else if (string.startsWith(TIMESTAMP)) {
            type = FieldType.DATE;
        } else {
            type = FieldType.STRING;
        }
        return type;
    }
}
