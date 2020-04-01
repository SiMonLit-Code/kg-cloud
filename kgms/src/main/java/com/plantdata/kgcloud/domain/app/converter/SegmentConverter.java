package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.SemanticSegFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 10:40
 */
public class SegmentConverter extends BasicConverter {

    public static SemanticSegFrom segmentReqToSemanticSegFrom(SegmentReq segmentReq) {
        SemanticSegFrom segFrom = new SemanticSegFrom();
        consumerIfNoNull(segmentReq.getUseEntity(), segFrom::setEntity);
        consumerIfNoNull(segmentReq.getUseAttr(), segFrom::setAttr);
        consumerIfNoNull(segmentReq.getUseConcept(), segFrom::setConcept);
        segFrom.setInput(segmentReq.getKw());
        return segFrom;
    }

    public static List<SegmentEntityRsp> entityVoToSegmentEntityDto(@NotNull List<EntityVO> entityList, Map<Long, Double> scoreMap,
                                                                    Map<Long, String> wordMap) {

        return entityList.stream().map(a -> {
            SegmentEntityRsp entityDto = EntityConverter.entityVoToBasicEntityRsp(a, new SegmentEntityRsp());
            entityDto.setWord(wordMap.get(a.getId()));
            BasicConverter.consumerIfNoNull(scoreMap.get(a.getId()), entityDto::setScore);
            entityDto.setSynonym(a.getSynonyms());
            return entityDto;
        }).collect(Collectors.toList());
    }

    public static QaAnswerDataRsp AnswerDataRspToQaAnswerDataRsp(@NotNull AnswerDataRsp dataRsp) {
        QaAnswerDataRsp qaAnswerDataRsp = new QaAnswerDataRsp();
        qaAnswerDataRsp.setHit(dataRsp.getHit());
        qaAnswerDataRsp.setArray(dataRsp.getArray());
        qaAnswerDataRsp.setText(dataRsp.getText());
        qaAnswerDataRsp.setType(dataRsp.getType());
        qaAnswerDataRsp.setWord(dataRsp.getWord());
        return qaAnswerDataRsp;
    }
}
