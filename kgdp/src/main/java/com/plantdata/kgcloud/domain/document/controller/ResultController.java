package com.plantdata.kgcloud.domain.document.controller;


import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.document.req.DocumentReq;
import com.plantdata.kgcloud.domain.document.req.PdDocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.DataCheckRsp;
import com.plantdata.kgcloud.domain.document.service.ResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "结果集")
@RestController
@RequestMapping("/result")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @ApiOperation("冲突检查")
    @PostMapping("/check")
    public ApiReturn<List<DataCheckRsp>> check(@RequestBody DocumentReq documentReq) {
        return resultService.check(documentReq.getSceneId(),documentReq.getId());
    }


    @ApiOperation("结果入图")
    @PostMapping("/import/group")
    public ApiReturn importGroup(@RequestBody DocumentReq documentReq) {
        resultService.importGroup(documentReq.getSceneId(),documentReq.getId(),documentReq.getModel());
        return ApiReturn.success();
    }

    @ApiOperation("获取文档标注")
    @PatchMapping("/get/pddocument/{id:\\d+}/{sceneId:\\d+}")
    public ApiReturn<List<PdDocument>> getPddocument(@PathVariable("id") Integer id, @PathVariable("sceneId") Integer sceneId) {
        return resultService.getPddocument(sceneId,id);
    }

    @ApiOperation("更新文档标注")
    @PostMapping("/update/pddocument")
    public ApiReturn updatePddocument(@RequestBody PdDocumentReq documentReq) {
        return resultService.updatePddocument(documentReq.getSceneId(), documentReq.getId(),documentReq.getPdDocumentList());
    }

}
