package com.plantdata.kgcloud.domain.chanyelian.controller;

import com.plantdata.kgcloud.sdk.ComponentClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xiezhenxiang 2020/4/14
 */
@RestController
@RequestMapping("/lian")
@Api(tags = "产业链")
public class LianController {

    @Resource
    private ComponentClient componentClient;

    @GetMapping("/view/{kgName}")
    @ApiOperation("可视化")
    public Object view(@PathVariable("kgName") String kgName) {
        return componentClient.chanYeLianView(kgName);
    }
}
