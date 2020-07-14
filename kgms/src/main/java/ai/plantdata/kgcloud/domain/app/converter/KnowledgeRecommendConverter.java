package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.EntityAttributesObjectFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ai.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import ai.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReqList;
import ai.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 16:55
 */
@Slf4j
public class KnowledgeRecommendConverter extends BasicConverter{

    public static EntityAttributesObjectFrom reqToFrom(KnowledgeRecommendReqList req) {
        EntityAttributesObjectFrom from = new EntityAttributesObjectFrom();
        from.setEntityId(req.getEntityId());
        from.setAttributeIds(req.getAllowAttrs());
        consumerIfNoNull(req.getAllowAttrs(), a->{
            Map<Integer, Integer> directionMap = Maps.newHashMapWithExpectedSize(a.size());
            Map<Integer, Integer> sizeMap = Maps.newHashMapWithExpectedSize(a.size());
            for (int i = 0; i < a.size(); i++) {
                directionMap.put(a.get(i), req.getDirection());
                sizeMap.put(a.get(i), req.getSize());
            }
            from.setDirections(directionMap);
            from.setSizes(sizeMap);
        });
        return from;
    }

    public static List<ObjectAttributeRsp> voToRsp(@NonNull Map<Integer, Set<Long>> data, List<EntityVO> entityList) {
        List<ObjectAttributeRsp> attributeRspList = Lists.newArrayListWithCapacity(data.size());

        Map<Long, EntityVO> resultIndexMap = CollectionUtils.isEmpty(entityList) ? Collections.emptyMap()
                : entityList.stream().collect(Collectors.toMap(EntityVO::getId, Function.identity()));

        for (Map.Entry<Integer, Set<Long>> entry : data.entrySet()) {
            List<PromptEntityRsp> rspList = Lists.newArrayListWithCapacity(entry.getValue().size());
            for (Long entityId : entry.getValue()) {
                EntityVO entityVO = resultIndexMap.get(entityId);
                if (entityVO == null) {
                    continue;
                }
                rspList.add(new PromptEntityRsp(entityVO.getId(), entityVO.getName(), entityVO.getMeaningTag(), entityVO.getConceptId(), EntityTypeEnum.ENTITY,entityVO.getImageUrl()));
            }
            attributeRspList.add(new ObjectAttributeRsp(entry.getKey(), rspList));
        }

        return attributeRspList;
    }

    public static List<ObjectAttributeRsp> graphVOToRsp(GraphVO graphVO,Integer distance) {

        if(CollectionUtils.isEmpty(graphVO.getRelationList())){
            return Collections.emptyList();
        }

        SimpleEntity entity = graphVO.getEntityList().get(0);

        List<SimpleRelation> relations = graphVO.getRelationList();

        List<Long> oneEntityIds = relations.stream().map(relation ->{
            if(relation.getFrom().equals(entity.getId())){
                return relation.getTo();
            }else if(relation.getTo().equals(entity.getId())){
                return relation.getFrom();
            }else{
                return 0L;
            }
        }).collect(Collectors.toList());

        Map<Integer,Set<Long>> map = new HashMap<>();

        relations.forEach(relation ->{
            if(!relation.getFrom().equals(entity.getId()) && !relation.getTo().equals(entity.getId())){

                Set<Long> entIds = map.get(relation.getAttrId());
                if(entIds == null){
                    entIds = new HashSet<>();
                    map.put(relation.getAttrId(),entIds);
                }

                if(oneEntityIds.contains(relation.getFrom())){
                    entIds.add(relation.getTo());
                }else if(oneEntityIds.contains(relation.getTo())){
                    entIds.add(relation.getFrom());
                }
            }
        });

        if(CollectionUtils.isEmpty(map)){
            return Collections.emptyList();
        }

        Map<Long,SimpleEntity> entityMap = graphVO.getEntityList().stream().collect(Collectors.toMap(SimpleEntity::getId,Function.identity()));

        return ent2ObjectAttributeRsp(entityMap,map);
    }

    private static List<ObjectAttributeRsp> ent2ObjectAttributeRsp(Map<Long, SimpleEntity> entityMap, Map<Integer, Set<Long>> map) {

        List<ObjectAttributeRsp> rsList = new ArrayList<>();
        for(Map.Entry<Integer,Set<Long>> entry : map.entrySet()){

            Set<Long> entIds = entry.getValue();

            List<PromptEntityRsp> entityRsps = entIds.stream().map(id -> simleEntity2PromptEntityRsp(entityMap.get(id))).collect(Collectors.toList());

            rsList.add(new ObjectAttributeRsp(entry.getKey(),entityRsps));
        }

        return rsList;
    }

    private static PromptEntityRsp simleEntity2PromptEntityRsp(SimpleEntity simpleEntity) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        entityRsp.setName(simpleEntity.getName());
        entityRsp.setConceptId(simpleEntity.getConceptId());
        entityRsp.setId(simpleEntity.getId());
        entityRsp.setMeaningTag(simpleEntity.getMeaningTag());
        entityRsp.setType(EntityTypeEnum.parseById(simpleEntity.getType()));
        entityRsp.setImageUrl(simpleEntity.getImageUrl());
        entityRsp.setMetaData(simpleEntity.getMetaData());

        return entityRsp;
    }
}
