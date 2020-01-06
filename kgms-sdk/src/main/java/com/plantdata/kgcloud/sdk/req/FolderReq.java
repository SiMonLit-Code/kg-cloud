package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 10:10
 **/
@Data
public class FolderReq {
    @NotEmpty
    @ApiModelProperty("文件夹名称")
    private String folderName;
}
