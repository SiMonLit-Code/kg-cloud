package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.constant.GraphInitEnum;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 15:21
 */
@ToString
@Getter
@Setter
@ApiModel("图初始化视图")
public class GraphInitRsp {
    @ApiModelProperty("唯一标识")
    private Integer id;
    @ApiModelProperty("图谱名称")
    private String kgName;
    @ApiModelProperty("初始化类型")
    private GraphInitEnum type;
    @ApiModelProperty("配置信息")
    private Map<String, Object> config;
    @ApiModelProperty("实例列表")
    private List<PromptEntityRsp> entities;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
