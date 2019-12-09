package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel
public class TableSchemaRsp {

    /** 表名 */
    private String tableName;
    /** 表描述 */
    private String tableComment;
    /** 表类型 0实体表 1关系表 */
    private Integer tableType = 0;
    /** 默认主键字段 */
    private String idField;
    /** 表字段信息 */
    private List<FieldInfoRsp> fieldInfoLs;
    /** 关联外键信息 */
    private List<ForeignKeyRsp> foreignKeyLs;

}
