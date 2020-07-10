package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 14:20
 */
@Getter
@Setter
public class BaseTableReq extends PageReq {

    @ApiModelProperty("要查询的字段")
    private List<String> fields;
    @ApiModelProperty(value = "普通数据集 例：{\"field\":\"value\"} " +
            "搜索数据集 例：{\"match_phrase\":{\"{field}\":\"{value}}\"}}" +
            "field:数据集key; value: 数据集的值 ",required = true)
    private Map<String, Object> query;
    @ApiModelProperty("搜索数据集  例： {\"field\":\"sort\"}  field:数据集key; sort :desc反向 asc正向" +
            "普通数据集  例： {\"field\":\"sort\"}  field:数据集key; sort :-1反向 1正向")
    private Map<String, Object> sort;

}
