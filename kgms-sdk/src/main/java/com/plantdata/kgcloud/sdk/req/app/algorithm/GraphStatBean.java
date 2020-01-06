package com.plantdata.kgcloud.sdk.req.app.algorithm;

import java.util.ArrayList;
import java.util.List;

public class GraphStatBean {

    private String key;
    private Long type;
    private List<Integer> atts;
    private List<GraphStatDetailBean> rs;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<Integer> getAtts() {
        return atts;
    }

    public void setAtts(List<Integer> atts) {
        this.atts = atts;
    }

    public List<GraphStatDetailBean> getRs() {
        return rs;
    }

    public void addRs(Long id, Integer count) {
        if (this.rs == null) {
            rs = new ArrayList<>();
        }
        rs.add(new GraphStatDetailBean(id, count));
    }

    class GraphStatDetailBean {

        Long id;
        Integer count;

        public GraphStatDetailBean(Long id, Integer count) {
            this.id = id;
            this.count = count;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

    }
}
