package com.plantdata.kgcloud.domain.edit.req.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 17:21
 * @Description:
 */
@Data
public class ImportFieldHeader {

    @ExcelProperty("领域词（必填）")
    private String domainName;

    @ExcelProperty("对应实体id")
    private String entityId;

    @ExcelProperty("词性")
    private String nature;

    @ExcelProperty("词频")
    private Double frequency;
}
