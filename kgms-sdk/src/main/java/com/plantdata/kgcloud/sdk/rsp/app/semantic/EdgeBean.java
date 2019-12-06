package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author ?
 */
@Getter
@Setter
@ApiModel("边结果")
public class EdgeBean {
    private Integer id;
    private Map<String, Object> attr;
    private String name;

}
