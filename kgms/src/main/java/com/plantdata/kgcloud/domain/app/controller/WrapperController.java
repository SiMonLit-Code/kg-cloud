package com.plantdata.kgcloud.domain.app.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.wrapper.bean.FieldConfigBean;
import com.hiekn.wrapper.service.Sfe4jService;
import com.hiekn.wrapper.service.StaticFetchKVAct;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wrapper")
@Api(tags = "Wrapper预览")
public class WrapperController {

    @Autowired
    private StaticFetchKVAct staticFetchKVAct;

    @Autowired
    private Sfe4jService sfe4jService;

    @PostMapping("preview")
    @ApiOperation("预览")
    public ApiReturn<Map<String, Object>> parse(@ApiParam(required = true) @RequestParam String html,
                                                @ApiParam(value = "字段配置", required = true) @RequestParam String config) {
        List<FieldConfigBean> fieldConfig = JsonUtils.readToList(config, FieldConfigBean.class);
        return ApiReturn.success(staticFetchKVAct.execute(html, fieldConfig));
    }


    @GetMapping("formatter/list")
    @ApiOperation("列表")
    public ApiReturn<List<Map<String, String>>> formatterList() {
        return ApiReturn.success(sfe4jService.getDefaultFormatter());
    }


}
