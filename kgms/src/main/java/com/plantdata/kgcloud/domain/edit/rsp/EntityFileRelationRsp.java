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

    @ApiModelProperty("标引文件ID")
    private String dwFileId;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
