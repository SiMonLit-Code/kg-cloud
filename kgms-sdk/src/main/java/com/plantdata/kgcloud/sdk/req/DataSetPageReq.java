package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 13:07
 **/
@ApiModel("数据集-schema-查询模型")
@Data
public class DataSetPageReq extends BaseReq {

    @ApiModelProperty("文件夹id")
    private Long folderId;

    @ApiModelProperty("数据类型")
    private Integer dataType;

    @ApiModelProperty("模糊匹配名称")
    private String kw;

    @ApiModelProperty("创建方式")
    private Integer createWay;
}
