package com.plantdata.kgcloud.constant;

/**
 * @author cjw 2019-11-01 15:32:22
 */
public enum MetaDataNumEnum implements BaseEnum {
    /**
     * 元数据类型
     */
    creationTime(2),
    score(3),
    tag(4),
    source(11),
    reliability(12),
    batch(13),
    additional(14),
    gisCoordinate(15),
    gisAddress(16),
    openGis(17),
    entityLink(18),
    fromTime(19),
    toTime(20),
    sourceReason(111);
    private Integer value;

    MetaDataNumEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}