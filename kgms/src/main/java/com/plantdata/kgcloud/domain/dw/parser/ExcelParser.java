package com.plantdata.kgcloud.domain.dw.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.DataTypeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.util.CommonUtil;
import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 解析上传的excel，支持xls,xlsx
 *
 * @author DingHao
 * @since 2019/8/9 11:06
 */
@Slf4j
public class ExcelParser {

    private boolean saveLocal;
    private static String uploadPath;
    private String outFilePath;

    public Workbook wb;
    private boolean error;

    private int conceptErrorNum = 4;
    private Row conceptTitleRow;
    private List<Row> conceptErrorRows;

    private int attrErrorNum = 6;
    private Row attrTitleRow;
    private List<Row> attrErrorRows;

    private int relationErrorNum = 6;
    private Row relationTitleRow;
    private List<Row> relationErrorRows;

    private int relationAttrErrorNum = 6;
    private Row relationAttrTitleRow;
    private List<Row> relationAttrErrorRows;

    private static final Map<String,List<String>> titles = Maps.newHashMap();
    public static final String CONCEPT = "concept";
    public static final String CONCEPT_KEY = "概念建模";
    public static final String ENTITY = "entity";
    public static final String ATTRIBUTE = "attribute";
    public static final String ATTRIBUTE_KEY = "属性建模";
    public static final String RELATION = "relation";
    public static final String RELATION_KEY = "关系建模";
    public static final String RELATION_ATTR = "relation_attr";
    public static final String RELATION_ATTR_KEY = "边属性建模";

    static {
        //概念
        titles.put(CONCEPT, Lists.newArrayList("父概念(必填)", "父概念消歧标识", "子概念(必填)", "子概念消歧标识"));
        //数值、对象属性定义
        titles.put(ATTRIBUTE, Lists.newArrayList("属性名称(必填)", "别名", "属性定义域(必填)", "属性定义域的消歧标识","数据类型(必填)","单位"));
        //关系
//        titles.put(RELATION, Lists.newArrayList("定义域（必填，实例的概念类型）", "定义域消歧标识", "定义域实例名称（必填）", "实例消歧标识","关系名称（必填）","值域实例名称（必填）"));
        titles.put(RELATION, Lists.newArrayList("关系名称(必填)", "别名", "关系定义域(必填)", "关系定义域的消歧标识","关系值域(必填)","关系值域的消歧标识"));

        titles.put(RELATION_ATTR, Lists.newArrayList("关系定义域(必填)", "关系定义域的消歧标识", "关系名称(必填)", "边属性名称(必填)","数据类型(必填)","单位"));

    }

    public ExcelParser(InputStream in, String originalFileName) {
        this(in,originalFileName,false);
    }

    public ExcelParser(InputStream in, String originalFileName, boolean saveLocal) {
        String filenameExtension = StringUtils.getFilenameExtension(originalFileName);
        if(!StringUtils.hasText(filenameExtension)){
            filenameExtension = "";
        }
        this.wb = readExcel(filenameExtension, in);
        this.saveLocal = saveLocal;
    }


    public void checkTitle(Row row, String key){
        checkTitle(row, key,false,key);
    }

    public void checkTitle(Row row, String key, boolean isGisOpen,String type){
        List<String> title = titles.get(key);
        for (int i = 0; i < title.size(); i++) {
            if(!isGisOpen && i == 5){//gis不开启 后续三个不校验
                break;
            }
            String cellValue = getCellValue(row.getCell(i));
            //只校验固定表头
            if(cellValue == null || !cellValue.trim().equals(title.get(i).trim())){
                throw BizException.of(KgmsErrorCodeEnum.MODEL_FILE_TITLE_ERROR);
            }
        }

        switch (type){
            case CONCEPT:
                this.conceptTitleRow = row;
                break;
            case ATTRIBUTE:
                this.attrTitleRow = row;
                break;
            case RELATION:
                this.relationTitleRow = row;
                break;
            case RELATION_ATTR:
                this.relationAttrTitleRow = row;
                break;
        }
    }

    public ByteArrayOutputStream parse(Parser parser) {

        if (wb.getNumberOfSheets() != 0) {
            parser.parse(wb);
        }
        return outStream();
    }

    public ByteArrayOutputStream parse(ParseRow parseRow, int sheet) {
        if (wb.getNumberOfSheets() != 0) {
            Sheet sheetAt = wb.getSheetAt(sheet);
            int rowNum = sheetAt.getPhysicalNumberOfRows();
            for (int i = 0; i <= rowNum; i++) {
                Row row = sheetAt.getRow(i);
                if (row != null) {
                    parseRow.parse(row, i);
                }
            }
        }
        return outStream();
    }

    public ByteArrayOutputStream parse(ParseRows parseRows, int sheet) {
        if (wb.getNumberOfSheets() != 0) {
            Sheet sheetAt = wb.getSheetAt(sheet);
            parseRows.parse(sheetAt);
        }
        return outStream();
    }

    private ByteArrayOutputStream outStream() {
        return outputStream(true);
    }

