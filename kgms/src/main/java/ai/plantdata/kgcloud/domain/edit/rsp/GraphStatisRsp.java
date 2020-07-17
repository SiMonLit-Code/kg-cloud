package ai.plantdata.kgcloud.domain.edit.rsp;

import ai.plantdata.kgcloud.domain.edit.vo.StatisticVO;
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
 * @Date: 2019/12/3 17:39
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("图谱详情统计模型")
public class GraphStatisRsp {

    @ApiModelProperty(value = "图谱基础统计详细")
    private StatisticVO statistics;

    @ApiModelProperty(value = "图谱概念详情统计")
    private List<Map<String, Object>> conceptDetails;
}
