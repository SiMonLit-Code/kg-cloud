package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.AggAttrValueFrom;
import ai.plantdata.kg.api.pub.req.PromptListFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.PromptItemVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.constant.PromptResultTypeEnum;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.function.PromptSearchInterface;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 13:35
 */
public class PromptConverter extends BasicConverter {

    public static PromptListFrom promptReqReqToPromptListFrom(PromptReq req) {
        PromptListFrom from = new PromptListFrom();
        from.setConceptIds(req.getConceptIds());
        from.setIsCaseInsensitive(req.getCaseInsensitive());
        from.setIsFuzzy(req.getFuzzy());
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setIsInherit(req.getInherit());
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
        return JacksonUtils.readValue(query, Map.class);
    }

    public static List<PromptEntityRsp> esResultToEntity(@NotNull List<Map<String, Object>> maps) {
        return maps.stream().map(s -> {
            PromptEntityRsp entityBean = new PromptEntityRsp();
            Object conceptId = s.get("concept_id");
            if (conceptId != null) {
                try {
                    entityBean.setConceptId(Long.parseLong(conceptId.toString()));
                } catch (Exception e) {
                    List<Long> conceptIds = JacksonUtils.readValue(conceptId.toString(), new TypeReference<List<Long>>() {
                    });
                    if (conceptIds.size() > 0) {
                        entityBean.setConceptId(conceptIds.get(0));
                    }
                }
            }
            DefaultUtils.ifPresent(a -> entityBean.setId(Long.parseLong(s.get("entity_id").toString())), s.get("entity_id"));
            DefaultUtils.ifPresent(a -> entityBean.setName(s.get("name").toString()), s.get("name"));
            DefaultUtils.ifPresent(a -> entityBean.setName(s.get("meaning_tag").toString()), s.get("meaning_tag"));
            DefaultUtils.ifPresent(a -> entityBean.setName(s.get("score").toString()), s.get("score"));
            return entityBean;
        }).collect(Collectors.toList());
    }

    public static SeniorPromptRsp seniorPromptRspToPromptEntityRsp(@NonNull PromptEntityRsp promptEntityRsp) {
        SeniorPromptRsp promptRsp = new SeniorPromptRsp();
        BeanUtils.copyProperties(promptEntityRsp, promptRsp);
        return promptRsp;
    }

    public static PromptEntityRsp promptItemVoToPromptEntityRsp(@NonNull PromptItemVO item) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        entityRsp.setType(EntityTypeEnum.ENTITY);
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
        return entityRsp;
    }

    public static AggAttrValueFrom edgeAttrPromptReqToAggAttrValueFrom(EdgeAttrPromptReq req) {
        AggAttrValueFrom from = new AggAttrValueFrom();
        from.setSkip(req.getOffset());
        from.setLimit(req.getLimit());
        from.setAttrId(String.valueOf(req.getAttrId()));
        from.setIsPrivate(NumberUtils.INTEGER_ZERO);
        from.setSearchOption(req.getSearchOption());
        if (!CollectionUtils.isEmpty(req.getSorts())) {
            String sort = req.getSorts().get(1);
            Optional<SortTypeEnum> typeOpt = SortTypeEnum.parseByName(sort);
            typeOpt.ifPresent(sortTypeEnum -> from.setSortDirection(sortTypeEnum.getValue()));
        }
        if (from.getSortDirection() == null) {
            from.setSortDirection(SortTypeEnum.ASC.getValue());
        }
        return from;
    }
}
