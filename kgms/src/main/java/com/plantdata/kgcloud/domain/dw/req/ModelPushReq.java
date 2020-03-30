package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-26 12:52
 **/
@Data
public class ModelPushReq {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("行业")
    private String modelType;

    @ApiModelProperty("图谱名")
    private String kgName;
}
