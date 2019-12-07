package com.plantdata.kgcloud.domain.document.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocHtmlRsp {

    private String htmlText;

    private String level;

    private String number;

    private Integer id;

    private String pClass;

    private String text;

    //    表示是否有文档结构 ， 1表示有，0表示无
    private Integer structure;
}
