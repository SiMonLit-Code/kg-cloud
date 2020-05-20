package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.SemanticApi;
import ai.plantdata.kg.api.pub.req.SemanticSegFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.SemanticSegWordVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiekn.ierule.service.RuleModelService;
import com.hiekn.ierule.service.RuleModelServiceImpl;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdAttribute;
import com.hiekn.pddocument.bean.element.PdConcept;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdValue;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.SegmentConverter;
import com.plantdata.kgcloud.domain.app.service.NlpService;
import com.plantdata.kgcloud.domain.app.util.HanLPUtil;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.nlp.ModelConfig;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NamedEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:38
 */
@Service
public class NlpServiceImpl implements NlpService {

    private RuleModelService ruleModelService = new RuleModelServiceImpl();
    @Autowired
    private SemanticApi semanticApi;
    @Autowired
    private EntityApi entityApi;

    @Override
    public List<NerResultRsp> namedEntityRecognition(NerReq nerReq) throws Exception {

        String input = nerReq.getInput();
        NerReq.NerTagConfigReq tagConfigSeq = nerReq.getConfig();
        if (tagConfigSeq == null) {
            return Collections.emptyList();
        }
        List<NerReq.TagConfigReq> tagConfigList = tagConfigSeq.getTagConfigList();
        if (CollectionUtils.isEmpty(tagConfigList)) {
            return Collections.emptyList();
        }
        List<NerResultRsp> resultList = new ArrayList<>();
        Map<Integer, ModelConfig> modelConfigMap = Maps.newHashMap();

        tagConfigList.forEach(a -> BasicConverter.listConsumerIfNoNull(a.getModelConfigList(), b -> modelConfigMap.put(b.getId(), b)));

        Map<Integer, List<NamedEntityRsp>> modelResultList = Maps.newHashMap();
        for (Integer modelId : modelConfigMap.keySet()) {
            ModelConfig modelConfig = modelConfigMap.get(modelId);
            List<NamedEntityRsp> entityList = modelConfig.getType() == 0 ? HanLPUtil.ner(input) : run(input, modelConfig);
            modelResultList.put(modelId, entityList);
        }

        for (NerReq.TagConfigReq tagConfig : tagConfigList) {
            String name = tagConfig.getName();
            List<ModelConfig> modelConfigList = tagConfig.getModelConfigList();
            if (modelConfigList == null) {
                continue;
            }
            List<Boolean> flagList = new ArrayList<>(input.length());
            List<NamedEntityRsp> finalEntityList = new ArrayList<>();
            for (int i = 0; i < input.length(); i++) {
                flagList.add(true);
            }

            for (ModelConfig modelConfig : modelConfigList) {
                List<NamedEntityRsp> entityList = modelResultList.get(modelConfig.getId());
                List<NamedEntityRsp> filteredEntityList = getFilteredEntityList(entityList, name, modelConfig.getLabels());
                BasicConverter.consumerIfNoNull(filteredEntityList.stream().filter(a -> qualified(a, flagList)).collect(Collectors.toList()), finalEntityList::addAll);
            }
            if (finalEntityList.size() > 0) {
                NerResultRsp nerResultBean = new NerResultRsp();
                nerResultBean.setTag(name);
                nerResultBean.setEntityList(finalEntityList);
                resultList.add(nerResultBean);
            }

        }
        return resultList;
    }
    @Override
    public List<GraphSegmentRsp> graphSegment(String kgName, SegmentReq segmentReq) {
        List<SegmentEntityRsp> list = segment(kgName, segmentReq);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(s -> new GraphSegmentRsp(s.getName(), s.getScore())).collect(Collectors.toList());

    }

    private boolean qualified(NamedEntityRsp entity, List<Boolean> flagList) {
        boolean qualified = true;
        int start = entity.getPos();
        int end = start + entity.getName().length();
        for (int i = start; i < end; i++) {
            if (!flagList.get(i)) {
                qualified = false;
                break;
            }
        }
        if (qualified) {
            for (int i = start; i < end; i++) {
                flagList.set(i, false);
            }
        }
        return qualified;
    }

    private List<NamedEntityRsp> getFilteredEntityList(List<NamedEntityRsp> entityList, String name, Set<String> labels) {
        List<NamedEntityRsp> resultList = new ArrayList<>();
        BasicConverter.listConsumerIfNoNull(entityList, a -> {
            if (labels.contains(a.getTag())) {
                NamedEntityRsp entity1 = new NamedEntityRsp();
                entity1.setTag(name);
                entity1.setPos(a.getPos());
                entity1.setName(a.getName());
                resultList.add(entity1);
            }
        });
        return resultList;
    }

    private List<NamedEntityRsp> run(String input, ModelConfig modelConfig) throws Exception {

        String ruleConfig = modelConfig.getConfig();
        List<PdDocument> pdDocuments = ruleModelService.extractByConfigWithoutSplit(input, ruleConfig);
        if (pdDocuments == null || pdDocuments.size() != 1) {
            return Collections.emptyList();
        }

        List<PdEntity> pdEntityList = pdDocuments.get(0).getPdEntity();
        Set<String> labels = modelConfig.getLabels();
        return pdEntityList.stream()
                .filter(a -> labels == null || labels.contains(a.getTag()))
                .map(EntityConverter::pdEntityToNamedEntityRsp).collect(Collectors.toList());
    }

