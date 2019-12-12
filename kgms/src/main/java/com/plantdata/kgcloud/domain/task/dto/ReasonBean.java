package com.plantdata.kgcloud.domain.task.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReasonBean {
    private long count;

    private String ruleName;

    private List<TripleBean> tripleList;

}
