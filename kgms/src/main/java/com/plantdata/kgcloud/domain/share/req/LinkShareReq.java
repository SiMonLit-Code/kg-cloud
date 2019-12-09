package com.plantdata.kgcloud.domain.share.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by jdm on 2019/12/9 11:39.
 */
@Data
@ApiModel
public class LinkShareReq {

    @ApiModelProperty("链接")
    private String shareLink;
}
