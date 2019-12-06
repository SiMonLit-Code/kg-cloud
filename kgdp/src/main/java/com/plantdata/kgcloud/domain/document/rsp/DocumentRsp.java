package com.plantdata.kgcloud.domain.document.rsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRsp {

    private Integer id;
    private Integer sceneId;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String source;
    private Integer docStatus;
    private Integer modelStatus;
    private String docType;
    private Long docSize;

}
