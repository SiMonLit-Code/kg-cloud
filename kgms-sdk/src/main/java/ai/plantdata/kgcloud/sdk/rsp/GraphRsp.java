package ai.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class GraphRsp {

    private String userId;
    @ApiModelProperty(value = "图谱唯一标识")
    private String kgName;

    @ApiModelProperty(value = "图谱标题")
    private String title;
    @ApiModelProperty(value = "图谱icon")
    private String icon;
    @ApiModelProperty(value = "图谱是否私有")
    private Boolean privately;

    @ApiModelProperty(value = "图谱是否可修改")
    private Boolean editable;

    @ApiModelProperty(value = "图谱是否可删除")
    private Boolean deleted;

    @ApiModelProperty(value = "图谱备注")
    private String remark;
    private Date createAt;
    private Date updateAt;
}
