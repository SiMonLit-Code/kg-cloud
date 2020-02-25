package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 */
@Getter
@Setter
public class DataSetOneFieldReq  extends PageReq{
    @ApiModelProperty("es query条件 例：{\"query\":{\"match_phrase\":{\"{field}\":\"{value}}\"}}}  field:数据集key; value: 数据集的值" +
            "mongo query条件 {\"query\":{\"{field}\":\"{value}}\"}}")
    private Map<String, Object> query;
    @ApiModelProperty("要查询的字段")
    private String field;
    @ApiModelProperty("是否去重 默认true")
    private Boolean distinct = true;
    @ApiModelProperty("mongodb 语法 例： {\"{field}\":{sort}}  field:数据集key; sort :-1反向 1正向")
    private Map<String, Object> sort;
}
