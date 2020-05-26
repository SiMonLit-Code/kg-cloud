package com.plantdata.kgcloud.domain.graph.source.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-23 21:46
 **/

@Data
public class GraphSourceRsp {

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("真实来源")
    private List<String> trueSource;

    @ApiModelProperty("操作者")
    private String username;

    @ApiModelProperty("动作")
    private String action;
}
