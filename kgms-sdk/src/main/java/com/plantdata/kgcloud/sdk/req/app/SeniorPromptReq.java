package com.plantdata.kgcloud.sdk.req.app;

import ai.plantdata.cloud.bean.BaseReq;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.req.app.function.PromptSearchInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 10:02
 */
@Getter
@Setter
@ApiModel("高级实体搜索参数")
public class SeniorPromptReq extends BaseReq implements PromptSearchInterface {

    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("概念唯一标识")
    private String conceptKey;
    @ApiModelProperty("关键字")
    private String kw;
    @ApiModelProperty("实体查询条件")
    private List<EntityQueryFiltersReq> query;
    @ApiModelProperty("是否模糊搜索 false前缀搜索，true支持模糊搜索")
    private boolean fuzzy;
    /**
     * ?一直是开启的
     */
    @ApiModelProperty("？")
    private Boolean openExportDate = true;

    @ApiModelProperty(hidden = true)
    @Override
    public List<Long> getConceptIds() {
        return Lists.newArrayList(conceptId);
    }

    @ApiModelProperty(hidden = true)
    @Override
    public Boolean getInherit() {
        return true;
    }
}
