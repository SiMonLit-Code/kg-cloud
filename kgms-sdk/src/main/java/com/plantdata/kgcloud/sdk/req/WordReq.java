package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 16:56
 **/
@Data
public class WordReq {
    @NotEmpty
    @ApiModelProperty("词条名称")
    private String name;

    @NotNull
    @ApiModelProperty("词条同义")
    private List<String> syns;

    @NotEmpty
    @ApiModelProperty("词条类型 n 名称 v 动词 a 形容词 d 副词 p 介词 e 叹词 t 时间词")
    private String nature;

}
