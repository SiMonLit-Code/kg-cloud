package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("数仓状态日志统计")
public class DWTableLogReq extends BaseReq {
    @ApiModelProperty("数据表名")
    @NotBlank(message = "数据表名不能为空")
    private String tableName;

    @ApiModelProperty("数据库名")
    @NotBlank(message = "数据库名不能为空")
    private String dataName;


}
