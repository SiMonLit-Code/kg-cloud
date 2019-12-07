package com.plantdata.kgcloud.domain.document.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.document.req.AttrReq;
import com.plantdata.kgcloud.domain.document.req.ConceptReq;
import com.plantdata.kgcloud.domain.document.req.DocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.ConceptRsp;
import com.plantdata.kgcloud.domain.document.service.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "模式")
@RestController
@RequestMapping("/model")
public class ModelController {


    @Autowired
    private ModelService modelService;


    @ApiOperation("获取文档模式")
    @PatchMapping("/get/{id:\\d+}/{sceneId:\\d+}")
    public ApiReturn<List<ConceptRsp>> getModel(@PathVariable("id") Integer id, @PathVariable("sceneId") Integer sceneId) {
        return modelService.getModel(sceneId,id);
    }

    @ApiOperation("更新文档模式-更新概念")
    @PostMapping("/save/concept")
    public ApiReturn saveModelConcept(@RequestBody ConceptReq conceptReq) {
        return modelService.saveModelConcept(conceptReq);
    }

    @ApiOperation("更新文档模式-更新属性")
    @PostMapping("/save/attr")
    public ApiReturn updateModelAttr(@RequestBody AttrReq attrReq) {
        return modelService.saveModelAttr(attrReq);
    }

    @ApiOperation("模式入图")
    @PostMapping("/import/group")
    public ApiReturn importGroup(@RequestBody DocumentReq documentReq) {
        return modelService.importGroup(documentReq.getSceneId(),documentReq.getId());
    }

}
