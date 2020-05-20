package com.plantdata.kgcloud.domain.file.rsq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:42
 */
@Data
public class FileTableRsp {

    private Long id;

    @ApiModelProperty(value = "数据表名称")
    private String title;

    @ApiModelProperty(value = "数据库id")
    private Long fileDataBaseId;

    @ApiModelProperty(value = "文件数量")
    private Long fileCount;

}
