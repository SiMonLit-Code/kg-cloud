package com.plantdata.kgcloud.domain.edit.req.merge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 16:28
 * @Description:
 */
@Data
@ApiModel("推荐实体合并模型")
public class RecommendedMergeReq {

    @NotNull
    @ApiModelProperty("主实体")
    private Long entityId;

    @NotNull
    @ApiModelProperty("待合并的实体列表")
    private List<Long> toMergeEntityIds;
}
