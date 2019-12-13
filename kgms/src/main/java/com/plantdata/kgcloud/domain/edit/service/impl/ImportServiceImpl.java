package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.UploadApi;
import ai.plantdata.kg.api.rdf.RdfApi;
import com.alibaba.excel.EasyExcel;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.ImportType;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportTemplateReq;
import com.plantdata.kgcloud.domain.edit.req.upload.RdfExportReq;
import com.plantdata.kgcloud.domain.edit.req.upload.RdfReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.ImportService;
import com.plantdata.kgcloud.domain.edit.vo.EntityAttrValueVO;
import com.plantdata.kgcloud.domain.edit.vo.GisVO;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 14:04
 * @Description:
 */
@Service
public class ImportServiceImpl implements ImportService {

    @Autowired
    private BasicInfoService basicInfoService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadApi uploadApi;

    @Autowired
    private RdfApi rdfApi;

    @Override
    public void getImportTemplate(String kgName, ImportTemplateReq importTemplateReq, HttpServletResponse response) {
        String fileName = "template" + KgmsConstants.FileType.XLSX;
        String type = importTemplateReq.getType();
        switch (Objects.requireNonNull(ImportType.getByType(type))) {
            case CONCEPT:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case ENTITY:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, kgName, importTemplateReq.getConceptId());
                break;
            case RELATION:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case SYNONYMY:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case NUMERICAL_ATTR:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case OBJECT_ATTR:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case SPECIFIC_RELATION:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            case FIELD:
                fileName = type + KgmsConstants.FileType.XLSX;
                download(fileName, response, ImportType.getClassType(type));
                break;
            default:
                break;
        }
    }

    /**
     * 实体模板下载
     *
     * @param fileName
     * @param response
     * @param kgName
     * @param conceptId
     */
    private void download(String fileName, HttpServletResponse response, String kgName, Long conceptId) {
        try {
            List<List<String>> header = getHeader(kgName, conceptId);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(),
                    "iso-8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream).head(header).sheet("Sheet1").doWrite(new ArrayList());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 实体表头
     *
     * @param kgName
     * @param conceptId
     * @return
     */
    private List<List<String>> getHeader(String kgName, Long conceptId) {
        List<List<String>> header = new ArrayList<>();
        header.add(Collections.singletonList("实例名称（必填）"));
        header.add(Collections.singletonList("消歧标识"));
        header.add(Collections.singletonList("简介"));
        header.add(Collections.singletonList("数据来源"));
        header.add(Collections.singletonList("置信度"));
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(conceptId).isEntity(false).build());
        GisVO gis = details.getGis();
        if (Objects.nonNull(gis) && gis.getIsOpenGis()) {
            header.add(Collections.singletonList("GIS名称"));
            header.add(Collections.singletonList("经度"));
            header.add(Collections.singletonList("纬度"));
        }
        List<EntityAttrValueVO> attrValue = details.getAttrValue();
        List<Integer> types = Arrays.asList(91, 92, 93);
        attrValue.stream().filter(vo -> AttributeValueType.isNumeric(vo.getType()) && !types.contains(vo.getDataType()))
                .forEach(vo -> header.add(Collections.singletonList(vo.getName() + "(" + vo.getId() + ")")));
        return header;
    }

    /**
     * 模板下载
     *
     * @param fileName
     * @param response
     * @param classType
     */
    private void download(String fileName, HttpServletResponse response, Class classType) {
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(),
                    "iso-8859-1"));
            EasyExcel.write(response.getOutputStream(), classType).sheet("Sheet1").doWrite(new ArrayList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String importConcepts(String kgName, MultipartFile file) {
        return handleUploadError(uploadApi.concept(kgName, file));
    }

    @Override
    public String importEntities(String kgName, Long conceptId, MultipartFile file) {
        return handleUploadError(uploadApi.entity(kgName, conceptId, file));
    }

    @Override
    public String importSynonyms(String kgName, MultipartFile file) {
        return handleUploadError(uploadApi.synonym(kgName, file));
    }

    @Override
    public String importAttrDefinition(String kgName, Integer type, MultipartFile file) {
        return handleUploadError(uploadApi.attribute(kgName, type, file));
    }

    @Override
    public String importDomain(String kgName, Long conceptId, MultipartFile file) {
        return handleUploadError(uploadApi.domain(kgName, conceptId, file));
    }

    @Override
    public String importRelation(String kgName, Integer mode, MultipartFile file) {
        return handleUploadError(uploadApi.relation(kgName, mode, file));
    }

    @Override
    public String importRelation(String kgName, Integer attrId, Integer mode, MultipartFile file) {
        return handleUploadError(uploadApi.relation(kgName, attrId, mode, file));
    }

    @Override
    public String importRdf(String kgName, MultipartFile file, RdfReq rdfReq) {
        return handleUploadError(rdfApi.importRdf(kgName,rdfReq.getFormat(),file));
    }

    @Override
    public String exportRdf(String kgName, RdfExportReq rdfExportReq) {
        ResponseEntity<byte[]> body = rdfApi.exportRdf(kgName, rdfExportReq.getScope(),
                rdfExportReq.getFormat());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(body.getBody()));
        StorePath storePath = storageClient.uploadFile(inputStream, body.getBody().length, rdfExportReq.getFormat(), null);
        return "/" + storePath.getFullPath();
    }

    /**
     * 错误文件处理
     *
     * @param body
     * @return
     */
    private String handleUploadError(ResponseEntity<byte[]> body) {
        if (!body.getStatusCode().equals(HttpStatus.CREATED)) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
        }
        List<String> hasError = body.getHeaders().get("HAS-ERROR");
        if (Objects.nonNull(hasError)) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(body.getBody()));
            StorePath storePath = storageClient.uploadFile(inputStream, body.getBody().length, "xlsx", null);
            return "/" + storePath.getFullPath();
        }
        return null;
    }
}
