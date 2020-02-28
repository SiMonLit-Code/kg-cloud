package com.plantdata.kgcloud.domain.nlp;

import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.NaturalLanguageProcessingInterface;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.DistanceEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
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

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:07
 */
@RestController
@RequestMapping("v3/nlp")
public class NlpController implements NaturalLanguageProcessingInterface {

    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public SemanticClient semanticClient;

    private HanLPService hanLPService = new HanLPService();

    @ApiOperation(value = "文本语义标注",notes = "文本语义标注，以知识图谱的实体，对输入文本进行标注。")
    @PostMapping("annotation/{kgName}")
    public ApiReturn<List<TaggingItemRsp>> tagging(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestBody EntityLinkingReq linkingFrom) {
        return nlpClient.tagging(kgName, linkingFrom);
    }

    @ApiOperation(value = "图谱实体识别",notes = "实体识别，以知识图谱的实体，对输入文本进行命名实体识别。")
    @PostMapping("ner/graph/{kgName}")
    public ApiReturn<List<SegmentEntityRsp>> nerGraph(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                      @RequestBody SegmentReq segmentReq) {
        return nlpClient.nerGraph(kgName, segmentReq);
    }

    @ApiOperation("命名实体识别")
    @PostMapping("ner")
    public ApiReturn<List<NerResultRsp>> namedEntityRecognition(@RequestBody NerReq nerReq) {
        return nlpClient.namedEntityRecognition(nerReq);
    }

    @ApiOperation(value = "中文命名实体识别",notes = "中文命名实体识别，用于识别中文人名")
    @PostMapping("ner/chinese")
    public ApiReturn<Map<String, List<String>>> ner(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(hanLPService.ner(input));
    }

    @ApiOperation(value = "图谱分词",notes = "图谱分词，以知识图谱的实体，对输入文本进行分词。")
    @GetMapping("segment/graph/{kgName}")
    public ApiReturn<List<GraphSegmentRsp>> graphSegment(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                         SegmentReq segmentReq) {
        return nlpClient.graphSegment(kgName, segmentReq);
    }

    @ApiOperation(value = "语义关联",notes = "语义关联接口。在给定的知识图谱中对输入的文本内容进行实体识别和消歧，" +
            "并基于schema进行文本意图识别，返回识别结果及相应权重。")
    @PostMapping("association")
    public ApiReturn<IntentDataBeanRsp> intent(
            @ApiParam(value = "图谱名称") @RequestParam("kgName") String kgName,
            @ApiParam(value = "自然语言输入") @RequestParam("query") String query,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        return semanticClient.intent(kgName, query, size);
    }

    @ApiOperation(value = "繁体转换",notes = "繁体转换，将输入的文本转换为繁体中文")
    @PostMapping("traditional/chinese")
    public ApiReturn<String> toTraditionalChinese(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(hanLPService.toTraditionalChinese(input));
    }

    @ApiOperation(value = "简体转换",notes = "简体转换，将输入的文本转换为简体中文")
    @PostMapping("simplified/chinese")
    public ApiReturn<String> toSimplifiedChinese(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(hanLPService.toSimplifiedChinese(input));
    }

    @ApiOperation(value = "拼音转换",notes = "将输入文本转换为拼音")
    @PostMapping("phonetic")
    public ApiReturn<List<String>> phonetic(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(hanLPService.toPinyin(input));
    }

    @ApiOperation(value = "中文分词",notes = "中文分词")
    @PostMapping("segment/chinese")
    public ApiReturn<List<String>> seg(@RequestParam @ApiParam(required = true) String input) {

        return ApiReturn.success(hanLPService.seg(input));
    }

    @ApiOperation(value = "自动摘要",notes = "自动摘要")
    @PostMapping("summarize")
    public ApiReturn<List<String>> summarize(@ApiParam(required = true) @RequestBody String input,
                                             @ApiParam(required = true, value = " 句子个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.summarize(input, size));
    }

    @ApiOperation(value = "词性标注",notes = "词性标注")
    @PostMapping("pos")
    public ApiReturn<List<String>> pos(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(hanLPService.pos(input));
    }


    @ApiOperation(value = "短语提取",notes = "短语提取")
    @PostMapping("extract/phrase")
    public ApiReturn<List<String>> extractPhrase(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.extractPhrase(input, size));
    }

    @ApiOperation(value = "新词发现",notes = "新词发现")
    @PostMapping("extract/newWord")
    public ApiReturn<List<String>> extractNewWord(@ApiParam(required = true) @RequestParam("input") String input,
                                                  @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.extractNewWord(input, size));
    }

    @ApiOperation(value = "关键词提取",notes = "关键词提取")
    @PostMapping("extract/keyword")
    public ApiReturn<List<String>> extractKeyword(@ApiParam(required = true) @RequestParam("input") String input,
                                                  @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success((hanLPService.extractKeyword(input, size)));
    }

    @ApiOperation("两个实体间语义距离查询")
    @PostMapping("{kgName}/distance/score")
    public ApiReturn<Double> semanticDistanceScore(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestParam("entityIdOne") Long entityIdOne, @RequestParam("entityIdTwo") Long entityIdTwo) {
        return semanticClient.semanticDistanceScore(kgName, entityIdOne, entityIdTwo);
    }

    @ApiOperation("实体语义相关实体查询")
    @PostMapping("{kgName}/distance/list")
    public ApiReturn<List<DistanceEntityRsp>> semanticDistanceRelevance(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                        @RequestBody DistanceListReq listReq) {
        return ApiReturn.success(null);
    }
}
