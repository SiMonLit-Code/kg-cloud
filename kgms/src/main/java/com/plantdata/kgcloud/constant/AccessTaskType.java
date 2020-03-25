package com.plantdata.kgcloud.constant;

import lombok.Getter;

@Getter
public enum AccessTaskType {

    KTR(0, "ktr"),
    KG(1, "kg"),
    DW(2, "dw"),
    SEARCH(3, "index"),
    NLP(4, "nlp"),
    TRANSFER(5, "transfer");

    private Integer type;
    private String displayName;

    AccessTaskType(Integer type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }
}
