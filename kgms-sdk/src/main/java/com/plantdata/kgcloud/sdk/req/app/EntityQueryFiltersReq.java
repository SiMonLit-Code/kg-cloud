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
public class EntityQueryFiltersReq extends CompareFilterReq{

    private Integer attrId;
    private Integer relation;
}
