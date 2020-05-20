package com.plantdata.kgcloud.domain.file.rsq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:59
 */
@Data
public class FileDatabaseRsp {

    private Long id;

    private String userId;

    @ApiModelProperty(value = "数据库名称")
    private String title;

    @ApiModelProperty(value = "数据表列表")
    private List<FileTableRsp> tables;

}
