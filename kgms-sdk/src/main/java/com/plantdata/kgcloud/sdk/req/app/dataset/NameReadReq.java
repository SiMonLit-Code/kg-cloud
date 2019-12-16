package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 17:32
 */
@Getter
@Setter
@ApiModel
public class NameReadReq extends BaseTableReq{
    private String dataName;
}
