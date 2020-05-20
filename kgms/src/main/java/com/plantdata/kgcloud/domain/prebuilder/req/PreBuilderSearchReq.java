package com.plantdata.kgcloud.domain.prebuilder.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "预构建模式请求")
public class PreBuilderSearchReq extends BaseReq {

    @ApiModelProperty(value = "搜索词")
    private String kw;

    @ApiModelProperty(value = "模式类型")
    private String modelType;

    @ApiModelProperty(value = "模式标签 是否图谱模式")
    private boolean isGraph;

    @ApiModelProperty(value = "模式标签 是否数仓模式")
    private boolean isDw;

    @ApiModelProperty(value = "模式标签 是否管理员发布")
    private boolean isManage;

    @ApiModelProperty(value = "模式标签 是否用户发布")
    private boolean isUser;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "发布者")
    private String username;

}
