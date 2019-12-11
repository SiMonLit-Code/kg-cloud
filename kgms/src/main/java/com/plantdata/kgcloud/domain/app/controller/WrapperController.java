package com.plantdata.kgcloud.domain.app.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wrapper")
@Api("Wrapper预览")
public class WrapperController {

//    @Autowired
//    private StaticFetchKVAct staticFetchKVAct;
//
//    @Autowired
//    private Sfe4jService sfe4jService;
//
//    @PostMapping("preview")
//    @ApiOperation("预览")
//    @ExcludeValidation
//    public RestResp<Map<String, Object>> parse(@ApiParam(required = true) @RequestParam String html,
//                                               @ApiParam(value = "字段配置",required = true) @RequestParam String config){
//        List<FieldConfigBean> fieldConfig = JsonUtils.fromJson(config,new TypeToken<List<FieldConfigBean>>(){}.getType());
//        return new RestResp(staticFetchKVAct.execute(html,fieldConfig));
//    }
//
//
//    @GetMapping("formatter/list")
//    @ApiOperation("列表")
//    @ExcludeValidation
//    public RestResp<List<Map<String, String>>> formatterList(){
//        return new RestResp(sfe4jService.getDefaultFormatter());
//    }

}
