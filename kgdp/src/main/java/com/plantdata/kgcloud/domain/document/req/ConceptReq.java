package com.plantdata.kgcloud.domain.document.req;

import com.plantdata.kgcloud.domain.document.entity.Attr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConceptReq {

    @ApiModelProperty(value = "概念的ID")
    private Integer id;

    @ApiModelProperty(value = "所属文档")
    private Integer documentId;

    @ApiModelProperty(value = "所属场景")
    private Integer sceneId;

    @ApiModelProperty(value = "概念名称")
    private String Name;

    @ApiModelProperty(value = "概念原名称")
    private String oldName;

    @ApiModelProperty(value = "是否入图")
    private Boolean isImportGraph;


    @ApiModelProperty(value = "概念的图谱ID")
    private Long conceptId;

    @ApiModelProperty(value = "概念的状态")
    private String conceptStatus;

    @ApiModelProperty(value = "概念下的属性字段")
    private List<Attr> attrs;

}
