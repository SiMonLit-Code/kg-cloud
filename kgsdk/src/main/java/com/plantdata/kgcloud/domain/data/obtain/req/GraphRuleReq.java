package com.plantdata.kgcloud.domain.data.obtain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/14 11:07
 */
@Getter
@Setter
@ApiModel("kgQl规则创建参数")
public class GraphRuleReq {
    @ApiModelProperty("规则id")
    private Integer ruleId;
    @ApiModelProperty("规则名称")
    private String ruleName;
    @ApiModelProperty("kgQl语句")
    private String ruleKgQl;
}
