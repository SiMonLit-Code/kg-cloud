package ai.plantdata.kgcloud.domain.repo.model.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class GroupRsp {
    @ApiModelProperty("组id")
    private int id;
    @ApiModelProperty("所属分组id")
    private int groupId;
    @ApiModelProperty("分组名称")
    private String groupName;
    @ApiModelProperty("排序")
    private Integer rank;
    @ApiModelProperty("分组描述")
    private String desc;
}