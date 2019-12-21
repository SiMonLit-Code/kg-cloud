package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.SemanticSegFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import com.plantdata.kgcloud.domain.app.dto.SegmentEntityDTO;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;

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

    public static List<SegmentEntityDTO> entityVoToSegmentEntityDto(@NotNull List<EntityVO> entityList, Map<Long, Double> scoreMap,
                                                                    Map<Long, String> wordMap) {

        return entityList.stream().map(a -> {
            SegmentEntityDTO entityDto = EntityConverter.voToBasicEntityRsp(a, new SegmentEntityDTO());
            entityDto.setWord(wordMap.get(a.getId()));
            entityDto.setScore(scoreMap.get(a.getId()));
            entityDto.setSynonym(a.getSynonyms());
            return entityDto;
        }).collect(Collectors.toList());
    }
}
