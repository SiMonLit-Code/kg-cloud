package com.plantdata.kgcloud.domain.d2r.entity;

import lombok.Data;

import java.util.List;

/**
 * D2R映射配置实体
 * Created by bin on 2018/6/6.
 */
@Data
public class D2RMapperConfigBean {

    private D2RDataType dataType;

    private String kgName;

    private String dataSetId;

    private String userId;

    private Long conceptId;

    private Integer updateOpt;

    private List<String> entId;

    private List<EntMapperElementBean> ent;

    private SynonymMapperElementBean synonyms;

    private List<AttrMapperElementBean> attrs;

    private List<PrivateAttrBean> privateAttrs;

    private List<RelMapperElementBean> rels;
}

