package com.plantdata.kgcloud.sdk.req;

import com.google.gson.JsonObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 14:02
 **/
@Data
public class DataSetSchema {

    @NotBlank
    private String field;

    @NotNull
    private Integer type;

    private int isIndex;

    private JsonObject settings;
}
