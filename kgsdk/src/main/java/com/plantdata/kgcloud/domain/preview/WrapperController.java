package com.plantdata.kgcloud.domain.preview;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.WrapperClient;
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

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 16:28
 */
@RestController
@RequestMapping("v3/wrapper/extract")
public class WrapperController implements GraphApplicationInterface {

    @Autowired
    private WrapperClient wrapperClient;

    @PostMapping("preview")
    @ApiOperation("预览")
    public ApiReturn<Map<String, Object>> parse(@ApiParam(required = true) @RequestParam String html,
                                                @ApiParam(value = "字段配置", required = true) @RequestParam String config) {
        return wrapperClient.parse(html, config);
    }

    @GetMapping("formatter/list")
    @ApiOperation("列表")
    public ApiReturn<List<Map<String, String>>> formatterList() {
        return wrapperClient.formatterList();
    }
}
