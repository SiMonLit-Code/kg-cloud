package com.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lp
 */
@Data
@ApiModel("实体文件关联插入实体")
public class EntityFileRelationReq {

    @NotNull(message = "实体id不能为空")
    @ApiModelProperty(required = true, value = "实体id")
    private Long entityId;

    @NotNull(message = "数仓文件ID不能为空")
    @ApiModelProperty("数仓文件ID")
    private String dwFileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

}
