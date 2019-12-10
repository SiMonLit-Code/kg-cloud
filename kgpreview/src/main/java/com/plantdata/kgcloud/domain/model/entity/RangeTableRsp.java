package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/7/16
 */
@Data
@ApiModel
public class RangeTableRsp {

    /** 值域表 */
    private String tableName;
    /** 值域表描述 */
    private String tableComment;
    /** 值域字段 */
    private String fieldName;
    /** 值域字段描述 */
    private String fieldComment;
}
