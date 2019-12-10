package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetAnnotation;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetAnnotationRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetAnnotationService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.AnnotationConf;
import com.plantdata.kgcloud.sdk.req.AnnotationDataReq;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    private MongoClient mongoClient;

    @Override
    public Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq) {
        DataSetAnnotation build = DataSetAnnotation.builder().kgName(kgName).build();
        PageRequest of = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<DataSetAnnotation> all = dataSetAnnotationRepository.findAll(Example.of(build), of);
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
        return ConvertUtils.convert(AnnotationRsp.class).apply(dataSetAnnotation);
    }

    @Override
    public AnnotationRsp add(String kgName, AnnotationReq req) {
        DataSetAnnotation dataSetAnnotation = new DataSetAnnotation();
        BeanUtils.copyProperties(req, dataSetAnnotation);
        dataSetAnnotation.setKgName(kgName);
        DataSetAnnotation save = dataSetAnnotationRepository.save(dataSetAnnotation);
        return ConvertUtils.convert(AnnotationRsp.class).apply(save);
    }

    @Override
    public void annotation(String userId,String kgName, Long annotationId, AnnotationDataReq request) {
        Long datasetId = request.getId();
        String objId = request.getObjId();
        Map<String, Object> objectMap = dataOptService.updateData(userId,datasetId, objId, request.getData());
        ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            objectNode.putPOJO(entry.getKey(), entry.getValue());
        }
        DataSetAnnotation one = findOne(annotationId);
        List<AnnotationConf> config = one.getConfig();

        MongoDatabase mongoDatabase = mongoClient.getDatabase("kg_attribute_definition");
        MongoCollection<Document> kgDbName = mongoDatabase.getCollection("kg_db_name");
        FindIterable<Document> findIterable = kgDbName.find(new Document("kg_name", kgName));

        Document document = findIterable.first();
        Set<String> key = new HashSet<>();
        for (AnnotationConf conf : config) {
            if (Objects.equals(conf.getSource(), 1)) {
                key.add(conf.getKey());
            }
        }
        if (document != null && !key.isEmpty()) {
            String dbName = document.getString("db_name");
            MongoDatabase database = mongoClient.getDatabase(dbName);
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
