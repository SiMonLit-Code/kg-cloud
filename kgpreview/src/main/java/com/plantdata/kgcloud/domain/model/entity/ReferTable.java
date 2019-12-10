package com.plantdata.kgcloud.domain.model.entity;

import lombok.Data;

/**
 * @author xiezhenxiang 2019/7/30
 */
@Data
public class ReferTable {

    /** 关联表名 */
    private String tableName;
    /** 关联字段 */
    private String referField;
    /** 关联表概念名称 */
    private String conceptName;
    private Long conceptId;
}
