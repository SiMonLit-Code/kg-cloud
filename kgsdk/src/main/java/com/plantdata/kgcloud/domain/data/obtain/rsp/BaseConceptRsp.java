package com.plantdata.kgcloud.domain.data.obtain.rsp;

import com.plantdata.kgcloud.domain.common.rsp.matedata.AdditionalRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 13:37
 */

@ToString
@Getter
@Setter
@ApiModel("概念")
public class BaseConceptRsp {
    @ApiModelProperty("概念id")
    private Long id;
    @ApiModelProperty("父概念id")
    private Long parentId;
    @ApiModelProperty("概念名称")
    private Long name;
    private AdditionalRsp additional;
    @ApiModelProperty("图片")
    private String img;
    @ApiModelProperty("地理位置信息 true 开启 false关闭")
    private Boolean openGis;
}
