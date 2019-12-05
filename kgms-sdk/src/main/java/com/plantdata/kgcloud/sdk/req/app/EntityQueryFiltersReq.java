package com.plantdata.kgcloud.sdk.req.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 10:36
 */@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EntityQueryFiltersReq {

    private Integer attrId;
    private String eq;
    private Object gt;
    private Object lt;
    private Object gte;
    private Object lte;
    private List<Object> in;
    private List<Object> nin;
    private Object ne;
    private Integer relation;
}
