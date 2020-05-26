package com.plantdata.kgcloud.domain.prebuilder.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreBuilderRelationAttrRsp {


    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("边属性图谱id")
    private Integer attrId;

    @ApiModelProperty("边属性名称")
    private String name;

    @ApiModelProperty("边属性单位")
    private String unit;

    @ApiModelProperty("边属性类型")
    private Integer dataType;

    @ApiModelProperty("表")
    private List<String> tables;

    @ApiModelProperty("添加时间")
    private Date createAt;


    @ApiModelProperty("更新时间")
    private Date updateAt;

    @ApiModelProperty("属性匹配状态")
    private String attrMatchStatus;

    @ApiModelProperty("属性匹配")
    private Integer matchStatus;

}
