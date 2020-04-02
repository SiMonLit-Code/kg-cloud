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
