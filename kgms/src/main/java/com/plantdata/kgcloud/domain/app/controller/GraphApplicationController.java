package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import com.plantdata.kgcloud.domain.app.converter.ApkConverter;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.GraphPromptService;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 13:50
 */
@RestController
@RequestMapping("app")
public class GraphApplicationController implements GraphAppInterface {

    @Autowired
    private GraphApplicationService graphApplicationService;
    @Autowired
    private GraphPromptService graphPromptService;
    @Autowired
    private GraphService graphService;

    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema/{kgName}")
    public ApiReturn<SchemaRsp> querySchema(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphApplicationService.querySchema(kgName));
    }

    @ApiOperation("知识推荐")
    @PostMapping("knowledgeRecommend/{kgName}")
    public ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                  @RequestBody @Valid KnowledgeRecommendReq recommendParam) {
        return ApiReturn.success(graphApplicationService.knowledgeRecommend(kgName, recommendParam));
    }

    @ApiOperation("获取模型可视化数据")
    @GetMapping("visualModels/{kgName}/{conceptId}")
    public ApiReturn<BasicConceptTreeRsp> visualModels(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                       @ApiParam(value = "概念id", required = true) @PathVariable("conceptId") long conceptId,
                                                       @ApiParam(value = "是否显示对象属性", defaultValue = "false") @RequestParam("display") boolean display) {
        return ApiReturn.success(graphApplicationService.visualModels(kgName, display, conceptId));
    }

    @ApiOperation("获取概念树")
    @GetMapping("concept/{kgName}")
    public ApiReturn<List<BasicInfoVO>> conceptTree(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                    @ApiParam("概念id 0或不传查询全部") @RequestParam(value = "conceptId", required = false) Long conceptId,
                                                    @ApiParam("概念唯一标识概念id为null时有效") @RequestParam(value = "conceptKey", required = false) String conceptKey) {
        return ApiReturn.success(graphApplicationService.conceptTree(kgName, conceptId, conceptKey));
    }

    @ApiOperation("批量读取知识卡片")
    @PostMapping("infoBox/list/{kgName}")
    public ApiReturn<List<InfoBoxRsp>> listInfoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody BatchInfoBoxReq batchInfoBoxReq) {
        return ApiReturn.success(graphApplicationService.infoBox(kgName, batchInfoBoxReq));
    }

    @ApiOperation("读取知识卡片")
    @PostMapping("infoBox/{kgName}")
    public ApiReturn<InfoBoxRsp> infoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                         @RequestBody InfoBoxReq infoBoxReq) throws IOException {
        return ApiReturn.success(graphApplicationService.infoBox(kgName, SessionHolder.getUserId(), infoBoxReq));
    }

    @ApiOperation("获取所有图谱名称")
    @GetMapping("kgName/all/{apk}")
    public ApiReturn<List<ApkRsp>> getKgName(@PathVariable("apk") String apk) {
        String userId = SessionHolder.getUserId();
        List<GraphRsp> all = graphService.findAll(userId);
        return ApiReturn.success(BasicConverter.listConvert(all, a -> ApkConverter.graphRspToApkRsp(a, apk)));
    }


    @ApiOperation("综合搜索")
    @PostMapping("prompt/{kgName}")
    public ApiReturn<List<PromptEntityRsp>> prompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody @Valid PromptReq promptReq, @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(graphPromptService.prompt(kgName, promptReq));
    }

    @ApiOperation("高级搜索查实体")
    @GetMapping("prompt/senior/{kgName}")
    public ApiReturn<List<SeniorPromptRsp>> seniorPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                         @RequestBody @Valid SeniorPromptReq seniorPromptReq, @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(graphPromptService.seniorPrompt(kgName, seniorPromptReq));
    }

    @ApiOperation("边属性搜索")
    @PostMapping("attributes/{kgName}")
    public ApiReturn<List<EdgeAttributeRsp>> attrPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @Valid @RequestBody EdgeAttrPromptReq edgeAttrPromptReq, @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(graphPromptService.edgeAttributeSearch(kgName, edgeAttrPromptReq));
    }

}


