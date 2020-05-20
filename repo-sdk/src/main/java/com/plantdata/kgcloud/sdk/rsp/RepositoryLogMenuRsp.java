package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/20  10:09
 */
@Getter
@Setter
@ApiModel("组件菜单检测结果")
public class RepositoryLogMenuRsp {
    @ApiModelProperty("菜单id")
    private int menuId;
    @ApiModelProperty("是否可用")
    private boolean available;
}
