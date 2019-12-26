package com.plantdata.kgcloud.plantdata.constant;


import com.plantdata.kgcloud.sdk.constant.BaseEnum;

/**
 * @author Administrator
 */

public enum MetaDataEnum implements BaseEnum {
    creationTime("meta_data_2"),
    score("meta_data_3"),
    tag("meta_data_4"),
    source("meta_data_11"),
    reliability("meta_data_12"),
    batch("meta_data_13"),
    additional("meta_data_14"),
    gisCoordinate("meta_data_15"),
    gisAddress("meta_data_16"),
    openGis("meta_data_17"),
    entityLink("meta_data_18"),
    fromTime("meta_data_19"),
    toTime("meta_data_20"),
    sourceReason("meta_data_111");
    private String value;

    MetaDataEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}