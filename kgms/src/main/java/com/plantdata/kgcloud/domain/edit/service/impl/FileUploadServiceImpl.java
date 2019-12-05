package com.plantdata.kgcloud.domain.edit.service.impl;

import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.domain.edit.rsp.ThumbPathRsp;
import com.plantdata.kgcloud.domain.edit.service.FileUploadService;
import com.plantdata.kgcloud.dto.FastdfsPathDto;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:49
 * @Description:
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FastdfsTemplate fastdfsTemplate;

    @Override
    public FilePathRsp uploadFile(MultipartFile file) {
        FastdfsPathDto fastdfsPathDto = fastdfsTemplate.uploadFile(file);
        return ConvertUtils.convert(FilePathRsp.class).apply(fastdfsPathDto);
    }

    @Override
    public ThumbPathRsp uploadPicture(MultipartFile file) {
        FastdfsPathDto fastdfsPathDto = fastdfsTemplate.uploadFile(file);
        return ConvertUtils.convert(ThumbPathRsp.class).apply(fastdfsPathDto);
    }
}
