package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.domain.dw.rsp.ModelAttrBeanRsp;
import com.plantdata.kgcloud.domain.dw.rsp.ModelRelationBeanRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TagJsonReq {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("概念")
    private Set<String> entity;

    @ApiModelProperty("关系")
    private Set<ModelRelationBeanRsp> relation;

    @ApiModelProperty("属性")
    private Set<ModelAttrBeanRsp> attr;

}

