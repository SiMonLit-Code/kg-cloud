package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author EYE
 */
@Data
@Builder
@ApiModel("实体文件关联")
public class EntityFileRelationRsp {

    private String id;

    @ApiModelProperty("图谱名称")
    private String kgName;

    @ApiModelProperty("实体ID")
    private Long entityId;

    @ApiModelProperty("实体名称")
    private String entityName;

    @ApiModelProperty("数仓文件ID")
    private String dwFileId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
