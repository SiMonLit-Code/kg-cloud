package com.plantdata.kgcloud.sdk.req.app.explore.common;

import com.plantdata.kgcloud.sdk.validator.ListLengthCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:57
 */
@Getter
@Setter
@ApiModel("关联分析-参数(通用/必填)")
public class CommonRelationReq {
    @ApiModelProperty(value = "分析实例列表，json数组格式", required = true)
    @NotNull
    @ListLengthCheck(min = 2)
    private List<Long> ids;
}
