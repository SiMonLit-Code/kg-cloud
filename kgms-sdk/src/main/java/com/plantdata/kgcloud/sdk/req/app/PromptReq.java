package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:16
 */
@Getter
@Setter
@ApiModel("实体提示参数")
public class PromptReq extends BaseReq {

    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("类型，默认实体或概念entity_concept； entity：实体； concept:概念 ")
    private String type;
    @ApiModelProperty("查询指定的概念，格式为json数组，默认为查询全部")
    private List<Long> conceptIds = new ArrayList<>();
    @ApiModelProperty("conceptIds为空时此参数生效")
    private List<String> conceptKeys = new ArrayList<>();
    @ApiModelProperty("提示类型")
    private Integer promptType = 0;
    @ApiModelProperty("allowTypes字段指定的概念是否继承")
    private Boolean isInherit = false;
    @ApiModelProperty("是否大小写敏感（默认区分大小写")
    private Boolean isCaseInsensitive = false;
    @ApiModelProperty("是否模糊搜索")
    private Boolean isFuzzy = false;
    @ApiModelProperty("是否使用导出实体数据集检索")
    private Boolean openExportDate = true;
}
