package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:41
 * @Description:
 */
@Data
@ApiModel("缩略图路径模型")
public class ThumbPathRsp extends FilePathRsp {

    @ApiModelProperty(value = "图片带缩略图的路径")
    private String thumbPath;
}
