package com.plantdata.kgcloud.domain.edit.rsp;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: lp
 */
@Setter
@Getter
public class EntityFileRsp {

    private String id;

    @ApiModelProperty("实体id")
    @JsonAlias("entity_id")
    private Long entityId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件路径")
    @JsonAlias("path")
    private String path;

    @ApiModelProperty("缩略图路径")
    @JsonAlias("thumb_path")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

}
