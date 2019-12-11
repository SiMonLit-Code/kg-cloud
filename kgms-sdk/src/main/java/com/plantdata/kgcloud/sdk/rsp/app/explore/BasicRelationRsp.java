package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

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

    @ApiModelProperty("起点id")
    private Long from;
    @ApiModelProperty("终点id")
    private Long to;
    @ApiModelProperty("属性id")
    private Integer attId;
    @ApiModelProperty("属性名称")
    private String attName;
    @ApiModelProperty("关系方向")
    private Integer direction;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("样式1")
    private Map<String, Object> labelStyle;
    @ApiModelProperty("样式2")
    private Map<String, Object> linkStyle;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EdgeInfo {
        @ApiModelProperty("边属性名称")
        private String name;
        @ApiModelProperty("边属性序号")
        private Integer seqNo;
        @ApiModelProperty("边属性值")
        private Object value;
        @ApiModelProperty("边属性值")
        private Integer dataType;
        @ApiModelProperty("值域")
        private List<Long> rangeValue;
    }
}
