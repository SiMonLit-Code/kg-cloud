package com.plantdata.kgcloud.domain.dw.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-01 17:35
 **/
public class ExampleTagJson {


    public static byte[] create(List<DWTableRsp> tableRspList){

        JSONArray rs = new JSONArray();
        for(DWTableRsp tableRsp : tableRspList){

            JSONObject tagJson = new JSONObject();
            tagJson.put("entity",new ArrayList<>());

            JSONObject attr = new JSONObject();
            attr.put("domain","");
            attr.put("name","");
            attr.put("dataType","");
            tagJson.put("attr", Lists.newArrayList(attr));

            JSONObject range = new JSONObject();
            range.put("domain","");
            range.put("name","");
            range.put("range",new ArrayList<>());
            JSONObject relationAttr = new JSONObject();
            relationAttr.put("name","");
            relationAttr.put("dataType","");
            range.put("attrs",Lists.newArrayList(relationAttr));

            tagJson.put("relation", Lists.newArrayList(range));

            tagJson.put("tableName",tableRsp.getTableName());

            rs.add(tagJson);

        }

        return rs.toJSONString().getBytes();
    }

}
