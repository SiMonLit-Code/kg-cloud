package com.plantdata.kgcloud.sdk.rsp.app.explore;

import com.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 11:55
 */
@Getter
@Setter
public class BasicEntityRsp implements MetaDataInterface {

    @ApiModelProperty("实例或概念id")
    private Long id;
    @ApiModelProperty("实例或概念名称")
    private String name;
    @ApiModelProperty("0概念 1实例 ")
    private Integer type;
    @ApiModelProperty("直接父概念名称id ")
    private Long conceptId;
    @ApiModelProperty("消歧标识")
    private String meaningTag;
    @ApiModelProperty("图片")
    private ImageRsp img;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("创建时间")
    private String creationTime;
}
