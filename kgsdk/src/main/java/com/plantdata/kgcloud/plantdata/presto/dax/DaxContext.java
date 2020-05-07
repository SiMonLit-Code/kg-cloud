package com.plantdata.kgcloud.plantdata.presto.dax;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class DaxContext {

    public String dax;
    public String sql;
    public String origin_dax;

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
