package com.plantdata.kgcloud.domain.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TitleBean {

    //    表示是否有文档结构 ， 1表示有，0表示无
    private Integer structure;
    private List<TitleLevel> titleList;

}
