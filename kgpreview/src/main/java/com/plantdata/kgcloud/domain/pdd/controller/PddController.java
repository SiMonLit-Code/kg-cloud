package com.plantdata.kgcloud.domain.pdd.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author xiezhenxiang 2019/12/9
 */
@RestController
@RequestMapping("/pdd")
@Api(tags = "文本入图")
public class PddController {

    @GetMapping("/dm/list")
    @ApiOperation("文本数据集列表")
    public ApiReturn<Map> dmList(@ApiParam("用户ID") @NotBlank String userId) {

        // RestData rs = docService.listDm(userId);
        return ApiReturn.success();
    }

    @GetMapping("/tag/get")
    @ApiOperation("获取文档标签")
    public ApiReturn<Map> getTag(@ApiParam("数据集ID") @NotNull Integer dmId) {

        // Object obj = docService.getTag(dmId);
        return ApiReturn.success();
    }
}
