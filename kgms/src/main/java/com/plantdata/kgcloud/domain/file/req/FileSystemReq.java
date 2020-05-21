package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:46
 */
@Data
@ApiModel("创建修改文件系统参数")
public class FileSystemReq {

    @ApiModelProperty("文件系统id")
    private Long fileSystemId;

    @ApiModelProperty("名称")
    private String name;

}
