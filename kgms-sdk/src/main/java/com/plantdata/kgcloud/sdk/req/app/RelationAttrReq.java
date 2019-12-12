package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 16:55
 */
@Getter
@Setter
@ApiModel("边属性过滤")
public class RelationAttrReq extends CompareFilterReq {
    @ApiModelProperty("属性定义id")
    private Integer attrId;
    @ApiModelProperty("属性数据id")
    private Integer seqNo;
}
