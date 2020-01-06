package com.plantdata.kgcloud.domain.audit.rsp;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public class ApiAuditUrlRsp {

    private List<String> horizontal;
    private List<VerticalData> vertical;

    public ApiAuditUrlRsp(List<String> horizontal, List<VerticalData> vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public List<String> getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(List<String> horizontal) {
        this.horizontal = horizontal;
    }

    public List<VerticalData> getVertical() {
        return vertical;
    }

    public void setVertical(List<VerticalData> vertical) {
        this.vertical = vertical;
    }

    public static class VerticalData {
        private String name;
        private List<Long> val;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Long> getVal() {
            return val;
        }

        public void setVal(List<Long> val) {
            this.val = val;
        }
    }

}
