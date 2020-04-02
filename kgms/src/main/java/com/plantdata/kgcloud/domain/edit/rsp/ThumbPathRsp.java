package com.plantdata.kgcloud.domain.edit.rsp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:41
 * @Description:
 */
@Setter
@Getter
@ApiModel("缩略图路径模型")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThumbPathRsp extends FilePathRsp {

    @ApiModelProperty(value = "图片带缩略图的路径")
    private String thumbPath;
}
