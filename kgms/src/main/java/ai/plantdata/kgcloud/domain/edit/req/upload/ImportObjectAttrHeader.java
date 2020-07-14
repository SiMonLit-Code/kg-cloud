package ai.plantdata.kgcloud.domain.edit.req.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 17:21
 * @Description:
 */
@Data
public class ImportObjectAttrHeader {

    @ExcelProperty("属性名称(必填)")
    private Long attrName;

    @ExcelProperty("别名")
    private String alias;

    @ExcelProperty("属性定义域(必填)")
    private String domainName;

    @ExcelProperty("属性定义域的消歧标识")
    private String domainMeaningTag;

    @ExcelProperty("属性值域(必填)")
    private String range;

    @ExcelProperty("属性值域的消歧标识")
    private String rangeMeaningTag;
}
