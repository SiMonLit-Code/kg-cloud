package com.plantdata.kgcloud.domain.edit.rsp;

import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 18:15
 * @Description:
 */
@Data
@ApiModel("属性规约查询结果模型")
public class AttrInduceFindRsp {

    @ApiModelProperty(value = "属性规约实体数量")
    private Integer count;

    @ApiModelProperty(value = "属性规约占比")
    private Double percent;

    @ApiModelProperty(value = "属性规约类型,1:对象化,2:公有化,3:合并")
    private Integer type;

    //建议的
    @ApiModelProperty(value = "属性规约建议")
    private String tipsMsg;

    @ApiModelProperty(value = "属性规约信息")
    private String msg;

    @ApiModelProperty(value = "属性定义集")
    private List<AttrDefinitionVO> attrDefinitionList;
}
