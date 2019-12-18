package com.plantdata.kgcloud.domain.audit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:31
 **/
@Data
public class ApiAuditTopRsp {
    @ApiModelProperty("路径")
    private String name;

    @ApiModelProperty("数量")
    private Long value;

    @ApiModelProperty("成功率")
    private Double success;

    @ApiModelProperty("失败率")
    private Double fail;
}
