package com.plantdata.kgcloud.domain.edit.req.file;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author EYE
 */
@Getter
@Setter
public class EntityFileRelationQueryReq extends BaseReq {

    @ApiModelProperty(value = "文件名")
    private String name;

}
