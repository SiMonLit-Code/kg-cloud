package com.plantdata.kgcloud.domain.edit.req.file;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author EYE
 */
@Data
@ApiModel("实体文件关联查询")
public class EntityFileRelationQueryReq extends BaseReq {

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "标引类型")
    private Integer indexType;

    @ApiModelProperty(value = "是否关联实体(0:全部，1：关联，2：不关联)")
    private Integer isRelatedEntity;

}
