package com.plantdata.kgcloud.domain.edit.req.merge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 14:23
 * @Description:
 */
@Data
@ApiModel("实体合并模型")
public class EntityMergeReq {

    @NotNull
    @ApiModelProperty("主实体")
    private Long entityId;

    @NotNull
    @ApiModelProperty("待合并的实体列表")
    private List<Long> toMergeEntityIds;

    @ApiModelProperty(value = "合并规则,0:以主体合并,1:以置信度合并,2:以来源合并")
    private Integer type;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty("0：默认主不存在使用被合并的，1 直接删除，2 合并（用//分割），3 表示使用被合并实体的值")
    private Integer mode;

}
