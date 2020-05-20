package com.plantdata.kgcloud.domain.repo.model.common;

import com.plantdata.kgcloud.domain.repo.enums.RepositoryTypeEnum;
import com.plantdata.kgcloud.domain.repo.model.req.RepoCheckConfigReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/18  18:17
 */
@Getter
@Setter
public abstract class BaseRepositoryVO {
    @NotBlank
    @ApiModelProperty(name = "组件名称", required = true)
    private String name;
    @ApiModelProperty(name = "启停状态", required = true)
    private boolean state;
    @NotNull
    @ApiModelProperty(name = "组件类型",required = true)
    private RepositoryTypeEnum type;
    @ApiModelProperty(name = "菜单id", required = true)
    private int menuId;
    @ApiModelProperty(name = "分组id", required = true)
    private int groupId;
    @ApiModelProperty(name = "排序", required = true)
    private int rank;
    @ApiModelProperty(name = "描述")
    private String remark;
    @ApiModelProperty(name = "检测配置")
    private List<RepoCheckConfigReq> checkConfigs;
    @ApiModelProperty("前端自定义配置")
    private Map<String, Object> config;
}
