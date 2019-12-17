package com.plantdata.kgcloud.domain.edit.req.basic;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 10:40
 * @Description:
 */
@Data
@ApiModel("概念或实体列表模型")
public class BasicInfoListReq extends BaseReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "关键字")
    private String kw;

    @ApiModelProperty(value = "是否忽略大小写")
    private Boolean ignore;

    @ApiModelProperty(value = "是否开启模糊查询")
    private Boolean like;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

    @ApiModelProperty(value = "是否继承")
    private Boolean inherit;

    @ApiModelProperty(hidden = true,value = "0:概念,1:实体")
    private Integer type = 1;

}
