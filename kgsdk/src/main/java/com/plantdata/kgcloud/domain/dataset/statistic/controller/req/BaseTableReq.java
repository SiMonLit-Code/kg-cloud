package com.plantdata.kgcloud.domain.dataset.statistic.controller.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 14:20
 */
@Getter
@Setter
@ApiModel
public class BaseTableReq extends BaseReq {
    @ApiModelProperty("es query条件")
    private String query;
    @ApiModelProperty("要查询的字段")
    private List<String> fields;
}
