package com.plantdata.kgcloud.plantdata.rsp.app;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class InitGraphBean {

    private Integer id;
    private String kgName;
    private String type;
    private Map<String, Object> config;
    private List<InitGraphEntity> entities;
    private Date createTime;
    private Date updateTime;

    @Getter
    @Setter
    public static class InitGraphEntity {
        private Integer id;
        private String name;
        private Long classId;
    }
}