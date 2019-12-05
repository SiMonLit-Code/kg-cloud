package com.plantdata.kgcloud.domain.data.obtain.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/15 16:11
 */
@Getter
@Setter
@ApiModel("统计配置")
public class StatisticConfigRsp {

    @ApiModelProperty("配置id")
    private Integer id;
    @ApiModelProperty("图谱名称")
    private String kgName;
    @ApiModelProperty("统计类型")
    private String type;
    @ApiModelProperty("统计规则")
    /**
     *todo 修改为对象
     */
    private Map<String, Object> rule;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
