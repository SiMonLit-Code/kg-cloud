package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 11:56
 */
@Getter
@Setter
@ApiModel("普通图探索视图")
public class CommonEntityRsp extends GraphEntityRsp{
    @ApiModelProperty("标签信息")
    private List<TagRsp> tags;
    @ApiModelProperty("样式")
    private StyleRsp style;
}
