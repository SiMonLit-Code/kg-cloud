package com.plantdata.kgcloud.domain.document.rsp;

import lombok.Data;

import java.util.List;

@Data
public class PageRsp<T> {

    private List<T> content;

    private Long totalPages;

    private Long totalElements;

    public PageRsp() {
    }

    public PageRsp(List<T> content, Long totalPages, Long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
