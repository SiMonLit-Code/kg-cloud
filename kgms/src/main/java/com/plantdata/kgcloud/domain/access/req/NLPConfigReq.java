package com.plantdata.kgcloud.domain.access.req;

import lombok.Data;

import java.util.List;

@Data
public class NLPConfigReq {

    private List<Model> entity;

    private List<Model> relation;

    private List<Model> attribute;

    private List<Model> synonyms;

    private List<Model> segemnt;


    @Data
    class Model{
        private String name;
        private String uniqueId;
    }
}

