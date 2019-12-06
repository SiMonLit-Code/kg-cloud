package com.plantdata.kgcloud.domain.document.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.domain.document.entity.Document;
import com.plantdata.kgcloud.domain.document.req.DocHtmlReq;
import com.plantdata.kgcloud.domain.document.req.DocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.DirectoryRsp;
import com.plantdata.kgcloud.domain.document.rsp.DocumentRsp;
import com.plantdata.kgcloud.domain.document.rsp.PageRsp;
import com.plantdata.kgcloud.domain.scene.entiy.Scene;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {


    ApiReturn<List<Document>> upload(Integer sceneId, MultipartFile[] multiRequest);

    ApiReturn<Page<DocumentRsp>> findAll(DocumentReq documentReq, Pageable pageable);

    ApiReturn<PageRsp<WordContent>> getHtml(Integer sceneId, Integer id, Integer page, Integer size);

    ApiReturn updateHtml(List<DocHtmlReq> docHtmlReqs, Boolean isInitPddoc, Integer sceneId, Integer docId);

    ApiReturn updateStatus(Integer sceneId, Integer id, Integer status);

    ApiReturn deleteDoc(Integer sceneId, Integer id);

    ApiReturn<List<DirectoryRsp>> getDirectory(Integer sceneId, Integer id);
}
