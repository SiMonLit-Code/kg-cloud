package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.AggAttrValueFrom;
import ai.plantdata.kg.api.pub.req.PromptListFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.domain.common.util.EnumUtils;
import ai.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import ai.plantdata.kgcloud.sdk.constant.PromptResultTypeEnum;
import ai.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import ai.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import ai.plantdata.kgcloud.sdk.req.app.PromptReq;
import ai.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import ai.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 13:35
 */
public class PromptConverter extends BasicConverter {

    public static SearchByAttributeFrom seniorPromptReqToSearchByAttributeFrom(@NonNull SeniorPromptReq promptReq, List<Map<String, Object>> queryMapList) {
        SearchByAttributeFrom attributeFrom = new SearchByAttributeFrom();
        consumerIfNoNull(queryMapList, a -> attributeFrom.setKvMap(a.get(0)));
        consumerIfNoNull(promptReq.getKw(), attributeFrom::setEntityName);
        attributeFrom.setInherit(true);
        attributeFrom.setFuzzy(promptReq.isFuzzy());
        consumerIfNoNull(promptReq.getConceptId(), a -> attributeFrom.setConceptIds(Lists.newArrayList(a)));
        return attributeFrom;
    }

    public static PromptListFrom promptReqReqToPromptListFrom(PromptReq req) {
        PromptListFrom from = new PromptListFrom();
        from.setConceptIds(req.getConceptIds());
        if(StringUtils.hasText(req.getKw())){
            req.setKw(req.getKw().replace("\\","\\\\"));
        }
        from.setText(req.getKw());
        from.setCaseInsensitive(req.getCaseInsensitive() == null ? true : req.getCaseInsensitive());
        from.setFuzzy(req.isFuzzy());
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setInherit(req.getInherit());
        Optional<PromptResultTypeEnum> enumObject = EnumUtils.getEnumObject(PromptResultTypeEnum.class, req.getType());
        PromptResultTypeEnum resultType = enumObject.orElse(PromptResultTypeEnum.ENTITY);
        if (PromptResultTypeEnum.CONCEPT_ENTITY == resultType) {
            from.setTypes(Lists.newArrayList(PromptResultTypeEnum.SYNONYM.getId(), PromptResultTypeEnum.CONCEPT.getId(), PromptResultTypeEnum.ENTITY.getId()));
        } else {
            from.setTypes(Lists.newArrayList(PromptResultTypeEnum.SYNONYM.getId(), resultType.getId()));
        }
        return from;
    }



    public static PromptEntityRsp promptItemVoToPromptEntityRsp(@NonNull PromptItemVO item) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        consumerIfNoNull(item.getType(), a -> entityRsp.setType(EntityTypeEnum.parseById(a)));
        entityRsp.setName(item.getName());
        entityRsp.setMeaningTag(item.getMeaningTag());
        entityRsp.setId(item.getId());
        entityRsp.setConceptId(item.getClassId());
        return entityRsp;
    }

    public static SeniorPromptRsp entityVoToSeniorPromptRsp(@NonNull EntityVO item) {
        SeniorPromptRsp entityRsp = new SeniorPromptRsp();
        entityRsp.setType(EntityTypeEnum.ENTITY);
        entityRsp.setName(item.getName());
        entityRsp.setMeaningTag(item.getMeaningTag());
        entityRsp.setId(item.getId());
        entityRsp.setConceptId(item.getConceptId());
        entityRsp.setAbs(item.getAbs());
        ImageConverter.stringT0Image(item.getImageUrl()).ifPresent(entityRsp::setImg);
        entityRsp.setSynonyms(item.getSynonyms());
        consumerIfNoNull(item.getDataAttributes(), a -> entityRsp.setAttrMap(keyStrToInt(a)));
        return entityRsp;
    }


    public static AggAttrValueFrom edgeAttrPromptReqToAggAttrValueFrom(EdgeAttrPromptReq req) {
        AggAttrValueFrom from = new AggAttrValueFrom();
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setAttrId(String.valueOf(req.getAttrId()));
        from.setIsPrivate(NumberUtils.INTEGER_ZERO);
        from.setSearchOption(req.getSearchOption());
        Integer sortDirection = SortTypeEnum.parseByValue(from.getSortDirection()).orElse(SortTypeEnum.DESC).getValue();
        from.setSortDirection(sortDirection);
        return from;
    }
}
