package com.plantdata.kgcloud.domain.prebuilder.req;

import com.plantdata.kgcloud.domain.prebuilder.aop.DefaultHandlerReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-24 22:05
 **/
@Data
public class PreBuilderUpdateReq extends DefaultHandlerReq {

    @ApiModelProperty("行业类别")
    private String modelType;

    @ApiModelProperty("模式名称")
    private String name;

    @ApiModelProperty("描述")
    private String desc;


    @ApiModelProperty("数据id")
    private Integer id;
}
