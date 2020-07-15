package ai.plantdata.kgcloud.sdk.rsp.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 13:47
 */
@Getter
@Setter
@ApiModel("关系的更新参数")
public class RelationUpdateReq {
@ApiModelProperty(value = "属性id",required = true)
    private Integer attrId;
    @ApiModelProperty(value = "关系id",required = true)
    private String tripleId;
    @ApiModelProperty("开始时间 格式yyyy-MM-dd hh:mm:ss")
    private String attrTimeFrom;
    @ApiModelProperty("结束时间 格式yyyy-MM-dd hh:mm:ss")
    private String attrTimeTo;
    @ApiModelProperty("边属性 key->序号 val 值")
    private Map<Integer, String> extraInfoMap;
    @ApiModelProperty("错误信息")
    private String note;
}
