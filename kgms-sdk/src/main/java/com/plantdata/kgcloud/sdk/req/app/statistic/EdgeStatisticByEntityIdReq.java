package com.plantdata.kgcloud.sdk.req.app.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 17:58
 */
@Getter
@Setter
@ApiModel("实体统计-关系度数-参数")
public class EdgeStatisticByEntityIdReq {

    @ApiModelProperty(value = "实体id",required = true)
    @NotNull
    private Long entityId;
    @ApiModelProperty("是否去重")
    private boolean distinct ;
    @Valid
    @ApiModelProperty("允许的属性定义id")
    private List<IdsFilterReq<Integer>> allowAttrDefIds;
    @Valid
    @ApiModelProperty("允许的概念id")
    private List<IdsFilterReq<Long>> allowConceptIds;

}
