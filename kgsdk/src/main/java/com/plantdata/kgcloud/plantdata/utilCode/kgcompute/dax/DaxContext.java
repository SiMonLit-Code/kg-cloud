package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.dax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class DaxContext {

    public String dax;
    public String sql;
    public String origin_dax;

    public List<String> sql_select = Lists.newArrayList();
    public List<String> sql_select_alias = Lists.newArrayList();
    public List<String> sql_where = Lists.newArrayList();
    public List<String> sql_groupby = Lists.newArrayList();
    public List<String> sql_groupby_alias = Lists.newArrayList();
    public List<String> sql_join = Lists.newArrayList();
    public Set<String> sql_tables = Sets.newHashSet();

    public List<String> filter_exp = Lists.newArrayList();
    public List<String> groupby_exp = Lists.newArrayList();
    public List<String> compute_exp = Lists.newArrayList();
    public List<String> computex_exp = Lists.newArrayList();
    public String summerize_exp = "";
    public List<String> select_exp = Lists.newArrayList();
    public Set<String> concepts = Sets.newHashSet();

    public DaxContext(String dax){
        dax = dax.toUpperCase().replaceAll(" ","");
        dax = dax.replaceAll("\"","'");
        dax = dax.replaceAll("“，","'");
        dax = dax.replaceAll("”","'");
        dax = dax.replaceAll("，",",");
        this.dax = dax;
        this.origin_dax = dax;
    }
}
