package com.plantdata.kgcloud.plantdata.presto.stat.bean;

import java.util.List;

public class PdStatBean {

    private List<PdStatBaseBean> dimensions;
    private List<PdStatBaseBean> measures;
    private List<PdStatFilterBean> filters;
    private List<PdStatOrderBean> orders;
    private int limit = 100;

    public List<PdStatBaseBean> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<PdStatBaseBean> dimensions) {
        this.dimensions = dimensions;
    }

    public List<PdStatBaseBean> getMeasures() {
        return measures;
    }

    public void setMeasures(List<PdStatBaseBean> measures) {
        this.measures = measures;
    }

    public List<PdStatFilterBean> getFilters() {
        return filters;
    }

    public void setFilters(List<PdStatFilterBean> filters) {
        this.filters = filters;
    }

    public List<PdStatOrderBean> getOrders() {
        return orders;
    }

    public void setOrders(List<PdStatOrderBean> orders) {
        this.orders = orders;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
