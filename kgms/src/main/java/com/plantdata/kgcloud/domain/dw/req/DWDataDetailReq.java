package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "数仓信息展示")
public class DWDataDetailReq  {
    @ApiModelProperty("创建时间")
    @NotBlank(message = "日期不能为空")
    private String logTimeStamp;
    @ApiModelProperty("数据表名")
    @NotBlank(message = "数据表名不能为空")
    private String tableName;
}
