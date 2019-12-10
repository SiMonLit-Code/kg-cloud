package com.plantdata.kgcloud.domain.edit.rsp;

import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 10:10
 * @Description:
 */
@Data
@ApiModel("关系查询结果模型")
public class RelationRsp {

    @ApiModelProperty(value = "关系id")
    private String tripleId;

    @ApiModelProperty(value = "属性id")
    private Integer attrId;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "关系的起点")
    private BasicInfoVO from;

    @ApiModelProperty(value = "关系的终点")
    private BasicInfoVO to;

    @ApiModelProperty(value = "关系开始时间")
    private String attrTimeFrom;

    @ApiModelProperty(value = "关系截止时间")
    private String attrTimeTo;

    @ApiModelProperty(value = "边附加属性")
    private Map<String, Object> extraInfoMap;

    @ApiModelProperty(value = "关系权重")
    private Double score;

    @ApiModelProperty(value = "关系来源")
    private String source;

    @ApiModelProperty(value = "关系批次")
    private String batch;

    @ApiModelProperty(value = "关系可信度")
    private Double reliability;
}
