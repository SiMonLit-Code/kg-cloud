package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 17:22
 */
@ApiModel("gis关系")
@Getter
@Setter
public class GisRelationRsp extends GraphRelationRsp {
    private Long ruleId;
}
