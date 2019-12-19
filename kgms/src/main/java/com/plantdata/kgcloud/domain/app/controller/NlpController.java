package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.EntityLinkingFrom;
import ai.plantdata.kg.api.pub.resp.TaggingItemVO;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.app.service.NlpService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 16:54
 */
@RestController
@RequestMapping("app/nlp")
public class NlpController implements SdkOpenApiInterface {

    @Autowired
    private NlpService nlpService;
    @Autowired
    private EntityApi entityApi;


    /**
     * 命名实体识别
     *
     * @return 词列表
     */
    @ApiOperation("命名实体识别")
    @PostMapping("ner")
    public ApiReturn<List<NerResultRsp>> namedEntityRecognition(@Valid @RequestBody NerReq nerReq) {
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
                                                         @Valid @ApiIgnore SegmentReq segmentReq) {
        return ApiReturn.success(nlpService.graphSegment(kgName, segmentReq));
    }

    @ApiOperation("语义标注")
    @PostMapping("annotation/{kgName}")
    public ApiReturn<List<TaggingItemRsp>> tagging(@ApiParam("图谱名称") @PathVariable("kgName") String kgName, @RequestBody EntityLinkingReq linkingFrom) {
        EntityLinkingFrom entityLinkingFrom = new EntityLinkingFrom();
        entityLinkingFrom.setConceptIds(linkingFrom.getConceptIds());
        entityLinkingFrom.setText(linkingFrom.getText());
        Optional<List<TaggingItemVO>> opt = RestRespConverter.convert(entityApi.tagging(kgName, entityLinkingFrom));
        return ApiReturn.success(RestCopyConverter.copyToNewList(opt.orElse(Collections.emptyList()), TaggingItemRsp.class));
    }


}
