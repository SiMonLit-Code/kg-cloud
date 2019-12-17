package com.plantdata.kgcloud.plantdata.converter.app;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.PromptParameter;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/16 19:55
 */
public class PromptConverter extends BasicConverter {

    public static PromptReq promptParameterToPromptReq(PromptParameter promptParam) {
        PromptReq promptReq = new PromptReq();
        promptReq.setCaseInsensitive(promptParam.getIsCaseInsensitive());
        promptReq.setConceptIds(promptParam.getAllowTypes());
        promptReq.setConceptKeys(promptParam.getAllowTypesKey());
        promptReq.setFuzzy(promptParam.getIsFuzzy());
        promptReq.setInherit(promptParam.getIsInherit());
        promptReq.setKw(promptParam.getKw());
        promptReq.setOpenExportDate(promptParam.getOpenExportDate());
        promptReq.setPage(promptParam.getPageNo());
        promptReq.setPromptType(promptReq.getPromptType());
        promptReq.setSize(promptReq.getSize());
        setIfNoNull(promptParam.getSort(), a -> promptReq.setSort(a.getValue()));
        return promptReq;
    }

    public static EntityBean promptEntityRspToEntityBean(PromptEntityRsp newEntity) {
        EntityBean entityBean = new EntityBean();
        entityBean.setMeaningTag(newEntity.getMeaningTag());
        entityBean.setConceptId(newEntity.getConceptId());
        entityBean.setId(newEntity.getId());
        entityBean.setQa(newEntity.getQa());
        entityBean.setScore(newEntity.getScore());
        setIfNoNull(newEntity.getType(), a -> entityBean.setType(a.getValue()));
        return entityBean;

    }
}