    private ByteArrayOutputStream outputStream(boolean isNew) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook wb = new XSSFWorkbook();
        if(isNew){

            if(conceptErrorRows != null){

                conceptErrorRows.add(0,conceptTitleRow);
                Sheet sheet = wb.createSheet(CONCEPT_KEY);
                int index = 0;
                for (Row errRow : conceptErrorRows) {
                    Row row = sheet.createRow(index++);
                    for (int i = 0; i <= conceptErrorNum; i++) {
                        write(row,i,getCellValue(errRow.getCell(i)),wb,i == conceptErrorNum);
                    }
                }

            }

            if(attrErrorRows != null){

                attrErrorRows.add(0,attrTitleRow);
                Sheet sheet = wb.createSheet(ATTRIBUTE_KEY);
                int index = 0;
                for (Row errRow : attrErrorRows) {
                    Row row = sheet.createRow(index++);
                    for (int i = 0; i <= attrErrorNum; i++) {
                        write(row,i,getCellValue(errRow.getCell(i)),wb,i == attrErrorNum);
                    }
                }

            }

            if(relationErrorRows != null){

                relationErrorRows.add(0,relationTitleRow);
                Sheet sheet = wb.createSheet(RELATION_KEY);
                int index = 0;
                for (Row errRow : relationErrorRows) {
                    Row row = sheet.createRow(index++);
                    for (int i = 0; i <= relationErrorNum; i++) {
                        write(row,i,getCellValue(errRow.getCell(i)),wb,i == relationErrorNum);
                    }
                }

            }

            if(relationAttrErrorRows != null){

                relationAttrErrorRows.add(0,relationAttrTitleRow);
                Sheet sheet = wb.createSheet(RELATION_ATTR_KEY);
                int index = 0;
                for (Row errRow : relationAttrErrorRows) {
                    Row row = sheet.createRow(index++);
                    for (int i = 0; i <= relationAttrErrorNum; i++) {
                        write(row,i,getCellValue(errRow.getCell(i)),wb,i == relationAttrErrorNum);
                    }
                }

            }

        }
        try {
            wb.write(outputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if(saveLocal){
            saveLocal(wb);
        }
        return outputStream;
    }

    private String saveLocal(Workbook wb) {
        try {
            FileOutputStream output = new FileOutputStream(outFilePath);
            wb.write(output);
            output.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return outFilePath;
    }

    public void buildErrorMsg(Row row, String msg,String type) {

        switch (type){

            case CONCEPT:

                if(conceptErrorRows == null){
                    conceptErrorRows = Lists.newArrayList();
                }
                conceptErrorRows.add(row);
                this.error = true;
                write(row,conceptErrorNum,msg,wb,true);
                break;
            case ATTRIBUTE:

                if(attrErrorRows == null){
                    attrErrorRows = Lists.newArrayList();
                }
                attrErrorRows.add(row);
                this.error = true;
                write(row,attrErrorNum,msg,wb,true);
                break;
            case RELATION:

                if(relationErrorRows == null){
                    relationErrorRows = Lists.newArrayList();
                }
                relationErrorRows.add(row);
                this.error = true;
                write(row,relationErrorNum,msg,wb,true);
                break;
            case RELATION_ATTR:

                if(relationAttrErrorRows == null){
                    relationAttrErrorRows = Lists.newArrayList();
                }
                relationAttrErrorRows.add(row);
                this.error = true;
                write(row,relationAttrErrorNum,msg,wb,true);
                break;

        }

    }

    public boolean hasError(){
        return error;
    }

    public void write(Row row, int column, String msg, Workbook wb, boolean red) {

        Cell cell = row.createCell(column);
        if(red){
            CellStyle cellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setColor(IndexedColors.RED.getIndex());
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
        }
        cell.setCellValue(msg);
    }


    public String getCellValue(Cell cell) {
        Object result = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case 1:
                    result = cell.getStringCellValue();
                    break;
                case 0:
                    if(DateUtil.isCellDateFormatted(cell)){
                        result = CommonUtil.formatDateTime(cell.getDateCellValue());
                    }else {
                        result = new DecimalFormat("#.#########").format(cell.getNumericCellValue());
                    }
                    break;
                case 4:
                    result = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case 2:
//                    result = cell.getCellFormula();
                    try {
                        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                        evaluator.setDebugEvaluationOutputForNextEval(true);
                        result = evaluator.evaluateInCell(cell).getRichStringCellValue();
                    } catch (IllegalStateException e) {
                        result = cell.getRichStringCellValue().getString();
                    }
                    break;
                case 5:
                    result = cell.getErrorCellValue();
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
        return result == null ? null : result.toString();
    }

    public String getCellValue(Cell cell, Integer type) {
        try{
            boolean b = DateUtil.isCellDateFormatted(cell);
            if (b) {
                if (DataTypeEnum.DATE.getType().equals(type)) {
                    return CommonUtil.formatDate(cell.getDateCellValue());
                } else if (DataTypeEnum.TIME.getType().equals(type)) {
                    return CommonUtil.formatTime(cell.getDateCellValue());
                } else {
                    return CommonUtil.formatDateTime(cell.getDateCellValue());
                }
            }
        }catch (Exception e){
            //ignore illegal time
        }
        return getCellValue(cell);
    }

    public Integer getCellIntValue(Cell cell) {
        String cellValue = getCellValue(cell);
        return cellValue == null ? null : Integer.valueOf(cellValue);
    }

    public Long getCellLongValue(Cell cell) {
        String cellValue = getCellValue(cell);
        return cellValue == null ? null : Long.valueOf(cellValue);
    }

    public Double getCellDoubleValue(Cell cell) {
        String cellValue = getCellValue(cell);
        return cellValue == null ? null : Double.valueOf(cellValue);
    }

    @FunctionalInterface
    public interface Parser {
        void parse(Workbook wb);
    }

    @FunctionalInterface
    public interface ParseRow {
        void parse(Row row, int index);
    }

    @FunctionalInterface
    public interface ParseRows {
        void parse(Sheet sheet);
    }

    private Workbook readExcel(String extentName, InputStream in) {
        Workbook wb = null;
        try {
            if ("xls".equals(extentName)) {
                wb = new HSSFWorkbook(in);
            } else if ("xlsx".equals(extentName)) {
                wb = new XSSFWorkbook(in);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return wb;
    }
}
