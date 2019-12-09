package com.plantdata.kgcloud.sdk.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 20:58
 **/
@Data
public class AnnotationReq {

    @NotNull
    private Integer dataId;

    private List<AnnotationConf> config;

    private String description;
}
