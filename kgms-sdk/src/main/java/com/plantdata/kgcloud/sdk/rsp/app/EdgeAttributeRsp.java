package com.plantdata.kgcloud.sdk.rsp.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author liyan
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("边属性搜索参数")
public class EdgeAttributeRsp {
    /**
     * todo 补充mongo数据结构
     */
    @ApiModelProperty("数值属性为属性名称 对象属性为 属性对象")
    private Object key;
    @ApiModelProperty("属性个数")
    private Integer num;
}
