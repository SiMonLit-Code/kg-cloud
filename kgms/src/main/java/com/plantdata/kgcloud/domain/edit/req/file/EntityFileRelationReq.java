package com.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 */
@Data
@ApiModel("实体文件关联插入实体")
public class EntityFileRelationReq {

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("文件ID")
    private String fileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

}
