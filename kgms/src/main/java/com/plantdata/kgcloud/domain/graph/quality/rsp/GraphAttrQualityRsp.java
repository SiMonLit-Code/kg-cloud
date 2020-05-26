package com.plantdata.kgcloud.domain.graph.quality.rsp;

import com.plantdata.kgcloud.domain.graph.quality.vo.AttrQualityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 15:10
 * @Description:
 */
@Setter
@Getter
@ApiModel("子概念下属性统计信息")
public class GraphAttrQualityRsp {

    @ApiModelProperty("概念名称")
    private String conceptName;

    @ApiModelProperty("实体数量")
    private Long entityCount;

    @ApiModelProperty("概念模式完整度")
    private Double schemaIntegrity;

    @ApiModelProperty("置信度质量")
    private Double reliability;

    @ApiModelProperty("属性统计信息")
    private List<AttrQualityVO> attrQualities;
}
