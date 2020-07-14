package ai.plantdata.kgcloud.domain.edit.req.merge;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 14:48
 * @Description:
 */
@Data
@ApiModel("融合候选集查询模型")
public class MergeRecommendedReq extends BaseReq {

    @ApiModelProperty(value = "主体名称关键字搜索")
    private String key;

    @ApiModelProperty(value = "主体概念id")
    private Long classId;

    @ApiModelProperty(value = "融合分值最小值")
    private Double minScore;

    @ApiModelProperty(value = "融合分值最大值")
    private Double maxScore;

}
