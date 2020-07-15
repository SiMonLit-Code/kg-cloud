package ai.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import ai.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import ai.plantdata.kgcloud.sdk.req.app.PromptReq;
import ai.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import ai.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "实体提示",notes = "知识图谱概念、实体的下拉提示，支持模糊匹配。")
    @PostMapping("{kgName}")
    public ApiReturn<List<PromptEntityRsp>> prompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody  PromptReq promptReq) {
        return appClient.prompt(kgName,promptReq);
    }

    @ApiOperation("高级搜索(仅实体)")
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
