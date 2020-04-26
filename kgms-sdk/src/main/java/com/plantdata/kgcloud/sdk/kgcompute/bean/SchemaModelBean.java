package com.plantdata.kgcloud.sdk.kgcompute.bean;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.kgcompute.graph.Graph;
import com.plantdata.kgcloud.sdk.kgcompute.graph.Vertex;

import java.util.Arrays;
import java.util.Map;

public class SchemaModelBean {

    private Map<String, String> conceptSchema = Maps.newHashMap();
    private Map<String, String> attrSchema = Maps.newHashMap();
    private Graph relationSchema = new Graph();
    private Map<String, String> relationLookup = Maps.newHashMap();

    public SchemaModelBean(){

        //概念 建模表 server.database.table
        conceptSchema.put("交易记录","mysql_145.cmdb.t_trans_log");
        conceptSchema.put("设备","mysql_145.cmdb.t_equipment");
        conceptSchema.put("数据中心","mysql_145.cmdb.t_dc");
        conceptSchema.put("地区","mysql_145.cmdb.t_dc");
        conceptSchema.put("会员","mysql_145.cmdb.t_relation");
        conceptSchema.put("关联记录","mysql_145.cmdb.t_relation");

        //属性 建模字段 server.database.table.column
        attrSchema.put("交易记录[名称]","mysql_145.cmdb.t_trans_log.name");
        attrSchema.put("交易记录[席位]","mysql_145.cmdb.t_trans_log.seat");
        attrSchema.put("交易记录[买入量]","mysql_145.cmdb.t_trans_log.buy_num");
        attrSchema.put("交易记录[卖出量]","mysql_145.cmdb.t_trans_log.sale_num");
        attrSchema.put("交易记录[委托买入量]","mysql_145.cmdb.t_trans_log.trusmysql_145.cmdb.t_buy_num");
        attrSchema.put("交易记录[委托卖出量]","mysql_145.cmdb.t_trans_log.trustSale_num");
        attrSchema.put("交易记录[交易价格]","mysql_145.cmdb.t_trans_log.deal_price");
        attrSchema.put("交易记录[交易时间]","mysql_145.cmdb.t_trans_log.deal_time");

        attrSchema.put("设备[名称]","mysql_145.cmdb.t_equipment.equipment");
        attrSchema.put("设备[类型]","mysql_145.cmdb.t_equipment.type");

        attrSchema.put("数据中心[名称]","mysql_145.cmdb.t_dc.dc");
        attrSchema.put("数据中心[地点]","mysql_145.cmdb.t_dc.locality");
        attrSchema.put("数据中心[类型]","mysql_145.cmdb.t_dc.type");

        attrSchema.put("地区[名称]","mysql_145.cmdb.t_dc.locality");

        attrSchema.put("会员[名称]","mysql_145.cmdb.t_relation.user");

        attrSchema.put("关联记录[系统]","mysql_145.cmdb.t_relation.system");
        attrSchema.put("关联记录[会员]","mysql_145.cmdb.t_relation.user");
        attrSchema.put("关联记录[数据中心]","mysql_145.cmdb.t_relation.dc");
        attrSchema.put("关联记录[机房]","mysql_145.cmdb.t_relation.idc");
        attrSchema.put("关联记录[机架]","mysql_145.cmdb.t_relation.rack");
        attrSchema.put("关联记录[设备]","mysql_145.cmdb.t_relation.equipment");
        attrSchema.put("关联记录[席位]","mysql_145.cmdb.t_relation.seat");

        //关系(对象属性) 建模主外键 邻接表
        relationSchema.addVertex("交易记录", Arrays.asList(new Vertex("关联记录")));
        relationSchema.addVertex("数据中心", Arrays.asList(new Vertex("关联记录"),new Vertex("地区")));
        relationSchema.addVertex("设备", Arrays.asList(new Vertex("关联记录")));
        relationSchema.addVertex("会员", Arrays.asList(new Vertex("关联记录")));
        relationSchema.addVertex("地区", Arrays.asList(new Vertex("数据中心")));
        relationSchema.addVertex("关联记录", Arrays.asList(new Vertex("交易记录"),new Vertex("数据中心"),new Vertex("设备"),new Vertex("会员")));

        //关系(对象属性) 建模主外键 server.database.table.column = server.database.table.column
        relationLookup.put("交易记录-关联记录","交易记录[席位]=关联记录[席位]");
        relationLookup.put("数据中心-关联记录","数据中心[名称]=关联记录[数据中心]");
        relationLookup.put("设备-关联记录","设备[名称]=关联记录[设备]");
        relationLookup.put("关联记录-交易记录","交易记录[席位]=关联记录[席位]");
        relationLookup.put("关联记录-数据中心","数据中心[名称]=关联记录[数据中心]");
        relationLookup.put("关联记录-设备","设备[名称]=关联记录[设备]");

        System.out.println("inited..");
    }

    public Map<String, String> getConceptSchema() {
        return conceptSchema;
    }

    public void setConceptSchema(Map<String, String> conceptSchema) {
        this.conceptSchema = conceptSchema;
    }

    public Map<String, String> getAttrSchema() {
        return attrSchema;
    }

    public void setAttrSchema(Map<String, String> attrSchema) {
        this.attrSchema = attrSchema;
    }

    public Graph getRelationSchema() {
        return relationSchema;
    }

    public void setRelationSchema(Graph relationSchema) {
        this.relationSchema = relationSchema;
    }

    public Map<String, String> getRelationLookup() {
        return relationLookup;
    }

    public void setRelationLookup(Map<String, String> relationLookup) {
        this.relationLookup = relationLookup;
    }
}
