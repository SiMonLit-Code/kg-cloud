package com.plantdata.kgcloud.domain.dw.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.DataTypeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.util.CommonUtil;
import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
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
    private int errorNum;
    private boolean error;

    private Row titleRow;
    private List<Row> errorRows;

    private static final Map<String,List<String>> titles = Maps.newHashMap();
    public static final String CONCEPT = "concept";
    public static final String ENTITY = "entity";
    public static final String ATTRIBUTE = "attribute";
    public static final String RELATION = "relation";
    public static final String SPECIAL = "special";
    public static final String SYNONYM = "synonym";
    public static final String DOMAIN = "domain";

    static {
        //概念
        titles.put(CONCEPT, Lists.newArrayList("父概念（必填）", "父概念消歧标识", "子概念（必填）", "子概念消歧标识"));
        //实体
        titles.put(ENTITY, Lists.newArrayList("实例名称（必填）", "消歧标识", "简介", "数据来源", "置信度","GIS名称","经度","纬度"));
        //数值、对象属性定义
        titles.put(ATTRIBUTE, Lists.newArrayList("属性名称(必填)", "别名", "属性定义域(必填)", "属性定义域的消歧标识"));
        //关系
        titles.put(RELATION, Lists.newArrayList("定义域（必填，实例的概念类型）", "定义域消歧标识", "定义域实例名称（必填）", "实例消歧标识","关系名称（必填）","值域实例名称（必填）"));
        //特定关系
        titles.put(SPECIAL, Lists.newArrayList("实例名称（必填）", "实例消歧标识", "关系实例名称（必填）", "关系实例消歧标识","关系值域（必填，关系实例的概念类型）"));
        //同义词
        titles.put(SYNONYM, Lists.newArrayList("id", "概念或实体名称（必填）", "同义词A（必填）", "同义词B","同义词C","..."));
        //领域词
        titles.put(DOMAIN, Lists.newArrayList("领域词（必填）", "对应实体id", "词性", "词频"));

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
        checkTitle(row, key,false);
    }

    public void checkTitle(Row row, String key, boolean isGisOpen){
        List<String> title = titles.get(key);
        for (int i = 0; i < title.size(); i++) {
            if(!isGisOpen && i == 5){//gis不开启 后续三个不校验
                break;
            }
            String cellValue = getCellValue(row.getCell(i));
            //只校验固定表头
            if(cellValue == null || !cellValue.trim().equals(title.get(i).trim())){
                throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
            }
        }
        this.titleRow = row;
    }
    public ByteArrayOutputStream parse(Parser parser) {
        if (wb.getNumberOfSheets() != 0) {
            this.errorNum = wb.getSheetAt(0).getRow(0).getLastCellNum();
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
                if (i == 0) {
                    this.errorNum = row.getLastCellNum();
                }
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
            this.errorNum = sheetAt.getRow(0).getLastCellNum();
            parseRows.parse(sheetAt);
        }
        return outStream();
    }

    private ByteArrayOutputStream outStream() {
        return outputStream(true);
    }

    private ByteArrayOutputStream outputStream(boolean isNew) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook wb = this.wb;
        if(isNew){
            if(errorRows == null){
                return outputStream;
            }
            errorRows.add(0,titleRow);
            wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            int index = 0;
            for (Row errRow : errorRows) {
                Row row = sheet.createRow(index++);
                for (int i = 0; i <= errorNum; i++) {
                    write(row,i,getCellValue(errRow.getCell(i)),wb,i == errorNum);
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

    public void buildErrorMsg(Row row, String msg) {
        if(errorRows == null){
            errorRows = Lists.newArrayList();
        }
        errorRows.add(row);
        this.error = true;
        write(row,errorNum,msg,wb,true);
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
                    result = cell.getCellFormula();
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
//    public String parse(Parser parser) {
//        if (wb.getNumberOfSheets() != 0) {
//            this.errorNum = wb.getSheetAt(0).getRow(0).getLastCellNum();
//            parser.parse(wb);
//        }
//        return saveLocal();
//    }
//    public String parse(ParseRow parseRow, int sheet) {
//        if (wb.getNumberOfSheets() != 0) {
//            Sheet sheetAt = wb.getSheetAt(sheet);
//            int rowNum = sheetAt.getPhysicalNumberOfRows();
//            for (int i = 0; i <= rowNum; i++) {
//                Row row = sheetAt.getRow(i);
//                if (i == 0) {
//                    this.errorNum = row.getLastCellNum();
//                }
//                if (row != null) {
//                    parseRow.parse(row, i);
//                }
//            }
//        }
//        return saveLocal();
//    }
//    public String parse(ParseRows parseRows, int sheet) {
//        if (wb.getNumberOfSheets() != 0) {
//            Sheet sheetAt = wb.getSheetAt(sheet);
//            this.errorNum = sheetAt.getRow(0).getLastCellNum();
//            parseRows.parse(sheetAt);
//        }
//        return saveLocal();
//    }
}
