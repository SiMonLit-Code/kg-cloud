package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 11:30
 */
@Getter
@Setter
@ApiModel("开放平台实体")
public class OpenEntityRsp extends BasicEntityRsp {

    @ApiModelProperty("实体所属多概念IDList")
    private List<Long> conceptIdList;
    @ApiModelProperty("同义列表")
    private List<String> synonyms;
    @ApiModelProperty("摘要")
    private String abs;
    @ApiModelProperty("属性")
    private Map<String, Object> attributes;
}
