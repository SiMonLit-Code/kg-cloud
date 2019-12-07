package com.plantdata.kgcloud.domain.nlp;

import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.NaturalLanguageProcessingInterface;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBean;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("nlp")
public class NlpController implements NaturalLanguageProcessingInterface {

    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public SemanticClient semanticClient;

    private HanLPService hanLPService = new HanLPService();

    @ApiOperation("语义标注")
    @PostMapping("annotation")
    public ApiReturn<List<TaggingItemRsp>> tagging(@RequestParam("kgName") String kgName, @RequestBody EntityLinkingReq linkingFrom) {
        return nlpClient.tagging(kgName, linkingFrom);
    }

    @ApiOperation("中文命名实体识别")
    @PostMapping("ner")
    public ApiReturn<List<NerResultRsp>> namedEntityRecognition(@RequestBody NerReq nerReq) {
        return nlpClient.namedEntityRecognition(nerReq);
    }

    @ApiOperation("图谱分词")
    @PostMapping("segment/graph/{kgName}")
    public ApiReturn<List<GraphSegmentRsp>> graphSegment(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                         @RequestBody SegmentReq segmentReq) {
        return nlpClient.graphSegment(kgName, segmentReq);
    }

    @ApiOperation("语义关联")
    @PostMapping("/intent")
    public ApiReturn<IntentDataBean> intent(
            @ApiParam(value = "图谱名称") @RequestParam("kgName") String kgName,
            @ApiParam(value = "自然语言输入") @RequestParam("query") String query,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        return semanticClient.intent(kgName, query, size);
    }

    @ApiOperation("简体转换为繁体")
    @PostMapping("traditional/chinese")
    public ApiReturn<String> toTraditionalChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return ApiReturn.success(hanLPService.toTraditionalChinese(input));
    }

    @ApiOperation("转换为简体中文")
    @PostMapping("simplified/chinese")
    public ApiReturn<String> toSimplifiedChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return ApiReturn.success(hanLPService.toSimplifiedChinese(input));
    }

    @ApiOperation("中文转换为拼音")
    @PostMapping("phonetic")
    public ApiReturn<List<String>> phonetic(@ApiParam(required = true) @RequestParam("input") String input) {
        return ApiReturn.success(hanLPService.toPinyin(input));
    }

    @ApiOperation("中文分词")
    @PostMapping("segment/chinese")
    public ApiReturn<List<String>> seg(@RequestParam @ApiParam(required = true) String input) {

        return ApiReturn.success(hanLPService.seg(input));
    }

    @ApiOperation("自动摘要")
    @PostMapping("summarize")
    public ApiReturn<List<String>> summarize(@ApiParam(required = true) @RequestParam("input") String input,
                                             @ApiParam(required = true, value = " 句子个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.summarize(input, size));
    }

    @ApiOperation("词性标注")
    @PostMapping("pos")
    public ApiReturn<List<String>> pos(@ApiParam(required = true) @RequestParam("input") String input) {
        return ApiReturn.success(hanLPService.pos(input));
    }

    @ApiOperation("中文命名实体识别")
    @PostMapping("ner/chinese")
    public ApiReturn<Map<String, List<String>>> ner(@ApiParam(required = true) @RequestParam("input") String input) {
        return ApiReturn.success(hanLPService.ner(input));
    }

    @ApiOperation("短语提取")
    @PostMapping("extract/phrase")
    public ApiReturn<List<String>> extractPhrase(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.extractPhrase(input, size));
    }

    @ApiOperation("新词发现")
    @PostMapping("extract/newWord")
    public ApiReturn<List<String>> extractNewWord(@ApiParam(required = true) @RequestParam("input") String input,
                                                  @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(hanLPService.extractNewWord(input, size));
    }

    @ApiOperation("关键词提取")
    @PostMapping("extract/keyword")
    public ApiReturn<List<String>> extractKeyword(@ApiParam(required = true) @RequestParam("input") String input,
                                                  @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success((hanLPService.extractKeyword(input, size)));
    }
}
