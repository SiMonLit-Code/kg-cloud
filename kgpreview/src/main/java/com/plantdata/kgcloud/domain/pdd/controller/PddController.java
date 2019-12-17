package com.plantdata.kgcloud.domain.pdd.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.pdd.service.PddService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xiezhenxiang 2019/12/9
 */
@RestController
@RequestMapping("/pdd")
@Api(tags = "文本入图")
public class PddController {

    @Autowired
    private PddService pddService;

    @GetMapping("/tag/get/{dmId}")
    @ApiOperation("获取文档标签")
    public ApiReturn<Map> getTag(@ApiParam("数据集ID") @PathVariable("dmId") Integer dmId) {

        Map m = pddService.getTag(dmId);
        return ApiReturn.success(m);
    }
}
