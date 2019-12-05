package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 11:30
 * @Description:
 */
@Getter
public enum MetaDataInfo {
    /**
     * 更新时间
     */
    UPDATE_TIME("2", "meta_data_2", "updateTime"),
    /**
     * 权重
     */
    SCORE("3", "meta_data_3", "score"),
    /**
     * 标签
     */
    TAG("4", "meta_data_4", "tags"),
    /**
     * 来源
     */
    SOURCE("11", "meta_data_11", "source"),
    /**
     * 来源理由
     */
    SOURCE_REASON("111", "meta_data_111", "sourceReason"),
    /**
     * 置信度
     */
    RELIABILITY("12", "meta_data_12", "reliability"),
    /**
     * 批次号
     */
    BATCH_NO("13", "meta_data_13", "batchNo"),
    /**
     * 业务额外信息
     */
    ADDITIONAL("14", "meta_data_14", "additional"),
    /**
     * gis坐标(list)
     */
    GIS_COORDINATE("15", "meta_data_15", "gisCoordinate"),
    /**
     * gis地点
     */
    GIS_ADDRESS("16", "meta_data_16", "gisAddress"),
    /**
     * gis开启标识
     */
    OPEN_GIS("17", "meta_data_17", "openGis"),
    /**
     * 实体关联
     */
    ENTITY_LINK("18", "meta_data_18", "entityLinks"),
    /**
     * 开始时间
     */
    FROM_TIME("19", "meta_data_19", "fromTime"),
    /**
     * 截止时间
     */
    TO_TIME("20", "meta_data_20", "toTime"),
    ;

    private final String code;

    private final String fieldName;

    private final String name;

    MetaDataInfo(String code, String fieldName, String name) {
        this.code = code;
        this.fieldName = fieldName;
        this.name = name;
    }

    /**
     * 通过字段名称获取code
     *
     * @param name
     * @return
     */
    public static String getCodeByName(String name) {
        for (MetaDataInfo metaDataInfo : MetaDataInfo.values()) {
            if (metaDataInfo.getName().equals(name)) {
                return metaDataInfo.getCode();
            }
        }
        //默认值
        return MetaDataInfo.UPDATE_TIME.getCode();
    }
}
