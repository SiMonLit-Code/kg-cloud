package ai.plantdata.kgcloud.sdk.req;

import ai.plantdata.kgcloud.sdk.req.app.PageReq;
import ai.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefListKeyReqInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 16:44
 */
@ApiModel("边属性搜索-参数")
@Getter
@Setter
@NoArgsConstructor
public class EdgeSearchReqList extends PageReq implements AttrDefListKeyReqInterface {
    @ApiModelProperty("起点id")
    private List<Long> entityIds;
    @ApiModelProperty("属性id")
    private List<Integer> allowAttrs;
    @ApiModelProperty("属性key")
    private List<String> allowAttrsKey;
    @ApiModelProperty("属性值")
    private List<Long> attrValueIds;
    @ApiModelProperty("开始时间")
    private Map<String, Object> attrTimeFrom;
    @ApiModelProperty("结束时间")
    private Map<String, Object> attrTimeTo;
    @ApiModelProperty("1正向 双向 -1反向")
    @ChooseCheck(value = "[-1,1,0]")
    private Integer direction = 0;
    @ApiModelProperty("边属性搜索参数")
    @Valid
    private List<RelationAttrReq> edgeAttrQuery;

}
