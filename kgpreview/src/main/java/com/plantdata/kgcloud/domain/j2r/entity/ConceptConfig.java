package com.plantdata.kgcloud.domain.j2r.entity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 概念J2r映射
 * @author xiezhenxiang 2019/12/9
 **/
@Data
public class ConceptConfig {

    /** 概念Id */
    private Long conceptId;
    /** 实体唯一ID JsonPath */
    private List<String> idPath;
    /** 实体名称JsonPath */
    private String entityPath;
    /** 消歧标识JsonPath */
    private String meaningTagPath;
    /** 摘要JsonPath */
    private String absPath;
    /** 图片地址JsonPath */
    private String imgPath;
    /** 同义词JsonPath */
    private String synonymPath;
    /** 属性配置列表 */
    private List<AttrConfig> attrConfigs;
    /** Table名称 */
    private String tableName;
    /** Table所在JsonPath */
    private String tableJsonPath;
    /** table实体所在行 */
    private String tableEntityRow;
    /** table实体所在列 */
    private String tableEntityCol;
    /** table实体起始位置 */
    private String tableEntityIndex;

    public Integer getTableEntityRow() {
        return StringUtils.isNotEmpty(tableEntityRow) ? Integer.parseInt(tableEntityRow) : null;
    }

    public Integer getTableEntityCol() {
        return StringUtils.isNotEmpty(tableEntityCol) ? Integer.parseInt(tableEntityCol) : null;
    }

    public Integer getTableEntityIndex() {
        return StringUtils.isNotEmpty(tableEntityIndex) ? Integer.parseInt(tableEntityIndex) : null;
    }
}
