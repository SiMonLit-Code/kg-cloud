package com.plantdata.kgcloud.plantdata.converter.app;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.bean.AttrPromtRemoteBean;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.common.MongoQueryConverter;
import com.plantdata.kgcloud.plantdata.req.app.AttrPromptParameter;
import com.plantdata.kgcloud.plantdata.req.app.PromptParameter;
import com.plantdata.kgcloud.plantdata.req.app.SeniorPromptParameter;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.entity.ImportEntityBean;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.CompareFilterReq;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/16 19:55
 */
public class PromptConverter extends BasicConverter {

    public static EdgeAttrPromptReq attrPromptParameterToEdgeAttrPromptReq(@NonNull AttrPromptParameter param) {
        EdgeAttrPromptReq promptReq = new EdgeAttrPromptReq();
        promptReq.setAttrId(param.getAttrId());
        promptReq.setAttrKey(param.getAttrKey());
        consumerIfNoNull(param.getSearchOption(), a -> promptReq.setCompareFilter(JacksonUtils.readValue(a, CompareFilterReq.class)));
        promptReq.setDataType(param.getDataType());
        promptReq.setKw(param.getKw());
        promptReq.setPage(param.getPageNo());
        promptReq.setSize(param.getPageSize());
        promptReq.setSeqNo(param.getSeqNo());
        return promptReq;
    }

    public static AttrPromtRemoteBean edgeAttributeRspToAttrPromtRemoteBean(@NonNull EdgeAttributeRsp rsp) {
        AttrPromtRemoteBean remoteBean = new AttrPromtRemoteBean();
        remoteBean.setKey(rsp.getKey());
        remoteBean.setNum(remoteBean.getNum());
        return remoteBean;
    }

    public static PromptReq promptParameterToPromptReq(@NonNull PromptParameter promptParam) {
        PromptReq promptReq = new PromptReq();
        promptReq.setCaseInsensitive(promptParam.getIsCaseInsensitive());
        consumerIfNoNull(promptParam.getAllowTypes(), a -> promptReq.setConceptIds(listToRsp(a, Long::valueOf)));
        promptReq.setConceptKeys(promptParam.getAllowTypesKey());
        promptReq.setFuzzy(promptParam.getIsFuzzy());
        promptReq.setInherit(promptParam.getIsInherit());
        promptReq.setKw(promptParam.getKw());
        promptReq.setOpenExportDate(promptParam.getOpenExportDate());
        promptReq.setPage(promptParam.getPageNo());
        promptReq.setPromptType(promptParam.getPromptType());
        promptReq.setSize(promptParam.getPageSize());
        consumerIfNoNull(promptParam.getSort(), a -> promptReq.setSort(SortTypeEnum.parseByName(a).orElse(SortTypeEnum.DESC).getValue()));
        return promptReq;
    }

    public static EntityBean promptEntityRspToEntityBean(@NonNull PromptEntityRsp newEntity) {
        EntityBean entityBean = new EntityBean();
        entityBean.setName(newEntity.getName());
        entityBean.setMeaningTag(newEntity.getMeaningTag());
        entityBean.setClassId(newEntity.getConceptId());
        entityBean.setConceptId(newEntity.getConceptId());
        entityBean.setClassIdList(Lists.newArrayList(newEntity.getConceptId()));
        entityBean.setId(newEntity.getId());
        consumerIfNoNull(newEntity.isQa(), entityBean::setQa);
        entityBean.setScore(newEntity.getScore());
        consumerIfNoNull(newEntity.getType(), a -> entityBean.setType(a.getValue()));
        return entityBean;
    }

    public static SeniorPromptReq seniorPromptParameterToSeniorPromptReq(@NonNull SeniorPromptParameter param) {
        SeniorPromptReq promptReq = new SeniorPromptReq();
        promptReq.setConceptId(param.getConceptId());
        promptReq.setConceptKey(param.getConceptKey());
        promptReq.setKw(param.getKw());
        promptReq.setOpenExportDate(param.getOpenExportDate());
        promptReq.setPage(param.getPageNo());
        consumerIfNoNull(param.getQuery(), a -> promptReq.setQuery(listToRsp(a, MongoQueryConverter::entityScreeningBeanToEntityQueryFiltersReq)));
        promptReq.setSize(param.getPageSize());
        return promptReq;
    }

    public static ImportEntityBean seniorPromptRspToSeniorPromptRsp(@NonNull SeniorPromptRsp newRsp) {
        ImportEntityBean rsp = new ImportEntityBean();
        rsp.setAbs(newRsp.getAbs());
        rsp.setConceptId(newRsp.getConceptId());
        rsp.setId(newRsp.getId());
        rsp.setName(newRsp.getName());
        rsp.setMeaningTag(newRsp.getMeaningTag());
        consumerIfNoNull(newRsp.getSynonyms(), rsp::setSynonyms);
        consumerIfNoNull(newRsp.getImg(), a -> rsp.setImageUrl(a.getHref()));
        consumerIfNoNull(newRsp.getAttrMap(), a -> rsp.setAttributes(BasicConverter.keyIntToStr(a)));
        return rsp;
    }
}
