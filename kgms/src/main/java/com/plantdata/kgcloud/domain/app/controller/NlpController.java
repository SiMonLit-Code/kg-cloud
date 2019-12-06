package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.EntityLinkingFrom;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.converter.graph.RestCopyConverter;
import com.plantdata.kgcloud.domain.app.service.NlpService;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 16:54
 */
@RestController
@RequestMapping("app/nlp")
@Api(tags = "自然语言处理")
public class NlpController {

    @Autowired
    private NlpService nlpService;
    @Autowired
    private EntityApi entityApi;

    /**
     * 命名实体识别
     *
     * @return 词列表
     */
    @ApiOperation("中文命名实体识别")
    @PostMapping("ner")
    public ApiReturn<List<NerResultRsp>> namedEntityRecognition(@Valid @RequestBody NerReq nerReq, @ApiIgnore BindingResult bindingResult) {
        try {
            return ApiReturn.success(nlpService.namedEntityRecognition(nerReq));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiReturn.success(Collections.emptyList());
    }

    @ApiOperation("图谱分词")
    @PostMapping("segment/graph/{kgName}")
    public ApiReturn<List<GraphSegmentRsp>> graphSegment(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                         @Valid @ApiIgnore SegmentReq segmentReq, @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(nlpService.graphSegment(kgName, segmentReq));
    }

    @ApiOperation("语义标注")
    @PostMapping("annotation")
    public ApiReturn<List<TaggingItemRsp>> tagging(@RequestParam("kgName") String kgName, @RequestBody EntityLinkingReq linkingFrom) {
        EntityLinkingFrom entityLinkingFrom = new EntityLinkingFrom();
        entityLinkingFrom.setConceptIds(linkingFrom.getConceptIds());
        entityLinkingFrom.setText(linkingFrom.getText());
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(entityApi.tagging(kgName, entityLinkingFrom), Collections.emptyList()));
    }
}
