package com.plantdata.kgcloud.domain.app.util;

import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ExcelUtils {


//    /**
//     * @param name   sheet名
//     * @param style  Excel类型
//     * @param titles 标题串
//     * @return T\F
//     */
//    public static boolean generateWorkbook(String name, String style, List<String> titles, List<List<?>> sparqlNodeBeans) {
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        ByteArrayOutputStream os;
//        InputStream is = null;
//
//
//        Workbook workbook;
//        if ("XLS".equals(style.toUpperCase())) {
//            workbook = new HSSFWorkbook();
//        } else {
//            workbook = new XSSFWorkbook();
//        }
//        // 生成一个表格
//        Sheet sheet = workbook.createSheet(name);
//
//        /*
//         * 创建标题行
//         */
//        Row row = sheet.createRow(0);
//
//        for (int i = 0; i < titles.size(); i++) {
//            Cell cell = row.createCell(i);
//            cell.setCellValue(titles.get(i) + "(value)");
//            sheet.setColumnWidth(i, 50 * 256);
//        }
//        /*
//         * 写入正文
//         */
//
//        int index = 0;
//
//        for (List<?> sparqlNodeBeanList : sparqlNodeBeans) {
//            index++;
//            int cellIndex = -1;
//            Row oneRow = sheet.createRow(index);
//            for (SparqlNodeBean sparqlNodeBean : sparqlNodeBeanList) {
//                String value = sparqlNodeBean.getValue();
////                String uri = sparqlNodeBean.getUri();
//                Cell valueCell = oneRow.createCell(++cellIndex);
//                valueCell.setCellValue(value);
//            }
//        }
//
//        /*
//         * 写入到文件中
//         */
//        boolean isCorrect = false;
//
//        os = new ByteArrayOutputStream();
//        try {
//            workbook.write(os);
//            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            byte[] content = os.toByteArray();
//            is = new ByteArrayInputStream(content);
//            // 设置response参数
//            response.reset();
//            response.setContentType("application/vnd.ms-excel;charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String((name + ".xls").getBytes(), "iso-8859-1"));
//            ServletOutputStream out = response.getOutputStream();
//
//            bis = new BufferedInputStream(is);
//            bos = new BufferedOutputStream(out);
//            byte[] buff = new byte[2048];
//            int bytesRead;
//            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//                bos.write(buff, 0, bytesRead);
//            }
//            isCorrect = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) {
//                    bis.close();
//                }
//                if (bos != null) {
//                    bos.close();
//                }
//                os.close();
//                if (is != null) {
//                    is.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return isCorrect;
//    }
//
//    /**
//     * Create a library of cell styles
//     */
//    private static Map<String, CellStyle> createStyles(Workbook wb) {
//        Map<String, CellStyle> styles = Maps.newHashMap();
//        DataFormat dataFormat = wb.createDataFormat();
//
//        // 标题样式
//        CellStyle titleStyle = wb.createCellStyle();
//        titleStyle.setLocked(true);
//        titleStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
//        titleStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
//        Font titleFont = wb.createFont();
//        titleFont.setFontHeightInPoints((short) 16);
//        titleFont.setBold(true);
//        titleFont.setFontName("微软雅黑");
//        titleStyle.setFont(titleFont);
//        styles.put("title", titleStyle);
//
//        // 文件头样式
//        CellStyle headerStyle = wb.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setWrapText(true);
//        Font headerFont = wb.createFont();
//        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setColor(IndexedColors.WHITE.getIndex());
//        titleFont.setFontName("微软雅黑");
//        headerStyle.setFont(headerFont);
//        styles.put("header", headerStyle);
//
//        // 正文样式
//        CellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setWrapText(true);
//        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        styles.put("cell", cellStyle);
//
//        return styles;
//    }
}
