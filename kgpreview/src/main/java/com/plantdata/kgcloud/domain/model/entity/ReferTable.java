package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/7/30
 */
@Data
@ApiModel
public class ReferTable {

    /** 关联表名 */
    private String tableName;
    /** 关联字段 */
    private String referField;
    /** 关联表概念名称 */
    private String conceptName;
    private Long conceptId;
}
