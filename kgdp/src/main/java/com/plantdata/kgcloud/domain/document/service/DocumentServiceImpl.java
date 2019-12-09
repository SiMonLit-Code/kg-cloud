package com.plantdata.kgcloud.domain.document.service;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.ConvertConstent;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.domain.common.service.CommonService;
import com.plantdata.kgcloud.domain.common.service.MongoDriver;
import com.plantdata.kgcloud.domain.document.entity.Document;
import com.plantdata.kgcloud.domain.document.repository.DocumentRepository;
import com.plantdata.kgcloud.domain.document.req.DocHtmlReq;
import com.plantdata.kgcloud.domain.document.req.DocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.DirectoryRsp;
import com.plantdata.kgcloud.domain.document.rsp.DocumentRsp;
import com.plantdata.kgcloud.domain.document.rsp.PageRsp;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import com.plantdata.kgcloud.domain.scene.service.SceneService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private MongoDriver mongoDriver;

    @Autowired
    private SceneService sceneService;


    @Override
    public ApiReturn<List<Document>> upload(Integer sceneId, MultipartFile[] multiRequest) {


        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        if(multiRequest == null || multiRequest.length == 0){
            return ApiReturn.success();
        }

        List<Document> rs = new ArrayList<>(multiRequest.length);
        for(int i=0; i< multiRequest.length; i++){
            MultipartFile file = multiRequest[i];
            if (!file.isEmpty()) {

                try {
                    String name = commonService.upload(file.getInputStream(),file.getOriginalFilename());
                    String docType = name.substring(name.lastIndexOf(".")+1);

                    //场景配置处理文件格式为文档
                    if(scene.getDocFormat() == 1){
                        List<String> docTypes = scene.getDocType();
                        List<String> types = docTypes.stream().map(t -> t.toLowerCase()).collect(Collectors.toList());
                        if(!types.contains(docType.toLowerCase())){
                            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_TYPE_ERROR);
                        }
                    }

                    Document document = Document.builder().sceneId(sceneId).docSize(file.getSize())
                            .docStatus(0).docType(docType).modelStatus(0)
                            .name(file.getOriginalFilename()).source(name).build();

                    documentRepository.save(document);

                    Document doc = ConvertUtils.convert(Document.class).apply(document);
                    rs.add(doc);
                }catch (IOException e){
                    e.printStackTrace();
                    throw BizException.of(KgDocumentErrorCodes.RESOURCE_UPLOAD_ERROR);
                }catch (JpaSystemException jpaSystemException){
                    jpaSystemException.printStackTrace();
                    throw BizException.of(KgDocumentErrorCodes.SAVE_DATA_TO_DATABASE_ERROR);
                }

            }
        }

        return ApiReturn.success(rs);
    }

    @Override
    public ApiReturn<Page<DocumentRsp>> findAll(DocumentReq documentReq, Pageable pageable) {

        Scene scene = sceneService.checkScene(documentReq.getSceneId(), SessionHolder.getUserId());

        Specification<Document> specification = new Specification<Document>() {
            @Override
            public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                Predicate senceId = cb.equal(root.get("sceneId").as(String.class), documentReq.getSceneId());
                predicates.add(senceId);

                if (!StringUtils.isEmpty(documentReq.getName())) {

                    Predicate likeUsername = cb.like(root.get("name").as(String.class), "%" + documentReq.getName() + "%");
                    predicates.add(likeUsername);
                }

                if(!StringUtils.isEmpty(documentReq.getDocType())){

                    Predicate eqType = cb.equal(root.get("docType").as(String.class), documentReq.getDocType());
                    predicates.add(eqType);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };


        Page<Document> all = documentRepository.findAll(specification,pageable);

        Page<DocumentRsp> map = all.map(ConvertUtils.convert(DocumentRsp.class));
        return ApiReturn.success(map);
    }

    @Override
    public ApiReturn<PageRsp<WordContent>> getHtml(Integer sceneId, Integer id, Integer page, Integer size) {

        page = page * size;
        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        Document docFilter = Document.builder().sceneId(scene.getId()).id(id).build();
        Optional<Document> document = documentRepository.findOne(Example.of(docFilter));

        if(!document.isPresent()){
            return ApiReturn.success();
        }

        int status = document.get().getDocStatus();
        if(status == 0){
            //文档 未解析

            String source = document.get().getSource();
            parserDocHtml(scene,id,source);
        }

        //从mongo分页获取
        org.bson.Document doc = new org.bson.Document("doc_id",id);
        List<org.bson.Document> list = mongoDriver.list(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_html,size,page,doc,null);
        Long count = mongoDriver.count(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_html,doc);
        if(list != null && !list.isEmpty()){
            List<WordContent> wordContents = new ArrayList<>(list.size());
            for(org.bson.Document d : list){
                wordContents.add(WordContent.builder()
                        .htmlText(d.getString("html"))
                        .level(d.getInteger("level"))
                        .text(d.getString("text"))
                        .index(d.getInteger("index"))
                        .structure(d.getInteger("structure"))
                        .build());
            }

            parserWordHtmlAttr(wordContents,sceneId,id);

            Long totalPages = count / size;
            if(count == 0L){
                totalPages = 1L;
            }else if(count % size != 0){
                totalPages++;
            }
            return ApiReturn.success(new PageRsp<>(wordContents,totalPages,count));
        }

        return ApiReturn.success();
    }

    private void parserWordHtmlAttr(List<WordContent> wordContents, Integer sceneId, Integer id) {

        List<DirectoryRsp> directoryRspList = getDirectory(sceneId,id).getData();
        if(directoryRspList == null){
            directoryRspList = Lists.newArrayList();
        }

        Map<String, DirectoryRsp> titleMap = Maps.newHashMap();
        getTitleByDirectory(directoryRspList,titleMap);

        String dataPddId = "";
        for(WordContent wordContent : wordContents){
            Element htmlDoc = Jsoup.parse(wordContent.getHtmlText()).body().child(0);
            if(titleMap.containsKey(wordContent.getText())){
                DirectoryRsp directoryRsp = titleMap.get(wordContent.getText());
                htmlDoc.attributes().put("data-level",directoryRsp.getLevel()+"")
                    .put("data-p",directoryRsp.getSerialNumber())
                    .put("data-structure",wordContent.getStructure()+"")
                    .put("class", "doc-title")
                    .put("data-id", directoryRsp.getIndex()+"");
                wordContent.setHtmlText(htmlDoc.outerHtml());

                dataPddId = wordContent.getIndex()+"";
            }else if(!htmlDoc.is("table")){
                htmlDoc.attributes().put("data-level", "0")
                    .put("data-p", "0")
                    .put("data-structure", "1")
                    .put("data-id", wordContent.getIndex()+"")
                    .put("class", "doc-text")
                    .put("data-text", "1");

                if(!"".equals(dataPddId)){
                    htmlDoc.attributes().put("data-pddId", dataPddId);
                }

                wordContent.setHtmlText(htmlDoc.outerHtml());
            }

        }
    }

    private void getTitleByDirectory(List<DirectoryRsp> directoryRspList, Map<String, DirectoryRsp> titleMap) {


        for(DirectoryRsp directoryRsp : directoryRspList){
            if(directoryRsp.getText().isEmpty()){
                continue;
            }
            titleMap.put(directoryRsp.getText(),directoryRsp);
            if(directoryRsp.getChildren() != null && !directoryRsp.getChildren().isEmpty()){
                getTitleByDirectory(directoryRsp.getChildren(),titleMap);
            }
        }
    }

    private void parserDocHtml(Scene scene, Integer docId, String source){

        List<WordContent> wordContentList = Lists.newArrayList();
        if(source.endsWith(".doc")){
            wordContentList = commonService.convertDocToHtml(source,scene.getStructureDismantling());
        }else if(source.endsWith(".docx")){
            wordContentList = commonService.convertDocxToHtml(source,scene.getStructureDismantling());
        }

        if(wordContentList == null || wordContentList.isEmpty()){
            return;
        }

        //存解析后的html记录
        List<org.bson.Document> docs  = Lists.newArrayList();
        for(WordContent wordContent : wordContentList){
            org.bson.Document doc = new org.bson.Document("doc_id",docId);
            doc.put("html",wordContent.getHtmlText());
            doc.put("level",wordContent.getLevel());
            doc.put("text",wordContent.getText());
            doc.put("index",wordContent.getIndex());
            doc.put("structure",wordContent.getStructure());
            docs.add(doc);
        }

        //根据标题生成pddocument存储
        List<PdDocument> pdDocuments = parseWordContent2PdDocument(wordContentList);

        Document docFilter = Document.builder().sceneId(scene.getId()).id(docId).build();
        Optional<Document> document = documentRepository.findOne(Example.of(docFilter));

        if(document == null || !document.isPresent()){
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_NOT_EXISTS);
        }

        int status = document.get().getDocStatus();

        synchronized (this){
            if(status == 0){
                //避免多并发请求时多插入到数据的情况
                mongoDriver.insertMany(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_html,docs);
                Document docFile = document.get();
                //更新状态
                if(scene.getModelAnalysis() != null && !scene.getModelAnalysis().isEmpty()){
                    docFile.setDocStatus(1);
                }else{
                    docFile.setDocStatus(2);
                }
                documentRepository.save(docFile);

                savePddocument(scene.getId(),docId,pdDocuments);
            }
        }

    }

    private void savePddocument(Integer senceId,Integer docId,List<PdDocument> pdDocumentList){
        org.bson.Document doc = new org.bson.Document("doc_id",docId);
        doc.put("pd_document",JSON.toJSON(pdDocumentList));
        mongoDriver.insert(ConvertConstent.databases,senceId+"_"+ ConvertConstent.collection_pddocument,doc);
    }

    private List<PdDocument> parseWordContent2PdDocument(List<WordContent> wordContentList) {

        List<PdDocument> pdDocumentList = Lists.newArrayList();
        if(wordContentList == null || wordContentList.isEmpty()){
            return pdDocumentList;
        }

        Map<Integer,String> levelIdMap = Maps.newHashMap();
        String content = "";

        for(WordContent wordContent : wordContentList){

            if(wordContent.getLevel() == null || wordContent.getLevel() < 1){
                //正文，拼接内容
                content += wordContent.getText();
                continue;
            }

            //碰到标题，内容赋值给上一个pddocument，重新生成给一个pddocument
            if(!pdDocumentList.isEmpty()){
                PdDocument pdDocument = pdDocumentList.get(pdDocumentList.size() - 1);
                pdDocument.setContent(content);
            }

            PdDocument pdDocument = new PdDocument();
            pdDocument.setTitle(wordContent.getText());
            pdDocument.setId(wordContent.getIndex()+"");
            pdDocument.setParentId(returnParentId(wordContent.getLevel(),levelIdMap));

            pdDocumentList.add(pdDocument);

            //当有新标题出现，删除比他小的标题id，重置层级关系
            Iterator<Map.Entry<Integer,String>> it = levelIdMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<Integer,String> item = it.next();
                if(item.getKey() >= wordContent.getLevel()){
                    it.remove();
                }
            }
            levelIdMap.put(wordContent.getLevel(),pdDocument.getId());

            //新标题重置内容信息
            content = "";
        }

        if(!pdDocumentList.isEmpty()){
            //遍历结束，给最后一个pddocument赋值内容
            PdDocument pdDocument = pdDocumentList.get(pdDocumentList.size() - 1);
            pdDocument.setContent(content);
        }

        return pdDocumentList;
    }

    private static String returnParentId(Integer level, Map<Integer,String> levelIdMap){

        if(level > 1){
            //上一级层级
            Integer parentLevel = level - 1;
            //上一层级id
            String id = levelIdMap.get(parentLevel);
            if(id == null){
                //不存在，再往上找
                return returnParentId(parentLevel, levelIdMap);
            }else{
                //存在 返回
                return id;
            }
        }

        //找到一级标题还没有，则直接挂载在文档中
        return null;
    }

    @Override
    public ApiReturn updateHtml(List<DocHtmlReq> docHtmlReqs, Boolean isInitPddoc, Integer sceneId, Integer docId) {


        if(docHtmlReqs == null || docHtmlReqs.isEmpty()){
            return ApiReturn.success();
        }

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        docHtmlReqs.forEach(doc -> {

            org.bson.Document filter = new org.bson.Document("doc_id",docId);
            filter.put("index",doc.getHtmlId());
            org.bson.Document document = new org.bson.Document("level",doc.getLevel());

            //html转义
            String html = StringEscapeUtils.unescapeHtml4(doc.getHtml());
            document.put("html",html);
            mongoDriver.update(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_html,filter,document);

        });

        if(isInitPddoc != null && isInitPddoc){
            //更改了结构，重新生成pddocument
            List<PdDocument> pdDocuments = parseWordContent2PdDocument(scene.getId(),docId);
            org.bson.Document doc = new org.bson.Document("doc_id",docId);
            mongoDriver.delete(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_pddocument,doc);
            savePddocument(scene.getId(),docId,pdDocuments);
        }


        return ApiReturn.success();
    }

    private List<PdDocument> parseWordContent2PdDocument(Integer sceneId, Integer id) {
        org.bson.Document doc = new org.bson.Document("doc_id",id);
        List<org.bson.Document> list = mongoDriver.listAll(ConvertConstent.databases,sceneId+"_"+ ConvertConstent.collection_html,doc,null);

        if(list != null && !list.isEmpty()){
            List<WordContent> wordContents = new ArrayList<>(list.size());
            for(org.bson.Document d : list){
                wordContents.add(WordContent.builder()
                        .htmlText(d.getString("html"))
                        .level(d.getInteger("level"))
                        .index(d.getInteger("index"))
                        .text(d.getString("text"))
                        .structure(d.getInteger("structure"))
                        .build());
            }

            return parseWordContent2PdDocument(wordContents);
        }

        return Lists.newArrayList();
    }


    @Override
    @Transactional
    public ApiReturn updateStatus(Integer sceneId, Integer id, Integer status) {

        if(sceneId == null){
            throw BizException.of(KgDocumentErrorCodes.PARAMETER_ERROR);
        }
        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        documentRepository.updateStatus(scene.getId(),id,status);
        return ApiReturn.success();
    }

    @Override
    public ApiReturn deleteDoc(Integer sceneId, Integer id) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        Document document = Document.builder().sceneId(scene.getId()).id(id).build();
        documentRepository.delete(document);
        return ApiReturn.success();
    }

    @Override
    public ApiReturn<List<DirectoryRsp>> getDirectory(Integer sceneId, Integer id) {

        Scene scene = sceneService.checkScene(sceneId, SessionHolder.getUserId());

        Document docFilter = Document.builder().sceneId(scene.getId()).id(id).build();
        Optional<Document> document = documentRepository.findOne(Example.of(docFilter));

        if(document == null || !document.isPresent()){
            return ApiReturn.success();
        }

        org.bson.Document doc = new org.bson.Document("doc_id",id);
        doc.put("level",new org.bson.Document("$gt",0));
        List<org.bson.Document> list = mongoDriver.listAll(ConvertConstent.databases,scene.getId()+"_"+ ConvertConstent.collection_html,doc,null);

        if(list == null || list.isEmpty()){
            return ApiReturn.success();
        }

        List<DirectoryRsp> directoryRspList = new ArrayList<>(list.size());
        for(org.bson.Document d : list){

            DirectoryRsp directoryRsp = DirectoryRsp.builder()
                    .index(d.getInteger("index"))
                    .level(d.getInteger("level"))
                    .text(d.getString("text"))
                    .build();
            directoryRsp.setSerialNumber(directoryRspList);
            directoryRspList.add(directoryRsp);

        }
        List<DirectoryRsp> rsList = directoryRspList.stream().filter(dir -> {
            if (dir.getLevel() == 1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        return ApiReturn.success(rsList);
    }
}
