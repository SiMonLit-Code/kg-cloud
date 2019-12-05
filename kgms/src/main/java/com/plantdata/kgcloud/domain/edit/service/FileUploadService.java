package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.domain.edit.rsp.ThumbPathRsp;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 15:42
 * @Description:
 */
public interface FileUploadService {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    FilePathRsp uploadFile(MultipartFile file);

    /**
     * 上传图片带缩略图
     *
     * @param file
     * @return
     */
    ThumbPathRsp uploadPicture(MultipartFile file);
}
