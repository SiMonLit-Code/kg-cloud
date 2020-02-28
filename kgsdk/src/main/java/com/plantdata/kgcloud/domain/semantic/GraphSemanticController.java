package com.plantdata.kgcloud.domain.semantic;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphSemanticApplicationInterface;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
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

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 15:58
 */
@RestController
@RequestMapping("v3/app/semantic/kbqa")
@Slf4j
public class GraphSemanticController implements GraphSemanticApplicationInterface {

    @Autowired
    private SemanticClient semanticClient;
    @Autowired
    private ReasoningClient reasoningClient;

    @ApiOperation(value = "知识图谱问答初始化",notes = "意图初始化接口。基于给定的知识图谱进行意图识别模块的初始化，" +
            "完成初始化后的图谱才能进行基于知识图谱的问答，" +
            "当知识图谱的模式（概念定义，属性定义）发生变化时需要重新进行初始化工作。")
    @GetMapping("init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable("kgName") String kgName) {
        semanticClient.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation(value = "知识图谱问答",notes = "知识图谱问答接口。基于给定的知识图谱进行图谱中知识的问答，包括实体属性问答，" +
            "实体关系问答，属性约束问答，属性最值问答及多跳问答，" +
            "初次构建的知识图谱以及图谱模式（概念定义，属性定义）发生变化后需要调用一次意图初始化接口，才能正确使用本接口。")
    @PostMapping("{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                             @RequestBody QueryReq queryReq) {

        return semanticClient.qaKbQa(kgName, queryReq);
    }

    @ApiOperation(value = "隐含关系推理",notes = "关系推理接口。在给定的知识图谱中基于路径规则进行隐含关系的推理。")
    @PostMapping("/execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        return reasoningClient.reasoning(kgName, reasoningReq);
    }
}
