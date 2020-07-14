package ai.plantdata.kgcloud.domain.file.rsq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:42
 */
@Data
public class FolderRsp {

    private Long id;

    @ApiModelProperty(value = "文件夹名称")
    private String name;

    @ApiModelProperty(value = "是否是默认文件夹")
    private Boolean isDefault;

    @ApiModelProperty(value = "文件系统id")
    private Long fileSystemId;

    @ApiModelProperty(value = "文件数量")
    private Long fileCount;

}
