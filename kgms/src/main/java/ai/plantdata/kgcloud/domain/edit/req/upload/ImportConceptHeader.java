package ai.plantdata.kgcloud.domain.edit.req.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 17:21
 * @Description:
 */
@Data
public class ImportConceptHeader {

    @ExcelProperty("父概念（必填）")
    private String parentConcept;

    @ExcelProperty("父概念消歧标识")
    private String parentMeaningTag;

    @ExcelProperty("子概念（必填）")
    private String sonConcept;

    @ExcelProperty("子概念消歧标识")
    private String sonMeaningTag;
}
