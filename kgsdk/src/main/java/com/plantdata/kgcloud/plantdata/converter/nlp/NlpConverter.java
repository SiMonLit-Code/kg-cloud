package com.plantdata.kgcloud.plantdata.converter.nlp;

import com.plantdata.kgcloud.plantdata.converter.common.EntityConverter;
import com.plantdata.kgcloud.plantdata.req.entity.SegmentEntityBean;
import com.plantdata.kgcloud.plantdata.req.nlp.AnnotationParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.RecognitionParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.SegmentParametet;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 16:08
 */
public class NlpConverter {

    public static SegmentReq recognitionParameterToSegmentReq(@NonNull RecognitionParameter param) {
        SegmentReq segmentReq = new SegmentReq();
        segmentReq.setUseEntity(param.getUseEntity());
        segmentReq.setUseConcept(param.getUseConcept());
        segmentReq.setUseAttr(param.getUseAttr());
        segmentReq.setKw(param.getKw());
        return segmentReq;
    }

    public static SegmentEntityBean segmentEntityRspToSegmentEntityBean(@NonNull SegmentEntityRsp entityRsp) {
        SegmentEntityBean entityBean = EntityConverter.basicEntityRspToEntityBean(entityRsp, new SegmentEntityBean());
        entityBean.setScore(entityRsp.getScore());
        entityBean.setWord(entityRsp.getWord());
        entityBean.setSynonym(entityRsp.getSynonym());
        return entityBean;
    }

    public static SegmentReq segmentParametetToSegmentReq(@NonNull SegmentParametet param) {
        SegmentReq segmentReq = new SegmentReq();
        segmentReq.setUseEntity(param.getUseEntity());
        segmentReq.setUseConcept(param.getUseConcept());
        segmentReq.setUseAttr(param.getUseAttr());
        segmentReq.setKw(param.getKw());
        return segmentReq;
    }

    public static EntityLinkingReq annotationParameterToEntityLinkingReq(@NonNull AnnotationParameter param) {
        EntityLinkingReq linkingReq = new EntityLinkingReq();
        linkingReq.setText(param.getText());
        linkingReq.setConceptIds(param.getConceptIds());
        return linkingReq;
    }

}
