package com.plantdata.kgcloud.domain.d2r.entity;

import lombok.Data;

/**
 * 私有数值属性配置
 * @author xiezhenxiang 2019/11/21
 */
@Data
public class PrivateAttrBean {

    /** 私有属性名称映射字段 */
    private String keyField;
    /** 私有属性值映射字段 */
    private String valueField;
    /** 私有属性名称是否是常量 */
    private Integer keyIsConstant = 0;

    public PrivateAttrBean(String keyField, String valueField, Integer keyIsConstant) {
        this.keyField = keyField;
        this.valueField = valueField;
        this.keyIsConstant = keyIsConstant;
    }
}
