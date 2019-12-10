package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 13:18
 */
@ApiModel("业务规则图探索")
@Getter
@Setter
public class ExploreByKgQlReq {
    @ApiModelProperty("实体id")
    @NotNull
    private Long entityId;
    @ApiModelProperty("kgQl语句")
    @NotEmpty
    private String kgQl;
    @ApiModelProperty("关系是否合并")
    private boolean relationMerge;
}
