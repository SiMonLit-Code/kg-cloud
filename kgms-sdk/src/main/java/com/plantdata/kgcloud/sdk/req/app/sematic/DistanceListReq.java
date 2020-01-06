package com.plantdata.kgcloud.sdk.req.app.sematic;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 15:49
 */
@Getter
@Setter
@ApiModel("层数-参数")
public class DistanceListReq {

    @NotNull
    @ApiModelProperty(
            value = "实体Id列表",
            required = true
    )
    private List<Long> ids;
    private BaseReq page;

}
