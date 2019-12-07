package com.plantdata.kgcloud.sdk.req.app.sematic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ProjectName: kg-services
 * @Package: ai.plantdata.kg.semantic.domain.qa.bean.req
 * @ClassName: QueryReq
 * @Author: zhangxc
 * @Description:
 * @Date: 2019/12/4 16:43
 * @Version: 1.0
 */
@Getter
@Setter
public class QueryReq {
    @NotBlank
    @ApiModelProperty("自然语言问题")
    private String query;
    @ApiModelProperty("默认0")
    private int pos;
    @ApiModelProperty("默认5")
    private int size;
}
