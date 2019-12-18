package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 19:05
 **/
@Data
public class WordRsp {
    private String id;
    @ApiModelProperty("词条名称")
    private String name;
    @ApiModelProperty("词条同义")
    private List<String> syns;
    @ApiModelProperty("词条类型")
    private String nature;
}
