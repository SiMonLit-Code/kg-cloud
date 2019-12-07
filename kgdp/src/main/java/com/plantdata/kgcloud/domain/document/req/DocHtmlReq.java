package com.plantdata.kgcloud.domain.document.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DocHtmlReq {

    @ApiModelProperty(value = "html记录id")
    private Integer htmlId;

    @ApiModelProperty(value = "标题等级")
    private Integer level;

    @ApiModelProperty(value = "修改后的html内容")
    private String html;

}
