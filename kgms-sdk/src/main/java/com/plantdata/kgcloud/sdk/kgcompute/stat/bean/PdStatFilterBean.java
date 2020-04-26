package com.plantdata.kgcloud.sdk.kgcompute.stat.bean;

import java.util.List;

public class PdStatFilterBean extends PdStatBaseBean {

    private List<PdStatOneFilterBean> filter;

    public List<PdStatOneFilterBean> getFilter() {
        return filter;
    }

    public void setFilter(List<PdStatOneFilterBean> filter) {
        this.filter = filter;
    }

}



