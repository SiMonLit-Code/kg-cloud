package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:58
 **/
@Data
public class AnnotationReq {

    @NotNull
    private Integer dataId;

    private Map<String, Object> config;

    private String description;
}
