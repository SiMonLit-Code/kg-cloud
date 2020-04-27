package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-20 18:47
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("自定义打标-表")
public class CustomTableRsp {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("tag 枚举，格式：{'tag':['enum1','enum2']}")
    private Map<String, List<String>> typeEnum;

    @ApiModelProperty("字段")
    private List<CustomColumnRsp> columns;

    @ApiModelProperty("关系")
    private List<CustomRelationRsp> relationRsps;
}
