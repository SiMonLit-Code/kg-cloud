package com.plantdata.kgcloud.plantdata.rsp.data;

import lombok.Data;

@Data
public class TreeBean {
    /**
     * 实体的id
     */
    private long id;

    /**
     * 节点名称
     */
    private String name;
    private String key;
    /**
     * 节点唯一标识
     */
    private String meaningTag;
    /**
     * 父概念id
     */
    private Long parentId;
}
