package com.plantdata.kgcloud.domain.app.controller;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:59
 */
@RestController
@RequestMapping("kgData")
public class KgDataController {


    @Autowired
    private ModelService modelService;


    @ApiOperation("sparql查询")
    @PostMapping("sparQl/query/{kgName}")
    public ApiReturn<Object> sparQlQuery(@Valid @ApiIgnore SparQlReq sparQlReq) {
        //todo 底层提供api
        return ApiReturn.success();
    }

    @ApiOperation("第三方模型抽取")
    @PostMapping("extract/thirdModel/{modelId}")
    public ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                               @RequestParam("input") String input, @RequestBody List<Map<String, String>> configList) {
        //todo kgms实现
        modelService.call(modelId, Lists.newArrayList(input));
        return ApiReturn.success(null);
    }

}
