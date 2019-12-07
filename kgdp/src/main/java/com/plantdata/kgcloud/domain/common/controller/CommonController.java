package com.plantdata.kgcloud.domain.common.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.common.rsp.FileRsp;
import com.plantdata.kgcloud.domain.common.service.CommonService;
import com.plantdata.kgcloud.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Api(tags = "common")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ApiOperation("批量资源上传")
    @PostMapping(value = "multi/upload", consumes = "multipart/form-data")
    public ApiReturn<List<FileRsp>> multiUpload(MultipartFile[] multiRequest) {
        return commonService.multiUpload(multiRequest);
    }

    @ApiOperation("资源上传")
    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ApiReturn<String> upload(MultipartFile multiRequest) {
        try {
            InputStream in = multiRequest.getInputStream();
            return ApiReturn.success(commonService.upload(in,multiRequest.getOriginalFilename()));
        }catch (Exception e){
            throw BizException.of(KgDocumentErrorCodes.RESOURCE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("资源下载")
    @GetMapping("resource/download")
    public ResponseEntity<byte[]> resourceDownLoad(String name) {
        return commonService.resourceDownLoad(name);
    }

}
