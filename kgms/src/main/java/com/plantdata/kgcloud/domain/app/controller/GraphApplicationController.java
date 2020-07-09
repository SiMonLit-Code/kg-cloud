package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.web.util.SessionHolder;
import com.plantdata.kgcloud.domain.app.controller.module.GraphAppInterface;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.service.GraphPromptService;
import com.plantdata.kgcloud.sdk.req.app.*;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoboxMultiModalReq;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.*;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @ApiOperation("获取当前图实体类型及属性类型的schema")
    @GetMapping("schema/{kgName}")
    public ApiReturn<SchemaRsp> querySchema(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphApplicationService.querySchema(kgName));
    }

    @ApiOperation("知识推荐")
    @PostMapping("knowledgeRecommend/{kgName}")
    public ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                  @RequestBody @Valid KnowledgeRecommendReqList recommendParam) {
        return ApiReturn.success(graphApplicationService.knowledgeRecommend(kgName, recommendParam));
    }

    @ApiOperation("多层知识推荐,暂支持两层")
    @PostMapping("layer/knowledgeRecommend/{kgName}")
    public ApiReturn<List<ObjectAttributeRsp>> layerKnowledgeRecommend(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                  @RequestBody @Valid LayerKnowledgeRecommendReqList recommendParam) {
        return ApiReturn.success(graphApplicationService.layerKnowledgeRecommend(kgName, recommendParam));
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
                                                   @RequestBody @Valid BatchInfoBoxReqList batchInfoBoxReq) {
        return ApiReturn.success(graphApplicationService.infoBox(kgName, batchInfoBoxReq));
    }

    @ApiOperation("读取知识卡片")
    @PostMapping("infoBox/{kgName}")
    public ApiReturn<InfoBoxRsp> infoBox(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                         @RequestBody @Valid InfoBoxReq infoBoxReq) throws IOException {
        return ApiReturn.success(graphApplicationService.infoBox(kgName, SessionHolder.getUserId(), infoBoxReq));
    }


    @ApiOperation("批量读取多模态文件")
    @PostMapping("infoBox/list/multi/modal/{kgName}")
    public ApiReturn<List<InfoboxMultiModelRsp>> listInfoBoxMultiModal(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody @Valid BatchMultiModalReqList batchMultiModalReq) {
        return ApiReturn.success(graphApplicationService.listInfoBoxMultiModal(kgName, batchMultiModalReq));
    }


    @ApiOperation("读取实体多模态文件")
    @PostMapping("infoBox/multi/modal/{kgName}")
    public ApiReturn<InfoboxMultiModelRsp> infoBoxMultiModal(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                             @RequestBody @Valid InfoboxMultiModalReq infoboxMultiModalReq) throws IOException {
        return ApiReturn.success(graphApplicationService.infoBoxMultiModal(kgName, SessionHolder.getUserId(), infoboxMultiModalReq));
    }

    @ApiOperation("复杂图分析-可视化展示")
    @GetMapping("complex/graph/visual/{kgName}")
    public ApiReturn<ComplexGraphVisualRsp> complexGraphVisual(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, @Valid ComplexGraphVisualReq visualReq) {
        return ApiReturn.success(graphApplicationService.complexGraphVisual(kgName, visualReq));
    }

    @ApiOperation("获取所有图谱名称")
    @GetMapping("kgName/all")
    public ApiReturn<PageRsp<ApkRsp>> getKgName(PageReq pageReq) {
        PageRsp<ApkRsp> pageRsp = graphApplicationService.listAllGraph(pageReq);
        return ApiReturn.success(pageRsp);
    }


    @ApiOperation("综合搜索")
    @PostMapping("prompt/{kgName}")
    public ApiReturn<List<PromptEntityRsp>> prompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                   @RequestBody @Valid PromptReq promptReq) {
        return ApiReturn.success(graphPromptService.prompt(kgName, promptReq));
    }

    @ApiOperation("高级搜索查实体")
    @PostMapping("prompt/senior/{kgName}")
    public ApiReturn<List<SeniorPromptRsp>> seniorPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                         @RequestBody @Valid SeniorPromptReq seniorPromptReq) {
        return ApiReturn.success(graphPromptService.seniorPrompt(kgName, seniorPromptReq));
    }

    @ApiOperation("边属性搜索")
    @PostMapping("attributes/{kgName}")
    public ApiReturn<List<EdgeAttributeRsp>> attrPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @Valid @RequestBody EdgeAttrPromptReq edgeAttrPromptReq) {
        return ApiReturn.success(graphPromptService.edgeAttributeSearch(kgName, edgeAttrPromptReq));
    }

    @ApiOperation("融合候选集写入")
    @PostMapping("fusion/candidate")
    public ApiReturn fusionCandidateSet() {
        return ApiReturn.success();
    }

}


