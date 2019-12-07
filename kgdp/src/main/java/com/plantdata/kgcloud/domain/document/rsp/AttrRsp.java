package com.plantdata.kgcloud.domain.document.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttrRsp {

    private Integer id;

    private Integer conceptId;

    private String name;

    private String oldName;

    private Integer type;

    private Integer dataType;

    private String rangeTo;

    private Boolean isIgnore;

    private Boolean isImportGraph;

    private Integer count;

    private String ps;

    private String attName;

    private Integer attId;

    private Integer attType;

    private Integer attDataType;

    private List<Long> attRange;

    private String attStatus;
}
