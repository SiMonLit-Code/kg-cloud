package com.plantdata.kgcloud.domain.model.entity;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/7/5
 */
@Data
@ApiModel
public class FieldInfoRsp {

    /** 字段名称 */
    private String name;
    /** 字段描述 */
    private String comment;
    /** 数据类型 */
    private String type;

    /** 属性类型 */
    private Integer attrType;

    List<RangeTableRsp> values;

    public List<RangeTableRsp> getValues() {
        return values == null ? Lists.newArrayList() : values;
    }
}
