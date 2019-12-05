package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.PromptListFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import com.plantdata.kgcloud.constant.PromptResultTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 13:35
 */
public class PromptConverter {

    public static PromptListFrom reqToRemote(PromptReq req){
        PromptListFrom from = new PromptListFrom();
        from.setConceptIds(req.getConceptIds());
        from.setIsCaseInsensitive(req.getIsCaseInsensitive());
        from.setIsFuzzy(req.getIsFuzzy());
        from.setIsInherit(req.getIsInherit());
        from.setText(req.getKw());
        PromptResultTypeEnum resultType = req.getType()==null?PromptResultTypeEnum.CONCEPT_ENTITY:PromptResultTypeEnum.parseWithDefault(req.getType());
        from.setType(resultType.getId());
        return from;
    }

    public static List<SeniorPromptRsp>  rspToBelow(@NonNull List<PromptEntityRsp> entityRspList){
        return entityRspList.stream().map(a->{
            SeniorPromptRsp promptRsp = new SeniorPromptRsp();
            BeanUtils.copyProperties(a,promptRsp);
            return promptRsp;
        }).collect(Collectors.toList());
    }

    public static List<PromptEntityRsp> voToRsp(@NonNull List<PromptItemVO>  itemList){
        return itemList.stream().map(item->{
           PromptEntityRsp entityRsp = new PromptEntityRsp();
           entityRsp.setType(EntityTypeEnum.parseById(item.getType()));
           entityRsp.setName(item.getName());
           entityRsp.setMeaningTag(item.getMeaningTag());
           entityRsp.setId(item.getId());
           entityRsp.setConceptId(item.getClassId());
           return entityRsp;
       }).collect(Collectors.toList());
    }

    public static List<SeniorPromptRsp> voToSeniorRsp(@NonNull List<EntityVO>  entityList){
        return  entityList.parallelStream().map(item->{
            SeniorPromptRsp entityRsp = new SeniorPromptRsp();
            entityRsp.setType(EntityTypeEnum.ENTITY);
            entityRsp.setName(item.getName());
            entityRsp.setMeaningTag(item.getMeaningTag());
            entityRsp.setId(item.getId());
            entityRsp.setConceptId(item.getConceptId());
            entityRsp.setAbs(item.getAbs());
            ImageConverter.stringT0Image(item.getImageUrl()).ifPresent(entityRsp::setImg);
            entityRsp.setSynonyms(item.getSynonyms());
            return entityRsp;
        }).collect(Collectors.toList());
    }
}
