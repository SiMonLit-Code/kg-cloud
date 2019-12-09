package com.plantdata.kgcloud.domain.d2r.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel("d2r预览结果模型")
public class PreviewRsp {

    @ApiModelProperty("实体")
    private List<EntRsp> ents;
    @ApiModelProperty("同义")
    private List<SynonymRsp> synonyms;
    @ApiModelProperty("数值属性")
    private List<AttrRsp> attrs;
    @ApiModelProperty("关系属性")
    private List<RelationRsp> rels;


}
