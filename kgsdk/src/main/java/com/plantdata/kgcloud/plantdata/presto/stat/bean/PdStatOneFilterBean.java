package com.plantdata.kgcloud.plantdata.presto.stat.bean;

import java.util.List;

public class PdStatOneFilterBean {

    private PdStatBaseBean.FilterBoolEnum bool;
    private PdStatBaseBean.FilterOperEnum oper;
    private List<Object> values;

    public PdStatBaseBean.FilterBoolEnum getBool() {
        return bool;
    }

    public void setBool(PdStatBaseBean.FilterBoolEnum bool) {
        this.bool = bool;
    }

    public PdStatBaseBean.FilterOperEnum getOper() {
        return oper;
    }

    public void setOper(PdStatBaseBean.FilterOperEnum oper) {
        this.oper = oper;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
