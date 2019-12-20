package com.plantdata.kgcloud.sdk.req.app.explore.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:57
 */
@Getter
@Setter
@ApiModel("关联分析通用参数")
public class CommonRelationReq {
    @ApiModelProperty(value = "分析实例列表，json数组格式",required = true)
    @NotNull
    @Max.List(@Max(2))
    private List<Long> ids;
    @ApiModelProperty("查询层数")
    @Max(value = 30, message = "步长不可超过30步")
    @Min(value = 1, message = "步长最小为1")
    private Integer distance = 1;

}
