package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 16:18
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("业务信息(meta)模型")
public class AdditionalReq {

    /**
     * 默认metadata_14
     */
    @ApiModelProperty(hidden = true, value = "meta data id")
    private Integer id = 14;

    @ApiModelProperty("概念或实体id:metadata value")
    private Map<Long, Object> basicMetaData;

    @ApiModelProperty("关系id:metadata value")
    private Map<String, Object> objMetaData;

    @ApiModelProperty("属性定义id:metadata value")
    private Map<Integer, String> adfMetaData;
}
