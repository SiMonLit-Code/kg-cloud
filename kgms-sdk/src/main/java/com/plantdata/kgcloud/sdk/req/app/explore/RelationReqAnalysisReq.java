package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.validator.ListLengthCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 15:46
 */
@Getter
@Setter
@ApiModel("关联分析-参数")
public class RelationReqAnalysisReq extends BasicGraphExploreReq implements GraphRelationReqInterface {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @NotNull
    @Valid
    @ApiModelProperty(value = "关联搜索参数", required = true)
    private CommonRelationReq relation;

    @Override
    public CommonRelationReq fetchRelation() {
        return relation;
    }
}
