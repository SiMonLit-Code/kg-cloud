package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/26 10:16
 */
@Getter
@Setter
public class GraphEntityRsp extends BasicEntityRsp {

    @ApiModelProperty("默认顶层概念id传replaceClassId则为replaceClassId")
    private Long classId;
    @ApiModelProperty("直接父概念名称")
    private String conceptName;
    @ApiModelProperty("所有概念id集合，包含顶层父概念")
    private List<Long> conceptIdList;
    @ApiModelProperty("坐标信息")
    private CoordinateReq coordinates;
    @ApiModelProperty("节点样式")
    private Map<String, Object> nodeStyle;
    @ApiModelProperty("label样式")
    private Map<String, Object> labelStyle;
}
