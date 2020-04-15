package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cjw
 * @date 2020/4/15  13:58
 */
@Data
@ApiModel("数仓数据搜索参数")
public class DwTableDataSearchReq extends PageReq {
    @ApiModelProperty(value = "关键字",required = true)
    private String kw;
    @ApiModelProperty(value = "搜索所在的字段 默认全部")
    private List<String> fields;
}
