package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:58
 **/
@Data
public class AnnotationCreateReq {

    @NotNull
    @ApiModelProperty("数据集Id")
    private Long dataId;

    @ApiModelProperty("任务名称")
    @NotBlank
    @Length(min = 1,max = 50 ,message = "任务名称不能超过50个字段")
    private String name;

    @ApiModelProperty("配置")
    private List<AnnotationConf> config;

    @ApiModelProperty("描述")
    private String description;


}
