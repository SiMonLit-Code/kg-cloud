package com.plantdata.kgcloud.domain.document.req;

import com.hiekn.pddocument.bean.PdDocument;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PdDocumentReq {

    @ApiModelProperty(value = "文档id")
    private Integer id;

    @ApiModelProperty(value = "文档标注内容")
    private List<PdDocument> pdDocumentList;

    @ApiModelProperty(value = "场景id")
    private Integer sceneId;


}
