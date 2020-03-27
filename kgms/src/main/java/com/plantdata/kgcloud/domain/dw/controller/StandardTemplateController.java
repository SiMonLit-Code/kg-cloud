package com.plantdata.kgcloud.domain.dw.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dw.req.StandardSearchReq;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateRsp;
import com.plantdata.kgcloud.domain.dw.service.StandardTemplateService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "行业标准")
@RestController
@RequestMapping("/standard/template")
public class StandardTemplateController {


    @Autowired
    private StandardTemplateService standardTemplateService;

    @ApiOperation("行业标准-查找所有")
    @PostMapping("/all")
    public ApiReturn<List<StandardTemplateRsp>> findAll(@RequestBody StandardSearchReq req) {
        return ApiReturn.success(standardTemplateService.findAll(req));
    }
}
