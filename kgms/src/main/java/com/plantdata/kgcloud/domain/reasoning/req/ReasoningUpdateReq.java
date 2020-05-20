package com.plantdata.kgcloud.domain.reasoning.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 16:56
 **/
@Data
public class ReasoningUpdateReq {

    @NotNull
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("推理配置")
    private String config;

    @ApiModelProperty("推理名称")
    private String name;
}
