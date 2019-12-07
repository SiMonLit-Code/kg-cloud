package com.plantdata.kgcloud.domain.common.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.domain.common.rsp.FileRsp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface CommonService {

    ResponseEntity<byte[]> resourceDownLoad(String name);

    String upload(InputStream fileIn, String name);

    ApiReturn<String> uploadImage(String name, byte[] content);

    List<WordContent> convertDocToHtml(String path, Boolean structureDismantling);

    List<WordContent> convertDocxToHtml(String path, Boolean structureDismantling);

    ApiReturn<List<FileRsp>> multiUpload(MultipartFile[] multiRequest);
}
