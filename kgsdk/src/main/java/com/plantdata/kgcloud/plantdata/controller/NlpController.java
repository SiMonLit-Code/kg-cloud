package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter;
import com.plantdata.kgcloud.plantdata.req.entity.SegmentEntityBean;
import com.plantdata.kgcloud.plantdata.req.nlp.AnnotationParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.NerParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.QaIntentParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.RecognitionParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.SegmentParametet;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter2;
import com.hiekn.pddocument.bean.PdDocument;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController("nlpController-v2")
@RequestMapping("sdk/nlp")
public class NlpController implements SdkOldApiInterface {

    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public SemanticClient semanticClient;
    private HanLPService hanLPService = new HanLPService();

    /**
     * 命名实体识别
     *
     * @return 词列表
     */
    @ApiOperation("命名实体识别")
    @PostMapping("ner")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "input", dataType = "string", paramType = "form", value = "input"),
            @ApiImplicitParam(name = "config", required = true, dataType = "string", paramType = "form", value = "config"),
    })
    public RestResp<List<NerResultRsp>> ner(@Valid @ApiIgnore NerParameter nerParameter) {
        NerReq nerReq = JacksonUtils.readValue(JacksonUtils.writeValueAsString(nerParameter), NerReq.class);
        Optional<List<NerResultRsp>> nerResultRspList = BasicConverter.apiReturnData(nlpClient.namedEntityRecognition(nerReq));
        return new RestResp<>(nerResultRspList.orElse(Collections.emptyList()));
    }

    @ApiOperation("图谱实体识别")
    @PostMapping("ner/graph")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "form", value = "kw"),
            @ApiImplicitParam(name = "useConcept", required = true, dataType = "boolean", paramType = "form", value = "是否使用概念作为词典"),
            @ApiImplicitParam(name = "useEntity", required = true, dataType = "boolean", paramType = "form", value = "是否使用实体作为词典"),
            @ApiImplicitParam(name = "useAttr", required = true, dataType = "boolean", paramType = "form", value = "是否使用属性作为词典"),
    })
    public RestResp<PdDocument> recognition(@Valid @ApiIgnore RecognitionParameter param) {
        Function<SegmentReq, ApiReturn<PdDocument>> returnFunction = a -> nlpClient.nerGraph(param.getKgName(), a);
        ApiReturn<PdDocument> document = returnFunction
                .compose(NlpConverter::recognitionParameterToSegmentReq).apply(param);
        return new RestResp<>(document.getData());
    }

    @ApiOperation("图谱分词")
    @PostMapping("segment/graph")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "form", value = "kw"),
            @ApiImplicitParam(name = "useConcept", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用概念作为词典"),
            @ApiImplicitParam(name = "useEntity", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用实体作为词典"),
            @ApiImplicitParam(name = "useAttr", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用属性作为词典"),
    })
    public RestResp<PdDocument> segment(@Valid @ApiIgnore SegmentParametet param) {
        Function<SegmentReq, ApiReturn<List<GraphSegmentRsp>>> returnFunction = a -> nlpClient.graphSegment(param.getKgName(), a);
        Optional<List<GraphSegmentRsp>> entityBeans = returnFunction
                .compose(NlpConverter::segmentParametetToSegmentReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        PdDocument document = NlpConverter2.graphSegmentToPdDocument(entityBeans.orElse(Collections.emptyList()),param.getKw());
        return new RestResp<>(document);
    }

    @ApiOperation("文本语义标注")
    @PostMapping("annotation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "text", dataType = "string", paramType = "form", value = "待标注文本"),
            @ApiImplicitParam(name = "conceptIds", dataType = "string", paramType = "form", value = "标注范围，格式为json数组格式的概念列表"),
    })
    public RestResp<PdDocument> annotation(@Valid @ApiIgnore AnnotationParameter param) {
        Function<EntityLinkingReq, ApiReturn<List<TaggingItemRsp>>> returnFunction = a -> nlpClient.tagging(param.getKgName(), a);
        Optional<List<TaggingItemRsp>> opt = returnFunction
                .compose(NlpConverter::annotationParameterToEntityLinkingReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        PdDocument document = NlpConverter2.annotationToPdDocument(opt.orElse(Collections.emptyList()));
        return new RestResp<>(document);
    }

    @ApiOperation("语义关联")
    @PostMapping("/semantic/association")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "待识别语句"),
            @ApiImplicitParam(name = "size", defaultValue = "5", dataType = "int", paramType = "query", value = "返回结果数量"),
    })
    public RestResp<PdDocument> qaIntent(@Valid @ApiIgnore QaIntentParameter param) {
        Optional<IntentDataBeanRsp> intentDataBean = BasicConverter.apiReturnData(semanticClient.intent(param.getKgName(), param.getQuery(), param.getSize()));
        return new RestResp<>(NlpConverter2.intentDataBeanRspToPdDocument(intentDataBean.get()));
    }

    /**
     * 分词
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("中文分词")
    @PostMapping("segment/chinese")
    public RestResp<PdDocument> seg(@RequestParam @ApiParam(required = true) String input) {
        List<String> segList = hanLPService.seg(input);
        return new RestResp<>(NlpConverter2.segmentToPdDocument(segList));
    }

    /**
     * 词性标注
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("词性标注")
    @PostMapping("pos")
    public RestResp<PdDocument> pos(@ApiParam(required = true) @RequestParam("input") String input) {
        List<String> segList = hanLPService.pos(input);
        return new RestResp<>(NlpConverter2.posToPdDocument(segList));
    }

    /**
     * 命名实体识别
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("中文命名实体识别")
    @PostMapping("ner/chinere")
    public RestResp<PdDocument> ner(@ApiParam(required = true) @RequestParam("input") String input) {
        Map<String, List<String>> nerList = hanLPService.ner(input);
        return new RestResp<>(NlpConverter2.nerToPdDocument(nerList,input));
    }

    /**
     * 转换为简体中文
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为简体中文")
    @PostMapping("simplified/chinese")
    public RestResp<PdDocument> toSimplifiedChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(NlpConverter2.stringToPdDocument(hanLPService.toSimplifiedChinese(input)));
    }

    /**
     * 转换为简体中文
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为繁体中文")
    @PostMapping("traditional/chinese")
    public RestResp<PdDocument> toTraditionalChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(NlpConverter2.stringToPdDocument(hanLPService.toTraditionalChinese(input)));
    }

    /**
     * 转换为拼音
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为拼音")
    @PostMapping("phoneticize")
    public RestResp<PdDocument> phoneticize(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(NlpConverter2.phoneticToPdDocument(hanLPService.toPinyin(input)));
    }

    /**
     * 转换为拼音
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("关键词提取")
    @PostMapping("extract/keyword")
    public RestResp<PdDocument> extractKeyword(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(NlpConverter2.keywordToPdDocument(hanLPService.extractKeyword(input, size),input));
    }


    /**
     * 新词发现
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("新词发现")
    @PostMapping("extract/newword")
    public RestResp<PdDocument> extractNewWord(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(NlpConverter2.segmentToPdDocument(hanLPService.extractNewWord(input, size),input));
    }

    /**
     * 短语提取
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("短语提取")
    @PostMapping("extract/phrase")
    public RestResp<PdDocument> extractPhrase(@ApiParam(required = true) @RequestParam("input") String input,
                                                @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(NlpConverter2.segmentToPdDocument(hanLPService.extractPhrase(input, size),input));
    }

    /**
     * 自动摘要
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("自动摘要")
    @PostMapping("summarize")
    public RestResp<PdDocument> summarize(@ApiParam(required = true) @RequestParam("input") String input,
                                            @ApiParam(required = true, value = " 句子个数") @RequestParam("size") Integer size) {
        return new RestResp<>(NlpConverter2.segmentToPdDocument(hanLPService.summarize(input, size),input));
    }
}
