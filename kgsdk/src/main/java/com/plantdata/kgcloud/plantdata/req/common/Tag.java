package com.plantdata.kgcloud.plantdata.req.common;

import com.plantdata.kgcloud.plantdata.link.LinkModel;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import lombok.Data;

@Data
@LinkModel(clazz = TagRsp.class)
public class Tag {
    private String name;
    private String source;
    private String creationTime;
    private Integer grade;

}
