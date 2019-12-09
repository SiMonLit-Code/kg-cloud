package com.plantdata.kgcloud.domain.j2r.entity;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 属性J2r映射
 * @author xiezhenxiang 2019/12/9
 **/
@Data
public class AttrConfig {

    private Integer id;
    /** 属性名称 */
    private String name;
    /** 数值属性类型 */
    private Integer dataType;
    /** 属性类型 0数值 1关系 2标引 */
    private Integer type;
    /** 属性jsonPath */
    private String jsonPath;
    /** 值域 */
    private String range;
    /** 定义域 */
    private String domain;
    /** 映射方式 1全映射 2索引映射 */
    private String mapType;
    private String tableAttrRow;
    private String tableAttrCol;
    private String tableAttrIndex;

    public Integer getMapType() {
        return StringUtils.isNotEmpty(mapType) ? Integer.parseInt(mapType) : null;
    }

    public Integer getTableAttrRow() {
        return StringUtils.isNotEmpty(tableAttrRow) ? Integer.parseInt(tableAttrRow) : null;
    }

    public Integer getTableAttrCol() {
        return StringUtils.isNotEmpty(tableAttrCol) ? Integer.parseInt(tableAttrCol) : null;
    }

    public Integer getTableAttrIndex() {
        return StringUtils.isNotEmpty(tableAttrIndex) ? Integer.parseInt(tableAttrIndex) : null;
    }
}
