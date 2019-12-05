package com.plantdata.kgcloud.sdk.req.app.explore.common;

import com.plantdata.kgcloud.sdk.req.app.AttrSortReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 16:00
 */
@ApiModel("普通图探索参数")
@Getter
@Setter
public class CommonFiltersReq {
    @ApiModelProperty("实例id")
    private Long id;
    @ApiModelProperty("实体或概念名称,若id为空时此参数生效，kw和id不能同时为空")
    private String kw;
    @ApiModelProperty("是否读取私有属性，默认读取")
    private boolean privateAttRead;
    @ApiModelProperty("上下位关系的读取层数，0表示不读取，默认为0")
    @Min(value = 0, message = "hyponymyDistance最小值为0")
    @Max(value = 100, message = "hyponymyDistance最大值为100")
    private Integer hyponymyDistance;
    @ApiModelProperty(value = "边附加属性排序参数")
    private List<AttrSortReq> attSorts;

}
