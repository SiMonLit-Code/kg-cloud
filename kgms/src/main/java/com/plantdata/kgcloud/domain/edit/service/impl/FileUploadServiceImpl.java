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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public List<ThumbPathRsp> uploadFiles(MultipartFile[] files) {
        List<String> imageType = new ArrayList<>();
        imageType.add("JPG");
        imageType.add("JPEG");
        imageType.add("PNG");
        imageType.add("GIF");
        imageType.add("BMP");
        imageType.add("WBMP");
        List<ThumbPathRsp> rstList = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String fileExtName =
                    originalFilename.substring(Objects.requireNonNull(originalFilename).lastIndexOf(".") + 1);
            FastdfsPathDto fastdfsPathDto;
            if (imageType.contains(fileExtName.toUpperCase())) {
                fastdfsPathDto = fastdfsTemplate.uploadImage(file);
            } else {
                fastdfsPathDto = fastdfsTemplate.uploadFile(file);
            }
            ThumbPathRsp thumbPathRsp = ConvertUtils.convert(ThumbPathRsp.class).apply(fastdfsPathDto);
            thumbPathRsp.setFileName(fileExtName);
            rstList.add(thumbPathRsp);
        }
        return rstList;
    }

    @Override
    public ThumbPathRsp uploadPicture(MultipartFile file) {
        FastdfsPathDto fastdfsPathDto = fastdfsTemplate.uploadImage(file);
        return ConvertUtils.convert(ThumbPathRsp.class).apply(fastdfsPathDto);
    }


    public void download(String filePath, HttpServletResponse response) {
        int i = filePath.lastIndexOf("/") + 1;
        String fileName = filePath.substring(i);
        byte[] bytes = fastdfsTemplate.downloadFile(filePath);
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(),
                    "iso-8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
