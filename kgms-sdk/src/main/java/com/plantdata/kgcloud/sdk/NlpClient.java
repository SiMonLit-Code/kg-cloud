package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 18:03
 */
@FeignClient(value = "kgms", path = "nlp", contextId = "nlpClient")
public interface NlpClient {
    /**
     * 中文命名实体识别
     *
     * @param nerReq req
     * @return ...
     */
    @PostMapping("ner")
    ApiReturn<List<NerResultRsp>> namedEntityRecognition(@RequestBody NerReq nerReq);

    /**
     * graph分词
     *
     * @param kgName     图谱名称
     * @param segmentReq req
     * @return 。
     */
    @PostMapping("segment/graph/{kgName}")
    ApiReturn<List<GraphSegmentRsp>> graphSegment(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                  @RequestBody SegmentReq segmentReq);

    /**
     * 语义标注
     *
     * @param kgName     图谱名称
     * @param linkingReq .
     * @return .
     */
    @PostMapping("annotation")
    ApiReturn<List<TaggingItemRsp>> tagging(@RequestParam("kgName") String kgName, @RequestBody EntityLinkingReq linkingReq);
}
