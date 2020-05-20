package com.plantdata.kgcloud.domain.reasoning.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 16:51
 **/
@Data
@ApiModel("在线推理")
public class ReasoningRsp {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("推理配置")
    private String config;

    @ApiModelProperty("添加时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;

    @ApiModelProperty("推理名称")
    private String name;
}
