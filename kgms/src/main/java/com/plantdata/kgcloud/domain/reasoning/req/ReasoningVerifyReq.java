package com.plantdata.kgcloud.domain.reasoning.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 17:07
 **/
@Data
public class ReasoningVerifyReq {

    @ApiModelProperty("推理规则id")
    private Integer id;

    @ApiModelProperty("实体id")
    private Long entityId;
}
