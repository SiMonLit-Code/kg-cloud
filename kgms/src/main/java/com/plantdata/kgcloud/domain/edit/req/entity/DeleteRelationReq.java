package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 14:26
 * @Description:
 */
@Data
@ApiModel("删除关系模型")
public class DeleteRelationReq {

    @ApiModelProperty(value = "关系id")
    private String tripleId;

}
