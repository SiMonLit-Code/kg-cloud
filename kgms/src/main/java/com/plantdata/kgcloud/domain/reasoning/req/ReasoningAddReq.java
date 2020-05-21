package com.plantdata.kgcloud.domain.reasoning.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 16:55
 **/
@Data
public class ReasoningAddReq {


    @NotNull
    @NotBlank
    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("推理配置")
    private String config;

    @NotNull
    @NotBlank
    @ApiModelProperty("推理名称")
    private String name;

}
