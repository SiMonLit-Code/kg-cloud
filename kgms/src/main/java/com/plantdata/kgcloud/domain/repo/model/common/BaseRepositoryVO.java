package com.plantdata.kgcloud.domain.repo.model.common;

import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import com.plantdata.kgcloud.domain.repo.model.req.RepoCheckConfigReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/18  18:17
 */
@Getter
@Setter
public abstract class BaseRepositoryVO {
    @ApiModelProperty(name = "组件名称", required = true)
    private String name;
    @ApiModelProperty(name = "启停状态", required = true)
    private boolean state;
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
