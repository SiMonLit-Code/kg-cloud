package com.plantdata.kgcloud.domain.repo.model.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupRsp {
    @ApiModelProperty("组id")
    private int id;
    @ApiModelProperty("所属分组id")
    private int groupId;
    @ApiModelProperty("分组名称")
    private String groupName;
    @ApiModelProperty("分组描述")
    private String desc;
}