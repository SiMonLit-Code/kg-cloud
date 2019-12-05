package com.plantdata.kgcloud.domain.edit.req.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 17:21
 * @Description:
 */
@Data
public class ImportSynonymHeader {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("概念或实体名称（必填）")
    private String name;

    @ExcelProperty("同义词A（必填）")
    private String synA;

    @ExcelProperty("同义词B")
    private String synB;

    @ExcelProperty("同义词C")
    private String synC;

    @ExcelProperty("...")
    private String synMore;
}
