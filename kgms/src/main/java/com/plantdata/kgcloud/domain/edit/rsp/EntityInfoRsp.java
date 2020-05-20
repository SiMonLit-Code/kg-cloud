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
@ApiModel("实体关联信息")
public class EntityInfoRsp {

    @ApiModelProperty("实体ID")
    private Long entityId;

    @ApiModelProperty("实体名称")
    private String entityName;

}
