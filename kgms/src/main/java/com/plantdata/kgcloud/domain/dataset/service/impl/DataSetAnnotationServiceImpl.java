package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetAnnotation;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetAnnotationRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetAnnotationService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.AnnotationConf;
import com.plantdata.kgcloud.sdk.req.AnnotationCreateReq;
import com.plantdata.kgcloud.sdk.req.AnnotationDataReq;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:13
 **/
@Service
public class DataSetAnnotationServiceImpl implements DataSetAnnotationService {

    @Autowired
    private DataSetAnnotationRepository dataSetAnnotationRepository;
    @Autowired
    private DataOptService dataOptService;
    @Autowired
    private GraphService graphService;
    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;
    @Autowired
    private MongoClient mongoClient;

    @Override
    public Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq) {
        PageRequest of = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), Sort.by(Sort.Direction.DESC,"createAt"));
        Specification<DataSetAnnotation> specification = (Specification<DataSetAnnotation>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.get("kgName"),kgName));
            if (StringUtils.hasText(baseReq.getName())) {
                expressions.add(cb.like(root.get("name"), "%" + baseReq.getName() + "%"));
            }
            return predicate;
        };
        Page<DataSetAnnotation> all = dataSetAnnotationRepository.findAll(specification, of);
        return all.map(ConvertUtils.convert(AnnotationRsp.class));
    }

    @Override
    public void delete(String kgName, Long id) {
        dataSetAnnotationRepository.deleteById(id);
    }

    private DataSetAnnotation findOne(Long id) {
        Optional<DataSetAnnotation> one = dataSetAnnotationRepository.findById(id);
        return one.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ANNOTATION_NOT_EXISTS));
    }

    @Override
    public AnnotationRsp findById(String kgName, Long id) {
        DataSetAnnotation one = findOne(id);
        return ConvertUtils.convert(AnnotationRsp.class).apply(one);
    }

    @Override
    public AnnotationRsp update(String kgName, Long id, AnnotationReq req) {
        DataSetAnnotation dataSetAnnotation = dataSetAnnotationRepository
                .findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ANNOTATION_NOT_EXISTS));
        BeanUtils.copyProperties(req, dataSetAnnotation);
        dataSetAnnotationRepository.save(dataSetAnnotation);
        return ConvertUtils.convert(AnnotationRsp.class).apply(dataSetAnnotation);
    }

    @Override
    public AnnotationRsp add(String userId, String kgName, AnnotationCreateReq req) {
        graphService.findById(userId, kgName);
        dataSetService.findOne(userId, req.getDataId());
        DataSetAnnotation dataSetAnnotation = new DataSetAnnotation();
        BeanUtils.copyProperties(req, dataSetAnnotation);
        dataSetAnnotation.setId(kgKeyGenerator.getNextId());
        dataSetAnnotation.setKgName(kgName);
        DataSetAnnotation save = dataSetAnnotationRepository.save(dataSetAnnotation);
        return ConvertUtils.convert(AnnotationRsp.class).apply(save);
    }

    @Override
    public void annotation(String userId, String kgName, Long annotationId, AnnotationDataReq request) {
        Long datasetId = request.getId();
        String objId = request.getObjId();
        Map<String, Object> objectMap = dataOptService.updateData(userId, datasetId, objId, request.getData());
        ObjectNode objectNode = JacksonUtils.readValue(JacksonUtils.writeValueAsString(objectMap),ObjectNode.class );
        DataSetAnnotation one = findOne(annotationId);
        List<AnnotationConf> config = one.getConfig();
        Set<String> key = new HashSet<>();
        for (AnnotationConf conf : config) {
            if (Objects.equals(conf.getSource(), 1)) {
                key.add(conf.getKey());
            }
        }
        if (!key.isEmpty()) {
            MongoDatabase database = mongoClient.getDatabase(KGUtil.dbName(kgName));
            MongoCollection<Document> collection = database.getCollection("entity_annotation");
            for (String sss : key) {
                JsonNode node = objectNode.get(sss);
                if (node.isArray()) {
                    for (JsonNode json : node) {
                        long kgId = json.findValue("kgId").asLong();
                        collection.deleteMany(new Document("entity_id", kgId).append("data_set_id", datasetId).append("source", 1));
                    }
                    for (JsonNode m : node) {
                        long kgId = m.findValue("kgId").asLong();
                        Document query = new Document("entity_id", kgId).append("data_set_id", datasetId).append("data_id", objId);
                        Document replace = new Document("entity_id", kgId).append("data_set_id", datasetId).append("data_id", objId).append("score", 1).append("source", 1);
                        collection.findOneAndReplace(query, replace, new FindOneAndReplaceOptions().upsert(true));
                    }
                }
            }
        }
    }
}
