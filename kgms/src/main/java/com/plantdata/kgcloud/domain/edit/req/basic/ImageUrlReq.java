package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/22 18:03
 * @Description:
 */
@Data
@ApiModel("保存图片路径模型")
public class ImageUrlReq {

    @ApiModelProperty(required = true)
    @NotNull
    @Min(value = 1, message = "不能修改图谱")
    private Long id;

    @ApiModelProperty(value = "图片路径")
    @Length(max = 1000,message = "图片路径不能超过1000")
    private String imageUrl;

}
