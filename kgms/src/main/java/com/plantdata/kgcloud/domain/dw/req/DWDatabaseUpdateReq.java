package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@ApiModel("数仓表数据更改")
public class DWDatabaseUpdateReq {
    @NotBlank(message = "数仓id不能为空")
    @ApiModelProperty("数仓id")
    private Long dataBaseId;
    @NotBlank(message = "表id不能为空")
    @ApiModelProperty("表id")
    private Long tableId;
    @NotBlank(message = "数据id不能为空")
    @ApiModelProperty("数据id")
    private String id;
    @ApiModelProperty("数据")
    private Map<String, Object> data;
}
