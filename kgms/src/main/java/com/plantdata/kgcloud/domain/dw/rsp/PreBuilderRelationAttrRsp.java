package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.constant.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
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

    @ApiModelProperty("添加时间")
    private Date createAt;

    @ApiModelProperty("更新时间")
    private Date updateAt;
}
