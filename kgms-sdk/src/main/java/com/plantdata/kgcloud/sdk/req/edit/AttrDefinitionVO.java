package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:27
 * @Description:
 */
@Data
@ApiModel("属性定义查询结果模型")
public class AttrDefinitionVO {

    @ApiModelProperty(value = "属性id")
    private Integer id;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "属性别名")
    private String alias;
    /**
     * 0:数值属性
     * 1:对象属性
     */
    @ApiModelProperty(value = "属性类型,0:数值属性,1:对象属性")
    private Integer type;

    /**
     * 概念ID（定义域）
     */
    @ApiModelProperty(value = "概念ID（定义域）")
    private Long domainValue;

    /**
     * 仅对象属性才有，概念ID（值域），多个以空格分隔
     */
    @ApiModelProperty(value = "概念ID（值域）")
    private List<Long> rangeValue;

    @ApiModelProperty(value = "属性类型")
    private Integer dataType;

    @ApiModelProperty(value = "属性单位")
    private String dataUnit;

    /**
     * 是否多值
     */
    @ApiModelProperty(value = "是否多值")
    private Integer functional;
    /**
     * 0:正向
     * 1:无向
     */
    @ApiModelProperty(value = "关系属性方向,0:正向,1:无向")
    private Integer direction;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private String modifyTime;

    @ApiModelProperty(value = "边属性信息")
    private List<ExtraInfoVO> extraInfo;

    /**
     * 前端使用
     */
    @ApiModelProperty(value = "业务额外信息")
    private Map<String, Object> additionalInfo;

    @ApiModelProperty(value = "属性约束")
    private Map<String, Object> constraints;
    /**
     * 全局唯一
     */
    @ApiModelProperty(value = "属性唯一标示")
    private String key;


}