    @Override
    public List<SegmentEntityRsp> segment(String kgName, SegmentReq segmentReq) {
        SemanticSegFrom semanticSegFrom = SegmentConverter.segmentReqToSemanticSegFrom(segmentReq);

        Optional<List<SemanticSegWordVO>> segWordOpt = RestRespConverter.convert(semanticApi.seg(KGUtil.dbName(kgName), semanticSegFrom));

        if (!segWordOpt.isPresent() || CollectionUtils.isEmpty(segWordOpt.get())) {
            return Collections.emptyList();
        }
        List<Long> entityIdList = Lists.newArrayList();
        Map<Long, Double> scoreMap = Maps.newHashMap();
        Map<Long, String> wordMap = new HashMap<>();
        for (SemanticSegWordVO seg : segWordOpt.get()) {
            String word = seg.getWord();
            if (!CollectionUtils.isEmpty(seg.getConceptIdList())) {
                BasicConverter.applyIfTrue(segmentReq.getUseConcept(), seg.getConceptIdList(), entityIdList::addAll);
            }
            if (!CollectionUtils.isEmpty(seg.getEntityIdList())) {
                BasicConverter.applyIfTrue(segmentReq.getUseEntity(), seg.getEntityIdList(), entityIdList::addAll);
                int bound = seg.getEntityIdList().size();
                IntStream.range(0, bound).forEach(i -> {
                    scoreMap.put(seg.getEntityIdList().get(i), seg.getEntityScoreList().get(i));
                    wordMap.put(seg.getEntityIdList().get(i), word);
                });
            }
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(entityIdList,true)));
        if (!entityOpt.isPresent() || CollectionUtils.isEmpty(entityOpt.get())) {
            return Collections.emptyList();
        }
        List<SegmentEntityRsp> segmentEntityList = SegmentConverter.entityVoToSegmentEntityDto(entityOpt.get(), scoreMap, wordMap);
        segmentEntityList.sort(Comparator.comparing(SegmentEntityRsp::getScore));
        return segmentEntityList;

    }


    @Override
    public PdDocument segment2(String kgName, SegmentReq segmentReq) {
        SemanticSegFrom semanticSegFrom = SegmentConverter.segmentReqToSemanticSegFrom(segmentReq);
        Optional<List<SemanticSegWordVO>> segWordOpt = RestRespConverter.convert(semanticApi.seg(KGUtil.dbName(kgName), semanticSegFrom));

        PdDocument document = new PdDocument();

        List<Long> entityIdList = Lists.newArrayList();
        List<PdConcept> conceptList = new ArrayList<>();
        List<PdAttribute> attributeList = new ArrayList<>();
        List<PdEntity> entityList = new ArrayList<>();
        Map<Long, Double> scoreMap = Maps.newHashMap();
        Map<Long, String> wordMap = new HashMap<>();
        for (SemanticSegWordVO seg : segWordOpt.get()) {
            String word = seg.getWord();
            if (!CollectionUtils.isEmpty(seg.getConceptIdList())) {
                PdConcept concept = new PdConcept();
                concept.setId(seg.getConceptIdList().get(0));
                concept.setName(word);
                concept.setIndex(seg.getPos());
                conceptList.add(concept);
            }
            if (!CollectionUtils.isEmpty(seg.getAttributeIdList())) {
                PdAttribute attribute = new PdAttribute();
                attribute.setAttId(new Long(seg.getAttributeIdList().get(0)));
                attribute.setName(word);
                attribute.setIndex(seg.getPos());
                attributeList.add(attribute);
            }
            if (!CollectionUtils.isEmpty(seg.getEntityIdList())) {
                BasicConverter.applyIfTrue(segmentReq.getUseEntity(), seg.getEntityIdList(), entityIdList::addAll);
                int bound = seg.getEntityIdList().size();
                IntStream.range(0, bound).forEach(i -> {
                    scoreMap.put(seg.getEntityIdList().get(i), seg.getEntityScoreList().get(i));
                    wordMap.put(seg.getEntityIdList().get(i), word);
                });
            }
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(entityIdList,true)));

        List<SegmentEntityRsp> segmentEntityList = SegmentConverter.entityVoToSegmentEntityDto(entityOpt.get(), scoreMap, wordMap);
        segmentEntityList.sort(Comparator.comparing(SegmentEntityRsp::getScore));
        for(SegmentEntityRsp entity : segmentEntityList){
            PdEntity pdEntity = new PdEntity();
            pdEntity.setId(entity.getId());
            pdEntity.setName(entity.getWord());
            pdEntity.setClassId(entity.getConceptId());
            pdEntity.setMeaningTag(entity.getMeaningTag());
            pdEntity.setImgUrl(entity.getImgUrl());
            pdEntity.setStartTime(entity.getStartTime());
            pdEntity.setEndTime(entity.getEndTime());
            pdEntity.setCreationTime(entity.getCreationTime());
            pdEntity.setScore(entity.getScore());
            if(entity.getSynonym() != null) {
                List<PdValue> valueList = new ArrayList<>();
                for (String synonymName : entity.getSynonym()) {
                    PdValue value = new PdValue();
                    value.setName(synonymName);
                    valueList.add(value);
                }
                pdEntity.setSynonyms(valueList);
            }
            entityList.add(pdEntity);
        }
        document.setPdEntity(entityList);
        document.setPdAttribute(attributeList);
        document.setPdConcept(conceptList);
        return document;

    }

}
