package com.plantdata.kgcloud.domain.data.obtain.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 11:07
 */
@Getter
@Setter
@ApiModel("kgQl规则视图")
public class GraphRuleRsp {
    @ApiModelProperty("规则id")
    private Integer ruleId;
    @ApiModelProperty("规则名称")
    private String ruleName;
    @ApiModelProperty("kgQl语句")
    private String ruleKgQl;
    @ApiModelProperty("概念解析ids")
    private List<Long> conceptIdList;
}
