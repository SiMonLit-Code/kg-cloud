package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: LinHo
 * @Date: 2020/3/23 10:42
 * @Description:
 */
@Setter
@Getter
@ApiModel("多模态数据")
public class MultiModalReq {
    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("文件名称")
    @Length(min = 1, max = 50, message = "长度不能超过50")
    private String name;

    @ApiModelProperty("文件路径")
    @Length(max = 200, message = "长度不能超过200")
    private String path;

    @ApiModelProperty("缩略图路径")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("上传类型(0：上传文件，1：选择文件)")
    private Integer uploadType;

    @ApiModelProperty("文件系统id")
    private Long fileSystemId;

    @ApiModelProperty("所属文件夹id")
    private Long folderId;

    @ApiModelProperty("文件id(上传类型为1传值)")
    private String fileId;

    @ApiModelProperty("关键词")
    @Length(max = 100, message = "长度不能超过100")
    private String keyword;

    @ApiModelProperty("简介")
    @Length(max = 5000, message = "长度不能超过5000")
    private String description;
}
