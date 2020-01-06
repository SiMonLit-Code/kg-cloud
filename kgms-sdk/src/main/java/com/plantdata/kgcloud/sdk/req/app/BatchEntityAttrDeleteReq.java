package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 13:19
 */
@Getter
@Setter
@ApiModel("删除实体属性参数")
public class BatchEntityAttrDeleteReq {

    @NotNull
    private List<Long> entityIds;
    @ApiModelProperty("不传则删除所有")
    private List<Integer> attributeIds;
    @ApiModelProperty("不传则删除所有")
    private List<String> attrNames;

}
