package com.plantdata.kgcloud.domain.app.controller;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 10:00
 */

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.service.GraphEditService;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("kgdata")
public class GraphEditController {

    @Autowired
    private GraphEditService graphEditService;

    @ApiOperation("添加概念")
    @PostMapping("concept/{kgName}")
    public ApiReturn<Long> createConcept(@PathVariable("kgName") String kgName,
                                         @Valid @RequestBody ConceptAddReq conceptAddReq) {
        return ApiReturn.success(graphEditService.createConcept(kgName, conceptAddReq));
    }
}
