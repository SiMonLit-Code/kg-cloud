package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.bean.ApiReturn;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:36
 */
@Slf4j
@RestController
@RequestMapping("v3/app/prompt")
public class GraphPromptController implements GraphApplicationInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("综合搜索")
    @PostMapping("{kgName}")
    public ApiReturn<List<PromptEntityRsp>> prompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody  PromptReq promptReq) {
        return appClient.prompt(kgName,promptReq);
    }

    @ApiOperation("高级搜索查实体")
    @PostMapping("senior/{kgName}")
    public ApiReturn<List<SeniorPromptRsp>> seniorPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                         @RequestBody SeniorPromptReq seniorPromptReq) {
        return appClient.seniorPrompt(kgName, seniorPromptReq);
    }

    @ApiOperation("边属性搜索")
    @PostMapping("attributes/{kgName}")
    public ApiReturn<List<EdgeAttributeRsp>> attrPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                       @RequestBody EdgeAttrPromptReq edgeAttrPromptReq) {
        return appClient.attrPrompt(kgName, edgeAttrPromptReq);
    }

}
