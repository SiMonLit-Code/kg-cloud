package com.plantdata.kgcloud.domain.edit.req.basic;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 16:17
 * @Description:
 */
@Data
@ApiModel("概念实体同义提示查询模型")
public class PromptReq extends BaseReq {

    @NotEmpty
    @ApiModelProperty(value = "关键词")
    private String kw;

    @ApiModelProperty(value = "是否忽略大小写,默认忽略大小写")
    private Boolean ignore = true;

    @ApiModelProperty(value = "是否模糊查询,默认前缀搜索")
    private Boolean like = false;

    @ApiModelProperty(value = "是否查询概念")
    private Boolean concept = false;

    @ApiModelProperty(value = "是否查询实体")
    private Boolean entity = true;

    @ApiModelProperty(value = "是否查询属性")
    private Boolean attribute = false;

    @ApiModelProperty(value = "概念ids")
    private List<Long> conceptIds;

    @ApiModelProperty(value = "是否继承")
    private Boolean inherit = false;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

}
