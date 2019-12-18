package com.plantdata.kgcloud.domain.edit.req.attr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/18 16:02
 * @Description:
 */
@Getter
@Setter
@ApiModel("关系溯源条件查询")
public class RelationSearchMetaReq {
    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "置信度筛选,{$gt:0}")
    private Map<String, Object> reliability;
}
