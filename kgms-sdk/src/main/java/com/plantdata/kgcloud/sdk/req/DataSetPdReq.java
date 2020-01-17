package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:38
 **/
@Data
@ApiModel
public class DataSetPdReq {

    @ApiModelProperty("文件夹id")
    private Long folderId;

    @ApiModelProperty("标题")
    @Length(min = 1, max = 20, message = "标题长度必须在1-20之间")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("唯一标识")
    @NotBlank(message = "唯一标识不能为空")
    @Length(min = 4, max = 20, message = "唯一标识长度必须在4-20之间")
    @Pattern(regexp = "^[a-z]\\w{1,19}$", message = "唯一标识必须以小写字母开头，可由字母数字下划线组成")
    private String key;

    @ApiModelProperty("文本数据集id")
    @NotNull(message = "文本数据集id不能为空")
    private Long pdId;

//    @ApiModelProperty(hidden = true, value = "数据集类型")
//    private Integer dataType = 3;

//    @Valid
//    @ApiModelProperty
//    @Size(min = 1, max = 50, message = "数据集最多只支持50个字段")
//    private List<DataSetSchema> schema;
}
