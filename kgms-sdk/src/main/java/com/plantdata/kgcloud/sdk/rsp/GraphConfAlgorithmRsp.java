package com.plantdata.kgcloud.sdk.rsp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
@ApiModel("图谱算法模型")
public class GraphConfAlgorithmRsp {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "接口")
    private String algorithmUrl;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty("类别 1面板类 2统计类")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
