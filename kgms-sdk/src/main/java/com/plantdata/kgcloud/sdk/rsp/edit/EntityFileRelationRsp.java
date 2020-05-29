package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @create 2020/4/27 22:10
 */
@Data
public class EntityFileRelationRsp {

    private String id;

    @ApiModelProperty("标引文件ID")
    private String fileId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("标引类型(0：文件,1：文本,2：链接)")
    private Integer indexType;

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty(value = "是否关联实体(1：关联，2：不关联)")
    private Integer isRelatedEntity;

    @ApiModelProperty("关联的实体信息")
    private List<EntityInfoRsp> entityInfoList;

    @ApiModelProperty("文件路径")
    private String path;

}
