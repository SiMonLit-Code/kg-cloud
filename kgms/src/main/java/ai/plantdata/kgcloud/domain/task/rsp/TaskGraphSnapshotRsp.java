package ai.plantdata.kgcloud.domain.task.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class TaskGraphSnapshotRsp {
    private Long id;

    private String userId;

    private String name;

    private String kgName;

    private String fileSize;

    @ApiModelProperty("文件保存目录")
    private String catalogue;

    @ApiModelProperty("文件存储类型(0:本地服务器,1:fastDFS)")
    private Integer fileStoreType;

    @ApiModelProperty("文件备份类型(0:仅图谱数据,1:图谱数据和多模态数据)")
    private Integer fileBackupType;

    @ApiModelProperty("磁盘空间总量")
    private String diskSpaceSize;

    @ApiModelProperty("还原状态(0:进行中,1:成功，2:失败)")
    private Integer status;

    @ApiModelProperty("还原时间")
    private Date restoreAt;

    @ApiModelProperty("备份时间")
    private Date createAt;

    private Date updateAt;

}
