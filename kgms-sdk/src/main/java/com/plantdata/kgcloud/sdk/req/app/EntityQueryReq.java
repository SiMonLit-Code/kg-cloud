package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:53
 */
@Getter
@Setter
@ApiModel("实体查询参数")
public class EntityQueryReq extends BaseReq {
    @ApiModelProperty("实体所属概念ID")
    private Long conceptId;
    @ApiModelProperty("conceptId为空时生效")
    private String conceptKey;
    @ApiModelProperty("筛选条件{\"数值属性id\":{\"$eq\":\"字段全匹配\"}},{\"数值属性id\":{\"$gt\":\"大于\",\"$lt\":\"小于\"}}")
    private Map<String, Object> query;
}
