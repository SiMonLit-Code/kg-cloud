package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author jiangdeming
 * @date 2019/12/25  15:17
 */
@ApiModel("图统计更新")
@Data
public class UpdateGraphConfStatisticalReq extends GraphConfStatisticalReq {

    @ApiModelProperty(value = "id",required = true)
    @ApiParam(name = "id",required = true)
    private Long id;
}
