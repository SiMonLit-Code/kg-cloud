package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:46
 */
@Data
@ApiModel("创建文件系统名称参数")
public class FileSystemNameReq {

    @ApiModelProperty("名称")
    private String name;

}
