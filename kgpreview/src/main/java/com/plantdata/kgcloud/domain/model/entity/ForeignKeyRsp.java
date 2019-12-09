package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/7/5
 */
@Data
@ApiModel
public class ForeignKeyRsp {

    /** 外键字段 */
    private String fieldName;
    /** 关联的表名称 */
    private String referTbName;
    /** 关联的表字段名称 */
    private String referFieldName;
}
