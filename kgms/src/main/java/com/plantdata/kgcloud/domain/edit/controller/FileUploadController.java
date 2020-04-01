package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.edit.aop.EditPermissionUnwanted;
import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.domain.edit.rsp.ThumbPathRsp;
import com.plantdata.kgcloud.domain.edit.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 17:29
 * @Description:
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("上传文件,不带缩略图")
    @PostMapping
    @EditPermissionUnwanted
    public ApiReturn<FilePathRsp> uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(fileUploadService.uploadFile(file));
    }

    @ApiOperation("多文件上传")
    @PostMapping("multi")
    @EditPermissionUnwanted
    public ApiReturn<List<ThumbPathRsp>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length < 1) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(fileUploadService.uploadFiles(files));
    }

    @ApiOperation("上传图片,带缩略图")
    @PostMapping("/thump")
    @EditPermissionUnwanted
    public ApiReturn<ThumbPathRsp> uploadPicture(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ApiReturn.fail(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(fileUploadService.uploadPicture(file));
    }
}
