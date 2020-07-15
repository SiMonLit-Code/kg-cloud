package ai.plantdata.kgcloud.sdk.req.app.statistic;

import ai.plantdata.kgcloud.sdk.constant.StatisticConstants;
import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
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
 * @date 2019/12/10 20:15
 */
@Getter
@Setter
@ApiModel("实体数值属性统计-参数")
public class EntityStatisticGroupByAttrIdReq implements AttrDefKeyReqInterface {

    @ApiModelProperty("属性定义id")
    private Integer attrDefId;
    @ApiModelProperty("属性定义key 与attrDefId只需要填一个")
    private String attrDefKey;
    @ApiModelProperty(value = "实体id 必填",required = true)
    private List<Long> entityIds;
    @ApiModelProperty(value = "排序 -1反序 1正序 默认-1")
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    @ApiModelProperty("允许的属性值")
    private List<Object> allowValues;
    @ApiModelProperty("返回结构类型 1 key,value格式 0 x轴y轴格式 默认0")
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(StatisticConstants.STATISTIC_MAX_SIZE)
    @ApiModelProperty("数量 min:1 max:10000")
    private Integer size = 10;
    @ApiModelProperty("")
    private Boolean merge = false;
    @ApiModelProperty("日期显示类型及筛选")
    private DateTypeReq dataType;

}
