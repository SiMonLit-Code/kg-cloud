package com.plantdata.kgcloud.domain.menu.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.SessionHolder;
import com.plantdata.kgcloud.domain.menu.rsp.MenuFavorRsp;
import com.plantdata.kgcloud.domain.menu.service.MenuFavorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:27
 **/
@Api(tags = "菜单订阅")
@RestController
@RequestMapping("/menu/favor")
public class MenuFavorController {

    @Autowired
    private MenuFavorService menuFavorService;

    @ApiOperation("菜单订阅-获取订阅菜单")
    @GetMapping("/")
    public ApiReturn<MenuFavorRsp> find() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(menuFavorService.find(userId));
    }


    @ApiOperation("菜单订阅-订阅")
    @PostMapping("/")
    public ApiReturn<MenuFavorRsp> favor(
            @ApiParam("菜单Id列表")
            @RequestBody List<Integer> menuId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(menuFavorService.favor(userId, menuId));
    }

}
