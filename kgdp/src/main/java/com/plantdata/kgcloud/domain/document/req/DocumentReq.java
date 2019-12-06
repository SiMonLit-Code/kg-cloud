package com.plantdata.kgcloud.domain.document.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DocumentReq extends BaseReq {

    @ApiModelProperty(value = "场景id",required = true)
    private Integer sceneId;

    @ApiModelProperty(value = "文档名称")
    private String name;

    @ApiModelProperty(value = "文档类型")
    private String docType;

    @ApiModelProperty(value = "html内容")
    private String html;

    @ApiModelProperty(value = "文档id")
    private Integer id;

    @ApiModelProperty(value = "文档状态 0:文档获取  1:模式提取 2.结构拆解 3.文档标注 4.结果集 5.知识入图 6.处理完成")
    private Integer status;


    @ApiModelProperty(value = "入图模式")
    private Integer model;

}
