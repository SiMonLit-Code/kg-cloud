package com.plantdata.kgcloud.sdk.req.edit;

import com.plantdata.kgcloud.sdk.rsp.edit.EntityModifyReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 13:47
 * @Description:
 */
@Data
@ApiModel("概念或实体修改模型")
public class BasicInfoModifyReq extends EntityModifyReq {

    @ApiModelProperty(value = "唯一标示")
    private String key;
}
