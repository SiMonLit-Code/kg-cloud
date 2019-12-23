package com.plantdata.kgcloud.domain.edit.req.attr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:51
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("查询概念下的属性定义模型")
public class AttrDefinitionSearchReq {

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Boolean inherit = true;

    @ApiModelProperty(required = true, value = "概念id")
    @NotNull(message = "概念id不能为空")
    private Long conceptId;

    /**
     * 0:全部，1:数值，2:对象
     */
    @ApiModelProperty(hidden = true)
    private Integer type = 0;

    /**
     * 概念ids
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private List<Long> ids = new ArrayList<>(1);

    public AttrDefinitionSearchReq(Boolean inherit, Long conceptId, Integer type) {
        this.inherit = inherit;
        this.conceptId = conceptId;
        this.type = type;
    }
}
