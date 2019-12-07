package com.plantdata.kgcloud.domain.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordContent {

    private String htmlText;

    private Integer level;

    private Integer index;

    private String text;

    //表示是否有文档结构 ， 1表示有，0表示无
    private Integer structure;
}
