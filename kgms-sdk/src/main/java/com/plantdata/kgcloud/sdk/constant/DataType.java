package com.plantdata.kgcloud.sdk.constant;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 21:36
 **/

public enum DataType {
    /**
     *
     */
    MYSQL(0),
    MONGO(1),
    ELASTIC(2),
    PD_DOCUMENT(3);

    private final int dataType;

    DataType(int dataType) {
        this.dataType = dataType;
    }

    public static DataType findType(int dataType) {
        for (DataType value : DataType.values()) {
            if (value.dataType == dataType) {
                return value;
            }
        }
        return DataType.MONGO;
    }

    public int getDataType() {
        return this.dataType;
    }
}
