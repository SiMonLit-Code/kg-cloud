package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 17:07
 **/
@Data
public class ReasoningExecuteReq {

    @ApiModelProperty("推理规则id数组")
    private List<Integer> ids;

    @ApiModelProperty("关系")
    private List<GraphRelationRsp> relationList;

    @ApiModelProperty("实例列表")
    private List<CommonEntityRsp> entityList;
}
