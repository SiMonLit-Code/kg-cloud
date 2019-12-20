package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyListReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 17:53
 */
@ApiModel("属性定义按照概念查询")
@Getter
@Setter
public class AttrDefQueryReq implements ConceptKeyReqInterface {
    @NonNull
    @ApiModelProperty("概念id，查询指定概念的属性 conceptId 和 conceptKey不能同时为空")
    private Long conceptId;
    @ApiModelProperty("概念唯一标识key,数据将为转换为conceptId参数")
    private String conceptKey;
    @ApiModelProperty("是否继承展示父概念属性 默认true")
    private boolean inherit = true;


}
