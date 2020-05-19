package com.plantdata.kgcloud.domain.repo.model.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/19  16:46
 */
@Getter
@Setter
@ApiModel("组件列表展示")
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryListRsp {
    @ApiModelProperty("分组")
    private List<GroupRsp> groups;
    @ApiModelProperty("组件")
    private List<RepositoryRsp> repositoryRspList;
}
