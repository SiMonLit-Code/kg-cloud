package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:30
 * @Description:
 */
@Data
@ApiModel("文件上传返回路径")
public class FilePathRsp {

    @ApiModelProperty(value = "路径")
    private String fullPath;
}
