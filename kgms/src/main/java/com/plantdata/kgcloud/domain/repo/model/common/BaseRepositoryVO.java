package com.plantdata.kgcloud.domain.repo.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/18  18:17
 */
@Getter
@Setter
public abstract class BaseRepositoryVO {
    @ApiModelProperty(name = "组件名称",required = true)
    private String name;
    @ApiModelProperty(name = "组件分组名称前端定义",required = true)
    private int group;
    @ApiModelProperty(name = "启停状态",required = true)
    private boolean state;
    @ApiModelProperty(name = "排序",required = true)
    private int rank;
    @ApiModelProperty(name="描述",required = true)
    private String remark;
    @ApiModelProperty(name = "菜单id",required = true)
    private String menuId;
    @ApiModelProperty("前端自定义配置")
    private Map<String, Object> config;
}
