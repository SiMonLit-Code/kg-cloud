package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:58
 **/
@Data
public class AnnotationReq {

    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("配置")
    private List<AnnotationConf> config;
    @ApiModelProperty("描述")
    private String description;
}
