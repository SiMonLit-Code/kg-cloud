package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:41
 */
@ToString
@ApiModel("关系视图")
@Getter
@Setter
public class BasicRelationRsp {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("起点id")
    private Long from;
    @ApiModelProperty("终点id")
    private Long to;
    @ApiModelProperty("属性id")
    private Integer attId;
    @ApiModelProperty("属性名称")
    private String attName;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    //    private List<RelationInfoBean> nRInfo;
//    private List<RelationInfoBean> oRInfo;
    @ApiModelProperty("关系方向")
    private Integer direction;
}
