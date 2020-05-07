package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 17:32
 */
@Getter
@Setter
@ApiModel
public class NameReadReq extends BaseTableReq {
    private String dataName;
}
