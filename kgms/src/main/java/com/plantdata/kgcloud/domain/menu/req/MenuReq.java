package com.plantdata.kgcloud.domain.menu.req;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:29
 **/
@Data
public class MenuReq {

    @ApiParam(value = "id")
    private Integer id;

    @ApiParam(value = "父id")
    private Integer pid;

    @ApiParam(required = true, value = "name")
    private String name;

    @ApiParam(value = "允许状态(1-禁用,0-不禁用)")
    private Boolean enable;

    @ApiParam(value = "选中状态(1-选中,0-未选中)")
    private Boolean checked;

    @ApiParam(value = "类型")
    private String type;

    @ApiParam(value = "排序")
    private Integer order;

    @ApiParam(value = "配置")
    private String config;
}
