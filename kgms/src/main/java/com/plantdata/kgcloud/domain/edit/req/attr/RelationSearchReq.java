package com.plantdata.kgcloud.domain.edit.req.attr;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/27 09:58
 * @Description:
 */
@Data
@ApiModel("关系溯源搜索模型")
public class RelationSearchReq extends BaseReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    @ApiModelProperty(value = "关系名称")
    private String name;

}
