package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 15:28
 */
@RestController
@RequestMapping("app/file/")
@Slf4j
public class FileOptController implements SdkOpenApiInterface {

    @ApiOperation("图片导出")
    @PostMapping("png/export")
    public ApiReturn exportPng(@RequestParam("name") String fileName, @RequestParam("data") String data) throws IOException {

        log.debug(fileName);

        if (!data.substring(0, 5).equals("data:")) {
            log.debug("The request did not include a valid 'data' parameter which must be a valid data-uri.\n");
            log.debug("Received input:\n");
            log.debug(data);
        }

        // divide data "data:image/png;base64,"
        int commaIndex = data.indexOf(",");
        int semicolonIndex = data.indexOf(";");
        if (semicolonIndex == -1) {
            semicolonIndex = commaIndex;
        }
        if (commaIndex == -1) {
            commaIndex = semicolonIndex;
        }

        // check valid type
        String fileType = data.substring(5, semicolonIndex);
        Map<String, String> validTypeMap = new HashMap<String, String>() {
            {
                put("image/jpeg", "jpeg");
                put("image/png", "png");
                put("application/pdf", "pdf");
                put("image/svg", "svg");
                put("text/csv", "csv");
                put("application/vnd.ms-excel", "xls");
                put("text/plain", "txt");
            }
        };

        String fileEnd = validTypeMap.get(fileType);
        if (fileEnd == null) {
            log.debug("Unsupported type.");
            return ApiReturn.success();
        }
        String mimeString = fileType != null ? fileType : "application/octet-stream";
        String fileNameString = fileName != null ? fileName : "export";
        boolean base64Boolean = data.substring(commaIndex - 7, commaIndex).equals(";base64");

        // split file name
        fileEnd = "." + fileEnd;
        if (!fileNameString.contains(fileEnd)) {
            fileNameString += fileEnd;
        }

        if (base64Boolean) {
            if (!data.contains(",")) {
                log.debug("Invalid data-uri format");
            }
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            response.setHeader("Content-Type", mimeString);
            response.setHeader("content-Disposition", "attachment; filename=\"" + fileNameString + "\"");
            response.setHeader("Cache-Control", "private, must-revalidate, max-age=0");
            String sub = data.substring(commaIndex + 1);
            log.debug(sub);
            byte[] bdata = Base64.getDecoder().decode(sub);
            ServletOutputStream out = response.getOutputStream();
            out.write(bdata);
        } else {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            response.setHeader("Content-Type", mimeString);
            response.setHeader("content-Disposition", "attachment; filename=\"" + fileNameString + "\"");
            response.setHeader("Cache-Control", "private, must-revalidate, max-age=0");
            String sub = data.substring(commaIndex + 1);
            String bData = URLDecoder.decode(sub, "UTF-8");
            log.debug(bData);
            ServletOutputStream out = response.getOutputStream();
            out.write(bData.getBytes());
        }
        return ApiReturn.success();
    }
}
