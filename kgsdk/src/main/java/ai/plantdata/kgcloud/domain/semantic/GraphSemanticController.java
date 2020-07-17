package ai.plantdata.kgcloud.domain.semantic;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphSemanticApplicationInterface;
import ai.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter2;
import ai.plantdata.kgcloud.sdk.EntityFileClient;
import ai.plantdata.kgcloud.sdk.NlpClient;
import ai.plantdata.kgcloud.sdk.ReasoningClient;
import ai.plantdata.kgcloud.sdk.SemanticClient;
import ai.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import ai.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import ai.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import ai.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import com.hiekn.pddocument.bean.PdDocument;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:58
 */
@RestController
@RequestMapping("v3/app/semantic")
@Slf4j
public class GraphSemanticController implements GraphSemanticApplicationInterface {

    @Autowired
    private SemanticClient semanticClient;
    @Autowired
    private ReasoningClient reasoningClient;
    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public EntityFileClient entityFileClient;

    @ApiOperation(value = "知识图谱问答初始化", notes = "意图初始化接口。基于给定的知识图谱进行意图识别模块的初始化，" +
            "完成初始化后的图谱才能进行基于知识图谱的问答，" +
            "当知识图谱的模式（概念定义，属性定义）发生变化时需要重新进行初始化工作。")
    @GetMapping("kbqa/init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {
        semanticClient.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation(value = "知识图谱问答", notes = "知识图谱问答接口。基于给定的知识图谱进行图谱中知识的问答，包括实体属性问答，" +
            "实体关系问答，属性约束问答，属性最值问答及多跳问答，" +
            "初次构建的知识图谱以及图谱模式（概念定义，属性定义）发生变化后需要调用一次意图初始化接口，才能正确使用本接口。")
    @PostMapping("kbqa/{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                             @RequestBody QueryReq queryReq) {

        return semanticClient.qaKbQa(kgName, queryReq);
    }

    @ApiOperation(value = "隐含关系推理", notes = "关系推理接口。在给定的知识图谱中基于路径规则进行隐含关系的推理。")
    @PostMapping("/execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        return reasoningClient.reasoning(kgName, reasoningReq);
    }

    @ApiOperation(value = "文本语义标注", notes = "文本语义标注，以知识图谱的实体，对输入文本进行标注。")
    @PostMapping("kbqa/annotation/{kgName}")
    public ApiReturn<PdDocument> tagging(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                         @RequestBody EntityLinkingReq linkingFrom) {
        return ApiReturn.success(NlpConverter2.annotationToPdDocument(nlpClient.tagging(kgName, linkingFrom).getData()));
    }

    @ApiOperation(value = "批量标引", notes = "提供图谱实体和多模态数据的批量标引。")
    @PostMapping("{kgName}/create/relation")
    public ApiReturn<EntityFileRelationRsp> add(@PathVariable("kgName") String kgName, @RequestBody EntityFileRelationAddReq req) {
        return entityFileClient.add(kgName, req);
    }
}
