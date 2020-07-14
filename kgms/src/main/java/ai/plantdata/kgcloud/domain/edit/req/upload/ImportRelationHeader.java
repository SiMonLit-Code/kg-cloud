package ai.plantdata.kgcloud.domain.edit.req.upload;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 17:21
 * @Description:
 */
@Data
public class ImportRelationHeader {

    @ExcelProperty("定义域（必填，实例的概念类型）")
    private String domainConcept;

    @ExcelProperty("定义域消歧标识")
    private String domainMeaningTag;

    @ExcelProperty("定义域实例名称（必填）")
    private String domainEntity;

    @ExcelProperty("实例消歧标识")
    private String domainEntityMeaningTag;

    @ExcelProperty("关系名称（必填）")
    private String relationName;

    @ExcelProperty("值域实例名称（必填）")
    private String rangeEntity;

    @ExcelProperty("值域实例消歧标识")
    private String rangeEntityMeaningTag;

    @ExcelProperty("关系值域（必填，关系实例的概念类型）")
    private String rangeConcept;

    @ExcelProperty("关系值域概念消歧标识")
    private String rangeMeaningTag;

    @ExcelProperty("数据来源")
    private String source;

    @ExcelProperty("置信度")
    private String reliability;
}
