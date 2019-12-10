package com.plantdata.kgcloud.domain.d2r.entity;

import java.util.List;

/**
 * 映射单元
 * Created by bin on 2018/6/7.
 */
public class MapperElementBean {

    // 映射源字段
    private List<String> mapField;

    // 更新策略
    private Integer updateOpt;

    public List<String> getMapField() {
        return mapField;
    }

    public void setMapField(List<String> mapField) {
        this.mapField = mapField;
    }

    public Integer getUpdateOpt() {
        return updateOpt;
    }

    public void setUpdateOpt(Integer updateOpt) {
        this.updateOpt = updateOpt;
    }
}