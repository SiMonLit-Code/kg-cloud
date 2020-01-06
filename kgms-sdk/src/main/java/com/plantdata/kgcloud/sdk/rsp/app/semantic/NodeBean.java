package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ?
 */
@Getter
@Setter
public class NodeBean {
    /**
     * 0 entity 1 literal
     */
    private int type;
    private Long id;
    private Long conceptId;
    private String name;
    private int valueType;
    private Object value;


}
