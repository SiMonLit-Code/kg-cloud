package com.plantdata.kgcloud.plantdata.rsp.common;


import com.plantdata.kgcloud.plantdata.rsp.schema.Additional;

/**
 * @author Administrator
 */
public class AdditionalNull extends Additional {

    @Override
    public String getColor() {
        return "";

    }

    public static Additional newAdditionalNull() {
        return new AdditionalNull();
    }
}
