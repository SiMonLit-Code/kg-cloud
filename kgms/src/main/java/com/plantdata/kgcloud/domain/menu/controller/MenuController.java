package com.plantdata.kgcloud.domain.menu.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.menu.req.MenuReq;
import com.plantdata.kgcloud.domain.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-25 20:26
 **/
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/{id}")
    @ApiOperation("根据节点获取子节点树")
    public ApiReturn get(@PathVariable("id") Integer id) {
        return ApiReturn.success(menuService.getMenuById(id));
    }


    @PatchMapping("/")
    @ApiOperation("更新菜单应用")
    public ApiReturn update(@Validated @RequestBody MenuReq menuBean) {
        menuService.updateMenu(menuBean);
        return ApiReturn.success();
    }

    @PatchMapping("/batch")
    @ApiOperation("批量更新菜单应用")
    public ApiReturn updateBatch(@Validated List<MenuReq> menuBeans) {
        menuService.updateBatch(menuBeans);
        return ApiReturn.success();
    }

    @GetMapping("/refresh")
    @ApiOperation("刷新缓存")
    public ApiReturn refresh() {
        menuService.refresh();
        return ApiReturn.success();
    }

}
