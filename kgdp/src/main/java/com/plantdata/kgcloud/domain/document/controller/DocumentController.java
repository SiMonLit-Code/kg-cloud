package com.plantdata.kgcloud.domain.document.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.domain.document.entity.Document;
import com.plantdata.kgcloud.domain.document.req.DocHtmlReq;
import com.plantdata.kgcloud.domain.document.req.DocumentReq;
import com.plantdata.kgcloud.domain.document.rsp.DirectoryRsp;
import com.plantdata.kgcloud.domain.document.rsp.DocumentRsp;
import com.plantdata.kgcloud.domain.document.rsp.PageRsp;
import com.plantdata.kgcloud.domain.document.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@Api(tags = "文档")
@RestController
@RequestMapping("/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;

    @ApiOperation("批量上传文档")
    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ApiReturn<List<Document>> upload(Integer sceneId,MultipartFile[] multiRequest) {
        return documentService.upload(sceneId,multiRequest);
    }

    @ApiOperation("文档列表")
    @PostMapping("/list/page")
    public ApiReturn<Page<DocumentRsp>> listPage(DocumentReq documentReq, @ApiIgnore @PageableDefault Pageable pageable) {

        if(documentReq.getSorts() == null || documentReq.getSorts().size() == 0){
            pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"createTime"));
        }
        return documentService.findAll(documentReq,pageable);
    }

    @ApiOperation("获取文档详情")
    @PatchMapping("/get/html")
    public ApiReturn<PageRsp<WordContent>> getHtml(DocumentReq documentReq) {
        return documentService.getHtml(documentReq.getSceneId(),documentReq.getId(),documentReq.getPage(),documentReq.getSize());
    }

    @ApiOperation("获取文档目录结构")
    @PatchMapping("/get/directory")
    public ApiReturn<List<DirectoryRsp>> getDirectory(DocumentReq documentReq) {
        return documentService.getDirectory(documentReq.getSceneId(),documentReq.getId());
    }

    @ApiOperation("批量更新文档结构")
    @PostMapping("/update/html")
    public ApiReturn updateHtml(@RequestBody List<DocHtmlReq> docHtmlReq,Boolean isInitPddoc,Integer sceneId,Integer docId) {
        String s = "<p style=\"text-indent: 21pt; font-size: 16px;\" data-level=\"0\" data-p=\"0\" data-structure=\"1\" data-id=\"2\" class=\"doc-title\" data-pddid=\"0\" data-i=\"2-0\"><span style=\"font-family: &quot;Times New Roman&quot;; font-size: 24px;\" data-i=\"2-0-0\">　　【国 名】 马达加斯加共和国（The Republic of Madagascar，La République de Madagascar）。</span><span id=\"_GoBack\" data-i=\"2-0-1\" style=\"font-size: 24px;\"></span></p>";
        return documentService.updateHtml(docHtmlReq,isInitPddoc,sceneId,docId);
    }

    @ApiOperation("删除文档")
    @PostMapping("/delete/{id:\\d+}/{sceneId:\\d+}")
    public ApiReturn deleteDoc(@PathVariable("id") Integer id,@PathVariable("sceneId") Integer sceneId) {
        return documentService.deleteDoc(sceneId,id);
    }

    @ApiOperation("更新文档状态")
    @PostMapping("/update/status")
    public ApiReturn getPddocument(@RequestBody DocumentReq documentReq) {
        return documentService.updateStatus(documentReq.getSceneId(),documentReq.getId(),documentReq.getStatus());
    }


}
