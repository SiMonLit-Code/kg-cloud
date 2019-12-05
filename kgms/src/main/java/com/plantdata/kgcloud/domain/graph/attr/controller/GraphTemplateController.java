package com.plantdata.kgcloud.domain.graph.attr.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrTemplateRsp;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 18:08
 * @Description:
 */
@Api(tags = "属性模板")
@RestController
@RequestMapping("/edit/template")
public class GraphTemplateController {

    @Autowired
    private GraphAttrTemplateService graphAttrTemplateService;

    @ApiOperation("所有属性模板")
    @GetMapping("/all")
    ApiReturn<List<GraphAttrTemplateRsp>> listAttrTemplate() {
        return ApiReturn.success(graphAttrTemplateService.listAttrTemplate());
    }

    @ApiOperation("属性模板详情")
    @GetMapping("/{kgName}/{id}")
    ApiReturn<GraphAttrTemplateRsp> listAttrTemplate(@PathVariable("kgName") String kgName,
                                                     @PathVariable("id") Long id) {
        return ApiReturn.success(graphAttrTemplateService.getDetails(kgName, id));
    }
}
