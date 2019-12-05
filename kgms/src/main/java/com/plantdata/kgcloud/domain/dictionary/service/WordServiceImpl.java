package com.plantdata.kgcloud.domain.dictionary.service;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dictionary.constant.DictConst;
import com.plantdata.kgcloud.domain.dictionary.constant.Nature;
import com.plantdata.kgcloud.domain.dictionary.entity.Dictionary;
import com.plantdata.kgcloud.domain.dictionary.repository.DictionaryRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.WordReq;
import com.plantdata.kgcloud.sdk.rsp.WordRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private MongoCollection<Document> getMongodb(Long dictId) {
        String userId = SessionHolder.getUserId();
        Dictionary dict = dictionaryRepository.findByIdAndUserId(dictId, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DICTIONARY_NOT_EXISTS));
        String database = dict.getDbName();
        String collection = DictConst.COLLECTION_PREFIX + dict.getId();
        return mongoClient.getDatabase(database).getCollection(collection);
    }

    private WordRsp buildWord(Document doc) {
        WordRsp rsp = new WordRsp();
        rsp.setId(doc.getObjectId(CommonConstants.MongoConst.ID).toHexString());
        rsp.setNature(Nature.toShow(doc.getString(DictConst.NATURE)));
        rsp.setSyns(doc.getString(DictConst.SYNONYM));
        rsp.setName(doc.getString(DictConst.NAME));
        return rsp;
    }

    private Document buildDoc(WordReq req) {
        Document doc = new Document();
        doc.append(DictConst.NAME, req.getName());
        doc.append(DictConst.SYNONYM, req.getSyns());
        doc.append(DictConst.NATURE, Nature.toType(req.getNature()));
        return doc;
    }

    @Override
    public List<WordRsp> findAll(Long dictId) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        ArrayList<WordRsp> words = new ArrayList<>();
        for (Document doc : mdb.find()) {
            words.add(buildWord(doc));
        }
        return words;
    }

    @Override
    public Page<WordRsp> findAll(Long dictId, BaseReq baseReq) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        long count = mdb.countDocuments();
        FindIterable<Document> findIterable = mdb.find();
        int page = baseReq.getPage();
        int size = baseReq.getSize();
        if (page > 0 && size > 0) {
            findIterable = findIterable.skip(page * size);
            findIterable = findIterable.limit(size);
        }
        ArrayList<WordRsp> words = new ArrayList<>();
        for (Document doc : findIterable) {
            words.add(buildWord(doc));
        }

        PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<WordRsp> pageResult = new PageImpl<>(words, pageable, count);
        return pageResult;
    }

    @Override
    public WordRsp findById(Long dictId, String id) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        Document doc = mdb.find(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id))).first();
        if (doc != null) {
            WordRsp data = buildWord(doc);
            return data;
        } else {
            throw BizException.of(KgmsErrorCodeEnum.WORD_NOT_EXISTS);
        }

    }

    @Override
    public void delete(Long dictId, String id) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        mdb.deleteOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)));
    }

    @Override
    public WordRsp insert(Long dictId, WordReq r) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        Document document = buildDoc(r);
        mdb.insertOne(document);
        return buildWord(document);
    }

    @Override
    public WordRsp update(Long dictId, String id, WordReq r) {
        MongoCollection<Document> mdb = getMongodb(dictId);
        Document document = buildDoc(r);
        mdb.updateOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)), document);
        return buildWord(document);
    }
}
