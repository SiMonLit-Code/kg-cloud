package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:43
 */
@Getter
@Setter
@ToString
@ApiModel("对象属性视图")
@AllArgsConstructor
@NoArgsConstructor
public class ObjectAttributeRsp {
    @ApiModelProperty("属性Id")
    private Integer attrDefId;
    @ApiModelProperty("属性关联的实例")
    private List<PromptEntityRsp> entityList;
}
