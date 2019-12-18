package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 10:12
 **/
@Data
public class FolderRsp {
    private Long id;

    private String userId;

    @ApiModelProperty("文件夹名称")
    private String folderName;

    @ApiModelProperty("是否默认")
    private Boolean defaulted;

    private Date createAt;

    private Date updateAt;
}
