package com.plantdata.kgcloud.domain.edit.req.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 15:24
 * @Description:
 */
@Data
@ApiModel(value = "边对象属性值添加模型")
public class EdgeObjectAttrValueReq {

    @NotEmpty
    @ApiModelProperty(value = "关系id")
    private String tripleId;

    @NotNull
    @ApiModelProperty(value = "边id")
    private Integer seqNo;

    @NotNull
    @ApiModelProperty(value = "边对象属性值")
    private List<Long> ranges;

    /**
     * 格式: {seqNo:value}
     */
    @JsonIgnore
    private Map<Integer, List<Long>> object;

}
