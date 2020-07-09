package com.plantdata.kgcloud.domain.dictionary.service;

import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.constant.CommonConstants;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dictionary.constant.DictConst;
import com.plantdata.kgcloud.domain.dictionary.constant.Nature;
import com.plantdata.kgcloud.domain.dictionary.entity.Dictionary;
import com.plantdata.kgcloud.domain.dictionary.repository.DictionaryRepository;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.WordRsp;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 14:59
 **/
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private MongoClient mongoClient;

    private MongoCollection<Document> getMongodb(String userId, Long dictId) {
        Dictionary dict = dictionaryRepository.findByIdAndUserId(dictId, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DICTIONARY_NOT_EXISTS));
        String database = dict.getDbName();
        String collection = DictConst.COLLECTION_PREFIX + dict.getId();
        return mongoClient.getDatabase(database).getCollection(collection);
    }

    private WordRsp buildWord(Document doc) {
        WordRsp rsp = new WordRsp();
        ObjectId objectId = doc.getObjectId(CommonConstants.MongoConst.ID);
        if (objectId != null) {
            rsp.setId(objectId.toHexString());
        }
        rsp.setNature(Nature.toShow(doc.getString(DictConst.NATURE)));
        rsp.setSyns(JacksonUtils.readValue(doc.getString(DictConst.SYNONYM), new TypeReference<List<String>>() {
        }));
        rsp.setName(doc.getString(DictConst.NAME));
        return rsp;
    }

    private Document buildDoc(WordReq req) {
        Document doc = new Document();
        doc.append(DictConst.NAME, req.getName());
        doc.append(DictConst.SYNONYM, JacksonUtils.writeValueAsString(req.getSyns()));
        doc.append(DictConst.NATURE, Nature.toType(req.getNature()));
        return doc;
    }

    @Override
    public List<WordRsp> findAll(String userId, Long dictId) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        ArrayList<WordRsp> words = new ArrayList<>();
        for (Document doc : mdb.find()) {
            words.add(buildWord(doc));
        }
        return words;
    }

    @Override
    public Page<WordRsp> findAll(String userId, Long dictId, BaseReq baseReq) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        long count = mdb.countDocuments();
        FindIterable<Document> findIterable = mdb.find();
        int page = baseReq.getOffset();
        int size = baseReq.getLimit();
        if (page >= 0 && size > 0) {
            findIterable = findIterable.skip(page);
            findIterable = findIterable.limit(size);
        }
        ArrayList<WordRsp> words = new ArrayList<>();
        for (Document doc : findIterable) {
            words.add(buildWord(doc));
        }

        PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        return new PageImpl<>(words, pageable, count);
    }

    @Override
    public WordRsp findById(String userId, Long dictId, String id) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        Document doc = mdb.find(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id))).first();
        if (doc != null) {
            return buildWord(doc);
        } else {
            throw BizException.of(KgmsErrorCodeEnum.WORD_NOT_EXISTS);
        }

    }

    @Override
    public void delete(String userId, Long dictId, String id) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        mdb.deleteOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)));
    }

    @Override
    public WordRsp insert(String userId, Long dictId, WordReq r) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        Document document = buildDoc(r);
        mdb.insertOne(document);
        return buildWord(document);
    }

    @Override
    public WordRsp update(String userId, Long dictId, String id, WordReq r) {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);
        Document value = buildDoc(r);
        Document document = new Document("$set", value);
        mdb.updateOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)), document);
        return buildWord(value);
    }

    @Override
    public void exportWord(String userId, Long dictId, HttpServletResponse response) {
        List<List<String>> resultList = new ArrayList<>();
        List<WordRsp> all = findAll(userId, dictId);
        for (WordRsp wordRsp : all) {
            List<String> word = new ArrayList<>();
            word.add(wordRsp.getName());
            word.add(String.join(",", wordRsp.getSyns()));
            word.add(wordRsp.getNature());
            resultList.add(word);
        }
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("名称"));
        head.add(Collections.singletonList("同义"));
        head.add(Collections.singletonList("词性"));
        Dictionary dict = dictionaryRepository.findByIdAndUserId(dictId, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DICTIONARY_NOT_EXISTS));
        try {
            response.setContentType("application/octet-stream");
            String dataName = dict.getTitle() + "_" + System.currentTimeMillis() + ".xlsx";
            String fileName = URLEncoder.encode(dataName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream).head(head).sheet().doWrite(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importWord(String userId, Long dictId, MultipartFile file) throws Exception {
        MongoCollection<Document> mdb = getMongodb(userId, dictId);

        EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, Object>>() {
            List<Document> mapList = new ArrayList<>();
            Integer name = null;
            Integer synonym = null;
            Integer nature = null;

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                    if (Objects.equals(entry.getValue(), "名称")) {
                        name = entry.getKey();
                    }
                    if (Objects.equals(entry.getValue(), "同义")) {
                        synonym = entry.getKey();
                    }
                    if (Objects.equals(entry.getValue(), "词性")) {
                        nature = entry.getKey();
                    }
                }
            }

            @Override
            public void invoke(Map<Integer, Object> data, AnalysisContext context) {
                if (name == null || synonym == null || nature == null) {
                    throw new RuntimeException("表头必须包含：名称、同义、词性");
                }
                if (name != null) {
                    Document map = new Document();
                    map.append(DictConst.NAME, data.get(name));

                    if (synonym != null) {
                        if (data.get(synonym) != null) {
                            String[] split = data.get(synonym).toString().split(",");
                            map.append(DictConst.SYNONYM, JacksonUtils.writeValueAsString(Arrays.asList(split)));
                        }
                    }
                    if (nature != null) {
                        if (data.get(nature) != null) {
                            map.append(DictConst.NATURE, Nature.toType(data.get(nature).toString()));
                        }
                    }
                    map.append(DictConst.NATURE, data.get(nature));
                    mapList.add(map);
                }
                if (mapList.size() == 10000) {
                    mdb.insertMany(mapList);
                    mapList.clear();
                }
                if (!mapList.isEmpty()) {
                    mdb.insertMany(mapList);
                    mapList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet().doRead();
    }
}
