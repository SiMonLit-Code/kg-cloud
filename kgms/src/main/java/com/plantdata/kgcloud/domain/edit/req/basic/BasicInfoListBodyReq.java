package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/17 15:10
 * @Description:
 */
@Data
@ApiModel("实体列表body请求模型")
public class BasicInfoListBodyReq {

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "实体标签")
    private List<String> tags;

    @ApiModelProperty(value = "置信度筛选,{$gt:0}")
    private Map<String, Object> reliability;


    /**
     * 来源操作者
     */
    @ApiModelProperty(value = "来源操作者")
    private String sourceUser="";

    /**
     * 来源动作
     */
    @ApiModelProperty(value = "来源动作")
    private String sourceAction="";
}
