package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:53
 **/
@Data
public class AnnotationConf {

    @ApiModelProperty("数据集Id")
    private String key;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源 0 概念 1 实体")
    private Integer source;

    @ApiModelProperty("实体的概念id")
    private List<Long> classId;

    @ApiModelProperty("实体的 选中概念id")
    private Long classIdSelected;
}
