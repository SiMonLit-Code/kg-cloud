package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 12:14
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ApiModel("图片视图")
public class ImageRsp {
    @ApiModelProperty("图片名称")
    private String name;
    @ApiModelProperty("图片链接地址")
    private String href;
}
