package com.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 14:14
 * @Description:
 */
@Data
@ApiModel("根据meta删除关系模型")
public class RelationMetaReq {

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;
}
