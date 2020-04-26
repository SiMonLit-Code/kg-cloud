package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.dax;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.bean.SchemaModelBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxParser {

    private SchemaModelBean smb = new SchemaModelBean();
    private DaxSqlGenerator dsg = new DaxSqlGenerator();

    //SUMX COUNTAX
    private String compute_x_regex = "(SUMX|COUNTX|MAXX|MINX|AVERAGEX)[(](.*?)[)]";
    private String compute_x_summerize_regex = "\\'(.*?)\\',(SUMX|COUNTX|MAXX|MINX|AVERAGEX)[(](.*?)[)]";
    private String compute_x_alias_regex = "\\\'(.*?)\\\',(SUMX|COUNTX|MAXX|MINX|AVERAGEX)";

    //SUM COUNT
    private String compute_regex = "(SUM|COUNT|MAX|MIN|AVERAGE)[(](.*?)[)]";
    private String compute_summerize_regex = "\\'(.*?)\\',(SUM|COUNT|MAX|MIN|AVERAGE)[(](.*?)[)]";
    private String compute_alias_regex = "\\\'(.*?)\\\',(SUM|COUNT|MAX|MIN|AVERAGE)";

    //FILTER
    private String filtr_regex = "(FILTER)[(](.*?)[)]";

    //SUMMARIZE
    private String summerize_regex = "(SUMMERIZE)[(](.*?)[)]";

    //提取 概念[属性] 仅支持中文
    private String concept_attr_regex = "[\\u4E00-\\u9FFF]+\\[[\\u4E00-\\u9FFF]+\\]";

    private String select_regex = "(SELECTCOLUMNS)[(](.*?)[)]";

    private boolean hasSummerize = false;

    public DaxContext parse(String dax){

        DaxContext dc = new DaxContext(dax);

        if(!validate(dc)){
            return  dc;
        }

        this.hasSummerize = dc.origin_dax.startsWith("SUMMERIZE");

//        transDax2Exp(dc);

        filterParser(dc);

        computeXParer(dc);

        computeParser(dc);

        summarizeParser(dc);

//        selectParser(dc);

        multiTableParser(dc);

        System.out.println(JSON.toJSON(dc));

        dsg.generateSQL(dc);

        return dc;

    }

    private boolean validate(DaxContext dc){

        if(dc.dax==null || dc.dax==""){
            return false;
        }

        return true;
    }

    private boolean transDax2Exp(DaxContext dc){

//        Matcher select_regex_matcher = Pattern.compile(select_regex).matcher(dc.dax);
//        if(select_regex_matcher.find()){
//            dc.select_exp = select_regex_matcher.group();
//            dc.dax = dc.dax.replace(dc.select_exp,"");
//        }

        return true;
    }

    public void filterParser(DaxContext dc){

        //从dax里找filter 可能多个
        Matcher filtr_regex_matcher = Pattern.compile(filtr_regex).matcher(dc.dax);
        while (filtr_regex_matcher.find()){
            String exp = filtr_regex_matcher.group();
            dc.filter_exp.add(exp);
            dc.dax = dc.dax.replace(exp,"");
        }

        if(dc.filter_exp.size()==0){
            return;
        }

        //目前只能将不同算子的filter展平
        for (String for_exp : dc.filter_exp) {
            String c_exp = for_exp;
            c_exp = c_exp.replace("FILTER","");
            c_exp = c_exp.substring(1,c_exp.length()-1).trim();
            String exp = extract_concept_attr_in_dax(c_exp, dc);
            dc.sql_where.add(exp);
        }
    }

    private void computeXParer(DaxContext dc){

        String cr = compute_x_regex;

        if(this.hasSummerize){
            cr = compute_x_summerize_regex;
        }

        //从dax里找普通算子 可能多个
        Matcher compute_x_regex_matcher = Pattern.compile(cr).matcher(dc.dax);
        while(compute_x_regex_matcher.find()){
            String exp = compute_x_regex_matcher.group();
            dc.computex_exp.add(exp);
            dc.dax = dc.dax.replace(exp,"");
        }

        if(dc.computex_exp.size()==0){
            return;
        }

        for (String for_exp : dc.computex_exp) {

            String c_exp = for_exp;
            String a_exp = "";

            if(c_exp.contains("COUNTX")){
                c_exp = c_exp.replace("COUNTX","COUNT");
            }
            else if(c_exp.contains("SUMX")){
                c_exp = c_exp.replace("SUMX","SUM");
            }
            else if(c_exp.contains("AVERAGEX")){
                c_exp = c_exp.replace("AVERAGEX","AVG");
            }
            else if(c_exp.contains("MAXX")){
                c_exp = c_exp.replace("MAXX","MAX");
            }
            else if(c_exp.contains("MINX")){
                c_exp = c_exp.replace("MINX","MIN");
            }

            if(hasSummerize){

                a_exp = c_exp.split("',")[0];
                c_exp = c_exp.split("',")[1];

                a_exp = a_exp.replaceAll("'","");
                dc.sql_select_alias.add(a_exp);
            }
            else {
                dc.sql_select_alias.add("");
            }

            //转换成表名
            c_exp = c_exp.replaceAll(",","");
            c_exp = extract_concept_attr_in_dax(c_exp, dc);
            dc.sql_select.add(c_exp);
        }

    }

    private void computeParser(DaxContext dc){

        String cr = compute_regex;

        if(this.hasSummerize){
            cr = compute_summerize_regex;
        }

        //从dax里找普通算子 可能多个
        Matcher compute_regex_matcher = Pattern.compile(cr).matcher(dc.dax);
        while(compute_regex_matcher.find()){
            String exp = compute_regex_matcher.group();
            dc.compute_exp.add(exp);
            dc.dax = dc.dax.replace(exp,"");
        }

        if(dc.compute_exp.size()==0){
            return;
        }

        for (String for_exp : dc.compute_exp) {

            String c_exp = for_exp.replace("AVERAGE","AVG");
            String a_exp = "";

            if(hasSummerize){

                a_exp = c_exp.split("',")[0];
                c_exp = c_exp.split("',")[1];

                a_exp = a_exp.replaceAll("'","");
                dc.sql_select_alias.add(a_exp);
            }
            else {
                dc.sql_select_alias.add("");
            }

            //转换成表名
            c_exp = c_exp.replaceAll(",","");
            c_exp = extract_concept_attr_in_dax(c_exp, dc);
            dc.sql_select.add(c_exp);
        }

    }

    private void summarizeParser(DaxContext dc){

        if(!this.hasSummerize){
            return;
        }

        //仅有一个
        Matcher summerize_regex_matcher = Pattern.compile(summerize_regex).matcher(dc.dax);
        if(summerize_regex_matcher.find()){
            dc.summerize_exp = summerize_regex_matcher.group();
            dc.dax = dc.dax.replace(dc.summerize_exp,"");
        }

        dc.summerize_exp = dc.summerize_exp.replace("SUMMERIZE","").trim();
        dc.summerize_exp = dc.summerize_exp.replaceAll("'","").trim();
        dc.summerize_exp = dc.summerize_exp.substring(1,dc.summerize_exp.length()-1).trim();

        for (String s : dc.summerize_exp.split(",")) {
            s = s.trim();
            if(s.length()>0){
                String exp = extract_concept_attr_in_dax(s, dc);
                dc.sql_groupby.add(exp);
                dc.sql_groupby_alias.add(s);
            }
        }
    }

    public void selectParser(DaxContext dc){

//        if(dc.select_exp==""){
//            return;
//        }
//
//        dc.select_exp = dc.select_exp.replace("SELECTCOLUMNS","").trim();
//        dc.select_exp = dc.select_exp.substring(1,dc.select_exp.length()-1).trim();
//
//        for (String s : dc.select_exp.split(",")) {
//            s = s.trim();
//            if(s.length()>0){
//                String exp = extract_concept_attr_in_dax(s, dc);
//                dc.sql_select.add(exp);
//                dc.sql_select_alias.add(s);
//            }
//        }
    }

    private void multiTableParser(DaxContext dc){

        if(dc.sql_tables.size()<=1){
            return;
        }

        Set<String> edges = find_edges_bewteen_multi_nodes(dc.concepts);

        for (String edge : edges) {
            String exp = extract_concept_attr_in_dax(edge, dc);
            dc.sql_join.add(exp);
        }

    }

    private String extract_concept_attr_in_dax(String part_exp, DaxContext dc){

        while (true){
            Matcher concept_attr_regex_matcher = Pattern.compile(concept_attr_regex).matcher(part_exp);

            if(!concept_attr_regex_matcher.find()){
                return part_exp.trim();
            }

            String concept_attr = concept_attr_regex_matcher.group();
            String table_column_name = smb.getAttrSchema().get(concept_attr);

            while(part_exp.contains(concept_attr)){
                part_exp = part_exp.replace(concept_attr, table_column_name);
            }

            String concept = concept_attr.split("\\[")[0];
            String table_name = smb.getConceptSchema().get(concept);

            dc.concepts.add(concept);
            dc.sql_tables.add(table_name);
        }
    }

    private Set<String> find_edges_bewteen_multi_nodes(Set<String> concepts){

        Set<String> edges = Sets.newHashSet();
        List<String> target_nodes = new ArrayList<>();
        target_nodes.addAll(concepts);

        for(int i=0; i<target_nodes.size()-1; i++){

            String start = target_nodes.get(i);
            String finish = target_nodes.get(i+1);

            List<String> paths = smb.getRelationSchema().getShortestPath(start,finish);
            paths.add(start);

            System.out.println("paths:"+paths);

            if(paths!=null && paths.size()>1){

                for(int j=0; j<paths.size()-1; j++){

                    String from = paths.get(j);
                    String to = paths.get(j+1);

                    if(smb.getRelationLookup().get(from+"-"+to)!=null){
                        edges.add(smb.getRelationLookup().get(from+"-"+to));
                    }
                    if(smb.getRelationLookup().get(to+"-"+from)!=null){
                        edges.add(smb.getRelationLookup().get(to+"-"+from));
                    }
                }
            }
        }

        return edges;
    }

}
