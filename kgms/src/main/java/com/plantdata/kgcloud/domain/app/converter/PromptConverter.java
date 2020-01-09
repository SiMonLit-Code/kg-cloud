package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.AggAttrValueFrom;
import ai.plantdata.kg.api.pub.req.PromptListFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.PromptResultTypeEnum;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.req.app.function.PromptSearchInterface;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 13:35
 */
public class PromptConverter extends BasicConverter {

    public static SearchByAttributeFrom seniorPromptReqToSearchByAttributeFrom(@NonNull SeniorPromptReq promptReq, List<Map<String, Object>> queryMapList) {
        SearchByAttributeFrom attributeFrom = new SearchByAttributeFrom();
        consumerIfNoNull(queryMapList, a -> attributeFrom.setKvMap(a.get(0)));
        attributeFrom.setEntityName(promptReq.getKw());
        attributeFrom.setInherit(true);
        attributeFrom.setConceptIds(Lists.newArrayList(promptReq.getConceptId()));
        return attributeFrom;
    }

    public static PromptListFrom promptReqReqToPromptListFrom(PromptReq req) {
        PromptListFrom from = new PromptListFrom();
        from.setConceptIds(req.getConceptIds());
        from.setCaseInsensitive(req.getCaseInsensitive() == null ? false : req.getCaseInsensitive());
        from.setFuzzy(req.isFuzzy());
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setInherit(req.getInherit());
        from.setText(req.getKw());
        PromptResultTypeEnum resultType = req.getType() == null ? PromptResultTypeEnum.CONCEPT_ENTITY : PromptResultTypeEnum.parseWithDefault(req.getType());
        from.setType(resultType.getId());
        return from;
    }

    public static Map<String, Object> buildEsParam(PromptSearchInterface promptReq) {
        String concept;
        if (promptReq.getInherit()) {
            concept = "concept_list";
        } else {
            concept = "concept_id";
        }
        String filter = "";
        List<Long> allowTypes = promptReq.getConceptIds();
        if (allowTypes != null && !allowTypes.isEmpty()) {
            filter = ",\"filter\":{\"terms\":{\"" + concept + "\":" + JacksonUtils.writeValueAsString(allowTypes) + "}}";
        }

        String query;
        if (EsUtils.isChinese(promptReq.getKw())) {
            query = "{\"bool\":{\"must\":{\"term\":{\"name\":\"" + promptReq.getKw() + "\"}}" + filter + "}}";
        } else {
            query = "{\"bool\":{\"must\":{\"prefix\":{\"name.pinyin\":\"" + promptReq.getKw() + "\"}}" + filter + "}}";
        }
        Map<String, Object> queryMap = JacksonUtils.readValue(query, new TypeReference<Map<String, Object>>() {
        });
        return DefaultUtils.oneElMap("query", queryMap);
    }

    public static PromptEntityRsp esResultToEntity(@NotNull Map<String, Object> map) {

        PromptEntityRsp entityBean = new PromptEntityRsp();
        Object conceptId = map.get("concept_id");
        consumerIfNoNull(conceptId, a -> {
            try {
                entityBean.setConceptId(Long.parseLong(conceptId.toString()));
            } catch (Exception e) {
                List<Long> conceptIds = JacksonUtils.readValue(conceptId.toString(), new TypeReference<List<Long>>() {
                });
                consumerIfNoNull(conceptIds, b -> entityBean.setConceptId(b.get(0)));
            }
        });
        DefaultUtils.ifPresent(a -> entityBean.setId(Long.parseLong(map.get("entity_id").toString())), map.get("entity_id"));
        DefaultUtils.ifPresent(a -> entityBean.setName(map.get("name").toString()), map.get("name"));
        DefaultUtils.ifPresent(a -> entityBean.setName(map.get("meaning_tag").toString()), map.get("meaning_tag"));
        DefaultUtils.ifPresent(a -> entityBean.setName(map.get("score").toString()), map.get("score"));
        return entityBean;
    }

    public static SeniorPromptRsp seniorPromptRspToPromptEntityRsp(@NonNull PromptEntityRsp promptEntityRsp) {
        SeniorPromptRsp promptRsp = new SeniorPromptRsp();
        BeanUtils.copyProperties(promptEntityRsp, promptRsp);
        return promptRsp;
    }

    public static PromptEntityRsp promptItemVoToPromptEntityRsp(@NonNull PromptItemVO item) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        if (item.getType() != null) {
            EntityTypeEnum typeEnum = EntityTypeEnum.parseById(item.getType());
            consumerWithDefault(EntityTypeEnum.ENTITY, typeEnum, entityRsp::setType);
        }
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
        from.setSortDirection(SortTypeEnum.DESC.getValue());
//        if (!CollectionUtils.isEmpty(req.getSorts()) && req.getSorts().size() > 0) {
//            String sort = req.getSorts().get(0);
//            String[] split = sort.split(":");
//            if (split.length == 2) {
//                Optional<SortTypeEnum> typeOpt = SortTypeEnum.parseByName(split[1]);
//                typeOpt.ifPresent(sortTypeEnum -> from.setSortDirection(sortTypeEnum.getValue()));
//            }
//        }
        consumerIfNoNull(from.getSortDirection(), a -> SortTypeEnum.parseByValue(a).orElse(SortTypeEnum.DESC).getValue());
        return from;
    }
}
