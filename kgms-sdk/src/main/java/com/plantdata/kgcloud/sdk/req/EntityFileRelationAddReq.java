package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/21 20:34
 */
@ApiModel("文件标引参数")
@Data
public class EntityFileRelationAddReq {

    @ApiModelProperty("文件ID")
    private String fileId;

    @ApiModelProperty("实体ids")
    private List<Long> entityIds;
}
