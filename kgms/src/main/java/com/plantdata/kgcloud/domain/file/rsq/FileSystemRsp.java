package com.plantdata.kgcloud.domain.file.rsq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:59
 */
@Data
public class FileSystemRsp {

    private Long id;

    private String userId;

    @ApiModelProperty(value = "文件系统名称")
    private String name;

    @ApiModelProperty(value = "是否是默认文件系统")
    private Boolean isDefault;

    @ApiModelProperty(value = "文件夹列表")
    private List<FolderRsp> fileFolders;

}
