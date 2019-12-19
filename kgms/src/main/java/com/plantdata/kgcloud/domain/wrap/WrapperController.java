package com.plantdata.kgcloud.domain.wrap;

import com.hiekn.wrapper.bean.FieldConfigBean;
import com.hiekn.wrapper.service.Sfe4jService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.wrap.StaticFetchKVAct;
import com.plantdata.kgcloud.sdk.req.wrapper.WrapperPreviewReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("wrapper")
public class WrapperController implements GraphAppInterface {

    @Autowired
    private StaticFetchKVAct staticFetchKVAct;

    @Autowired
    private Sfe4jService sfe4jService;

    @PostMapping("preview")
    @ApiOperation("Wrapper预览")
    public ApiReturn<Map<String, Object>> parse(@RequestBody WrapperPreviewReq previewReq) {
        List<FieldConfigBean> fieldConfig = BasicConverter.listConvert(previewReq.getConfig(), a -> BasicConverter.copy(a, FieldConfigBean.class));
        return ApiReturn.success(staticFetchKVAct.execute(previewReq.getHtml(), fieldConfig));
    }


    @GetMapping("formatter/list")
    @ApiOperation("列表")
    public ApiReturn<List<Map<String, String>>> formatterList() {
        return ApiReturn.success(sfe4jService.getDefaultFormatter());
    }


}
