package ai.plantdata.kgcloud.domain.edit.rsp;


import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kg.common.bean.ExtraInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:31
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("关系详情")
public class RelationDetailRsp{
    @ApiModelProperty(value = "")
    private List<ExtraInfo> extraInfo;

    @ApiModelProperty(value = "")
    private Map<Integer, Object> relationDataValues;

    @ApiModelProperty(value = "")
    private Map<Integer, List<BasicInfo>> relationObjectValues;

    @ApiModelProperty(value = "")
    private String attrTimeFrom;

    @ApiModelProperty(value = "")
    private String attrTimeTo;

    @ApiModelProperty(value = "")
    private double score;

    @ApiModelProperty(value = "")
    private double reliability;

    @ApiModelProperty(value = "")
    private String source;
}
