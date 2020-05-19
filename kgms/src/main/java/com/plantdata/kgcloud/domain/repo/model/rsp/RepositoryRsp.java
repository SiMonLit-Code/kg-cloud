package com.plantdata.kgcloud.domain.repo.model.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @date 2020/5/18  10:03
 */
@Getter
@Setter
@ApiModel("组件显示模型")
public class RepositoryRsp {
    @ApiModelProperty("组件id")
    private int id;
    @ApiModelProperty("组件名称")
    private String name;
    @ApiModelProperty("组件分组名称前端定义")
    private int group;
    @ApiModelProperty("可操作状态")
    private boolean enable;
    @ApiModelProperty("启停状态")
    private boolean state;
    @ApiModelProperty("排序")
    private int rank;
    @ApiModelProperty("描述")
    private String remark;
    @ApiModelProperty("菜单id")
    private int menuId;
    @ApiModelProperty("前端自定义配置")
    private Map<String, Object> config;
    @ApiModelProperty("是否为新功能")
    private boolean newFunction;
}
