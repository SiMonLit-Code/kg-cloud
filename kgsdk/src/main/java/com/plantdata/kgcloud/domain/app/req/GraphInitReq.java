package com.plantdata.kgcloud.domain.app.req;

import com.plantdata.kgcloud.sdk.constant.GraphInitEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 15:17
 */
@ApiModel("图探索初始化参数")
@Getter
@Setter
public class GraphInitReq {
    @NotNull
    private GraphInitEnum type;
}
