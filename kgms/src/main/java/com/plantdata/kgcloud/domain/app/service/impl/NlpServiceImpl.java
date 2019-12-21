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
import com.hiekn.pddocument.bean.element.PdEntity;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.SegmentConverter;
import com.plantdata.kgcloud.domain.app.dto.SegmentEntityDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        List<SegmentEntityDTO> list = segment(kgName, segmentReq);
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

    private List<SegmentEntityDTO> segment(String kgName, SegmentReq segmentReq) {
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
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(entityIdList)));
        if (!entityOpt.isPresent() || CollectionUtils.isEmpty(entityOpt.get())) {
            return Collections.emptyList();
        }
        List<SegmentEntityDTO> segmentEntityList = SegmentConverter.entityVoToSegmentEntityDto(entityOpt.get(), scoreMap, wordMap);
        segmentEntityList.sort(Comparator.comparing(SegmentEntityDTO::getScore));
        return segmentEntityList;

    }
}
