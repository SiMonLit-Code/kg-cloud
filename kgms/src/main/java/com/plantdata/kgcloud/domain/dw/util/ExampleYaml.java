package com.plantdata.kgcloud.domain.dw.util;

import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-01 17:35
 **/
public class ExampleYaml {


    public static byte[] create(List<DWTableRsp> tableRspList){

        StringBuilder yaml = new StringBuilder();

        yaml.append("/*根据当前数据表以及字段生成的打标文件；\n" +
                "tables：当前数仓所拥有的表及表中文名；\n" +
                "relation：关系表有此字段，格式“-概念名>关系名>概念名”；\n" +
                "columns：数据表中的字段\n" +
                "columns.tag：该字段表示数值属性，格式“模式名.属性名”；\n" +
                "columns.type：该字段的属性值类型，包括“string”、“float”、“int”；\n" +
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
}
