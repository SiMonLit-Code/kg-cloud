package com.plantdata.kgcloud.domain.app.service;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:38
 */
public interface NlpService {

    /**
     * 中文命名实体识别
     *
     * @param nerReq .
     * @return .
     * @throws Exception .
     */
    List<NerResultRsp> namedEntityRecognition(NerReq nerReq) throws Exception;

    /**
     * 图谱分词
     *
     * @param kgName     .
     * @param segmentReq .
     * @return .
     */
    List<GraphSegmentRsp> graphSegment(String kgName, SegmentReq segmentReq);

    /**
     * 图谱实体识别
     *
     * @param kgName
     * @param segmentReq
     * @return
     */
    List<SegmentEntityRsp> segment(String kgName, SegmentReq segmentReq);

    /**
     * 图谱实体识别2
     *
     * @param kgName
     * @param segmentReq
     * @return
     */
    PdDocument segment2(String kgName, SegmentReq segmentReq);
}
