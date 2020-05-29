package com.plantdata.kgcloud.domain.edit.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.domain.edit.rsp.ThumbPathRsp;
import com.plantdata.kgcloud.domain.edit.service.FileUploadService;
import com.plantdata.kgcloud.dto.FastdfsPathDto;
import com.plantdata.kgcloud.exception.BizException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        // 无后缀且文件名为中文 || 有后缀且后缀含有中文
        if ((filename.equals(suffix) && isChineseChar(filename)) || (!filename.equals(suffix) && isChineseChar(suffix))) {
            throw BizException.of(KgmsErrorCodeEnum.UPLOAD_FILENAME_NOT_TRUE);
        }
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
                // 无后缀且文件名为中文 || 有后缀且后缀含有中文
                if ((originalFilename.equals(fileExtName) && isChineseChar(originalFilename))
                        || (!originalFilename.equals(fileExtName) && isChineseChar(fileExtName))) {
                    throw BizException.of(KgmsErrorCodeEnum.UPLOAD_FILENAME_NOT_TRUE);
                }
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
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        // 无后缀且文件名为中文 || 有后缀且后缀含有中文
        if ((filename.equals(suffix) && isChineseChar(filename)) || (!filename.equals(suffix) && isChineseChar(suffix))) {
            throw BizException.of(KgmsErrorCodeEnum.UPLOAD_FILENAME_NOT_TRUE);
        }
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

    /**
     * 判断是否为汉字
     *
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }
}
