package com.plantdata.kgcloud.domain.d2r.entity;

/**
 * 属性映射单元Bean
 * Created by bin on 2018/6/7.
 */
public class RelMapperElementBean extends AttrMapperElementBean {

    // 目标值域
    private Long attrValueType;

    public Long getAttrValueType() {
        return attrValueType;
    }

    public void setAttrValueType(Long attrValueType) {
        this.attrValueType = attrValueType;
    }
}
