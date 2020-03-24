package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/23 10:33
 * @Description:
 */
@Setter
@Getter
public class MultiModalRsp {

    private String id;

    @ApiModelProperty("实体id")
    private Long entityId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("数据连接")
    private String dataHref;

    @ApiModelProperty("缩略图路径")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

}
