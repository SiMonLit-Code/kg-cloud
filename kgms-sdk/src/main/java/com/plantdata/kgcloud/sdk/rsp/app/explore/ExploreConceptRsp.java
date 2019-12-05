package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 11:31
 */
@Getter
@Setter
@ApiModel("概念")
@AllArgsConstructor
@NoArgsConstructor
public class ExploreConceptRsp {

    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("概念名称")
    private String conceptName;
}
