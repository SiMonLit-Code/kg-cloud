package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.RdfApi;
import ai.plantdata.kg.api.edit.UploadApi;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.*;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.sdk.req.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportTemplateReq;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.ImportService;
import com.plantdata.kgcloud.domain.edit.util.MetaDataUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.sdk.req.edit.ExtraInfoVO;
import com.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.*;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.vo.GisVO;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

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
    private AttributeService attributeService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadApi uploadApi;

    @Autowired
    private RdfApi rdfApi;

    @Autowired
    private GraphApplicationService graphApplicationService;

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
                download(fileName, response, getHeader(kgName, importTemplateReq.getConceptId()));
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
                download(fileName, response, getHeader(kgName, importTemplateReq.getAttrId()));
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
     * 动态模板下载
     *
     * @param fileName
     * @param response
     * @param header
     */
    private void download(String fileName, HttpServletResponse response, List<List<String>> header) {
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(),
                    "iso-8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream).head(header).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("Sheet1").doWrite(new ArrayList());
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
        if (Objects.isNull(conceptId)) {
            throw BizException.of(KgmsErrorCodeEnum.ENTITY_TEMPLATE_NEED_CONCEPT_ID);
        }
        List<List<String>> header = new ArrayList<>();
        header.add(Collections.singletonList("实例名称（必填）"));
        header.add(Collections.singletonList("消歧标识"));
        header.add(Collections.singletonList("简介"));
        header.add(Collections.singletonList("数据来源"));
        header.add(Collections.singletonList("置信度"));
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(conceptId).isEntity(false).build());
        GisVO gis = details.getGis();
        if (gis != null && gis.getIsOpenGis() != null && gis.getIsOpenGis()) {
            header.add(Collections.singletonList("GIS名称"));
            header.add(Collections.singletonList("经度"));
            header.add(Collections.singletonList("纬度"));
        }
        List<AttrDefinitionRsp> attrDefinitionRsps = attributeService.getAttrDefinitionByConceptId(kgName,
                new AttrDefinitionSearchReq(conceptId));
        List<Integer> types = Arrays.asList(91, 92, 93);
        attrDefinitionRsps.stream().filter(vo -> AttributeValueType.isNumeric(vo.getType()) && !types.contains(vo.getDataType()))
                .forEach(vo -> header.add(Collections.singletonList(vo.getName() + "(" + vo.getId() + ")")));
        return header;
    }

    /**
     * 特定关系表头
     *
     * @param kgName
     * @param attrId
     * @return
     */
    private List<List<String>> getHeader(String kgName, Integer attrId) {
        if (Objects.isNull(attrId)) {
            throw BizException.of(KgmsErrorCodeEnum.SPECIFIC_TEMPLATE_NEED_ATTR_ID);
        }
        List<List<String>> header = new ArrayList<>();
        header.add(Collections.singletonList("实例名称（必填）"));
        header.add(Collections.singletonList("实例消歧标识"));
        header.add(Collections.singletonList("关系实例名称（必填）"));
        header.add(Collections.singletonList("关系实例消歧标识"));
        header.add(Collections.singletonList("关系值域（必填，关系实例的概念类型）"));
        header.add(Collections.singletonList("关系值域消歧标识"));
        header.add(Collections.singletonList("数据来源"));
        header.add(Collections.singletonList("置信度"));
        header.add(Collections.singletonList("开始时间"));
        header.add(Collections.singletonList("结束时间"));
        AttrDefinitionVO attrDetails = attributeService.getAttrDetails(kgName, attrId);
        List<ExtraInfoVO> extraInfo = attrDetails.getExtraInfo();
        if (CollectionUtils.isEmpty(extraInfo)) {
            return header;
        }
        extraInfo.stream().filter(vo -> AttributeValueType.isNumeric(vo.getType()))
                .forEach(vo -> header.add(Collections.singletonList(vo.getName())));
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
            EasyExcel.write(response.getOutputStream(), classType).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("Sheet1").doWrite(new ArrayList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String importConcepts(String kgName, MultipartFile file) {
        return handleUploadError(uploadApi.concept(KGUtil.dbName(kgName), file));
    }

    @Override
    public String importEntities(String kgName, Long conceptId, MultipartFile file) {
        return handleUploadError(uploadApi.entity(KGUtil.dbName(kgName), conceptId, JacksonUtils.writeValueAsString(MetaDataUtils.getDefaultSourceMetaData(null, SessionHolder.getUserId())), file));
    }

    @Override
    public String importSynonyms(String kgName, MultipartFile file) {
        return handleUploadError(uploadApi.synonym(KGUtil.dbName(kgName), file));
    }

    @Override
    public String importAttrDefinition(String kgName, Integer type, MultipartFile file) {
        return handleUploadError(uploadApi.attribute(KGUtil.dbName(kgName), type, file));
    }

    @Override
    public String importDomain(String kgName, Long conceptId, MultipartFile file) {
        return handleUploadError(uploadApi.domain(KGUtil.dbName(kgName), conceptId, file));
    }

    @Override
    public String importRelation(String kgName, Integer mode, MultipartFile file) {
        return handleUploadError(uploadApi.relation(KGUtil.dbName(kgName), mode, JacksonUtils.writeValueAsString(MetaDataUtils.getDefaultSourceMetaData(null, SessionHolder.getUserId())), file));
    }

    @Override
    public String importRelation(String kgName, Integer attrId, Integer mode, MultipartFile file) {
        return handleUploadError(uploadApi.relation(KGUtil.dbName(kgName), attrId, mode, JacksonUtils.writeValueAsString(MetaDataUtils.getDefaultSourceMetaData(null, SessionHolder.getUserId())), file));
    }

    @Override
    public String importRdf(String kgName, MultipartFile file, String userId, String format) {
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        // 文件校验
        if (("RDF/XML".equals(format) && "rdf".equals(suffix))
                || ("N-TRIPLES".equals(format) && "nt".equals(suffix))
                || ("TURTLE".equals(format) && "ttl".equals(suffix))
                || ("N3".equals(format) && "n3".equals(suffix))) {
            return handleUploadError(rdfApi.importRdf(KGUtil.dbName(kgName), format, userId, file));
        }
        throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
    }

    @Override
    public String exportRdf(String kgName, String format, Integer scope) {
        ResponseEntity<byte[]> body = rdfApi.exportRdf(KGUtil.dbName(kgName), scope,
                RdfType.findByFormat(format).getType());
        if (!body.getStatusCode().equals(HttpStatus.CREATED)) {
            throw BizException.of(KgmsErrorCodeEnum.RDF_EXPORT_ERROR);
        }
        List<String> hasError = body.getHeaders().get("HAS-ERROR");
        if (!CollectionUtils.isEmpty(hasError)) {
            throw BizException.of(KgmsErrorCodeEnum.RDF_EXPORT_ERROR);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(body.getBody()));
        StorePath storePath = storageClient.uploadFile(inputStream, body.getBody().length, format, null);
        return "/" + storePath.getFullPath();
    }

    @Override
    public void exportEntity(String kgName, HttpServletResponse response) {
        SchemaRsp schemaRsp = graphApplicationService.querySchema(kgName);

        String title = schemaRsp.getKgTitle() + "图谱实体概念模型";
        Map<String, List<List<String>>> dataMap = Maps.newHashMap();

        if (schemaRsp.getTypes() != null && !schemaRsp.getTypes().isEmpty()) {
            // 剔除不要的属性
            List<AttributeDefinitionGroupRsp> attrGroups = schemaRsp.getAttrGroups();
            attrGroups.clear();
            attrGroups = null;

            List<BaseConceptRsp> conceptList = schemaRsp.getTypes();
            List<AttributeDefinitionRsp> attrList = schemaRsp.getAttrs();
            Map<Long, String> conceptMap = Maps.newHashMap();
            for (BaseConceptRsp baseConceptRsp : conceptList) {
                conceptMap.put(baseConceptRsp.getId(), baseConceptRsp.getName());
            }


            for (BaseConceptRsp baseConceptRsp : conceptList) {
                Long conceptId = baseConceptRsp.getId();
                List<AttributeDefinitionRsp> collect = attrList == null ? new ArrayList<>() : attrList.stream()
                        .filter(attr -> conceptId.equals(attr.getDomainValue())).collect(Collectors.toList());

                List<List<String>> dataList = Lists.newArrayList();

                for (AttributeDefinitionRsp attr : collect) {
                    String rangeValue = attr.getRangeValue() == null ? "-" : attr.getRangeValue().stream()
                            .map(conceptMap::get).collect(Collectors.joining("、"));
                    if (StringUtils.isEmpty(rangeValue)) {
                        rangeValue = "-";
                    }

                    String extraInfo = attr.getExtraInfos() == null ? "-" : attr.getExtraInfos().stream()
                            .map(AttrExtraRsp::getName).collect(Collectors.joining("、"));
                    if (StringUtils.isEmpty(extraInfo)) {
                        extraInfo = "-";
                    }

                    List<String> list = Lists.newArrayList();
                    list.add(attr.getName());
                    list.add(CollectionUtils.isEmpty(attr.getRangeValue()) ? "数值" : "对象");
                    list.add(rangeValue);
                    list.add(extraInfo);
                    dataList.add(list);
                }
                if (collect.size() == 0) {
                    List<String> list = Lists.newArrayList();
                    list.add("-");
                    list.add("-");
                    list.add("-");
                    list.add("-");
                    dataList.add(list);
                }
                dataMap.put(conceptMap.get(conceptId), dataList);
                collect.clear();
                collect = null;
            }
            if (attrList != null) {
                attrList.clear();
            }
            attrList = null;
            conceptList.clear();
            conceptList = null;
        }
        try {
            XWPFDocument document = createWord(title, dataMap);

            response.setContentType("application/octet-stream");
            String dataName = schemaRsp.getKgTitle() + "图谱模式报告.doc";
            String fileName = URLEncoder.encode(dataName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            document.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XWPFDocument createWord(String title, Map<String, List<List<String>>> dataMap) throws IOException {
        XWPFDocument doc = new XWPFDocument();
        // 创建标题
        XWPFParagraph titleParagraph = doc.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleFun = titleParagraph.createRun();
        titleFun.setText(title);
        titleFun.setFontSize(11);

        List<String> parameters = Lists.newArrayList("属性名", "属性类型", "值域", "边属性");

        Set<String> set = dataMap.keySet();
        for (String concept : set) {
            // 创建表格
            XWPFTable table = doc.createTable(dataMap.get(concept).size() + 2, 4);
            // 设置列宽
            CTTblPr tblPr = table.getCTTbl().getTblPr();
            tblPr.getTblW().setType(STTblWidth.DXA);
            tblPr.getTblW().setW(new BigInteger("8000"));
            // 当前行
            int currectRow = 0;

            // 合并单元格
            for (int cellIndex = 0; cellIndex <= 3; cellIndex++) {
                XWPFTableCell cell = table.getRow(currectRow).getCell(cellIndex);
                if (cellIndex == 0) {
                    cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                } else {
                    cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                }
            }
            // 设置行高和颜色
            table.getRow(currectRow).getCtRow().addNewTrPr().addNewTrHeight().setVal(new BigInteger("400"));
            table.getRow(currectRow).getCell(0).getCTTc().addNewTcPr().addNewShd().setFill("D7D7D7");
            // 概念
            XWPFParagraph cParagraph = table.getRow(currectRow).getCell(0).getParagraphArray(0);
            cParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun cContent = cParagraph.createRun();
            cContent.setText(concept);
            currectRow++;
            // 参数
            for (int i = 0; i < parameters.size(); i++) {
                table.getRow(currectRow).getCtRow().addNewTrPr().addNewTrHeight().setVal(new BigInteger("400"));
                XWPFParagraph paragraph = table.getRow(currectRow).getCell(i).getParagraphArray(0);
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun content = paragraph.createRun();
                content.setText(parameters.get(i));
            }
            // 属性
            for (int i = 0; i < dataMap.get(concept).size(); i++) {
                currectRow++;
                for (int j = 0; j < parameters.size(); j++) {
                    table.getRow(currectRow).getCtRow().addNewTrPr().addNewTrHeight().setVal(new BigInteger("400"));
                    XWPFParagraph paragraph = table.getRow(currectRow).getCell(j).getParagraphArray(0);
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun content = paragraph.createRun();
                    content.setText(dataMap.get(concept).get(i).get(j));
                }
            }
            // 换行
            doc.createParagraph();
            dataMap.get(concept).clear();
        }
        return doc;
    }

    /**
     * 错误文件处理
     *
     * @param body
     * @return
     */
    private String handleUploadError(ResponseEntity<byte[]> body) {
        if (!body.getStatusCode().equals(HttpStatus.CREATED)) {
            RestResp restResp = JSON.parseObject(new String(body.getBody()), RestResp.class);
            if (restResp != null && restResp.getActionStatus() == RestResp.ActionStatusMethod.FAIL) {
                throw new BizException(KgmsErrorCodeEnum.FILE_IMPORT_ERROR.getErrorCode(), restResp.getErrorInfo());
            }
            throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
        }
        List<String> hasError = body.getHeaders().get("HAS-ERROR");
        if (!CollectionUtils.isEmpty(hasError)) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Objects.requireNonNull(body.getBody()));
            StorePath storePath = storageClient.uploadFile(inputStream, body.getBody().length, "xlsx", null);
            return "/" + storePath.getFullPath();
        }
        return null;
    }
}
