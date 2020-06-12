package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:32
 */
@ApiModel("知识推荐参数")
@Data
public class LayerKnowledgeRecommendReqList {

    @ApiModelProperty("实例id")
    private Long entityId;
    @ApiModelProperty("实例名称")
    private String kw;

    @ApiModelProperty("分页")
    private PageReq page;

    @ApiModelProperty("步数 默认2步")
    @Min(1)
    @Max(10)
    private int distance = 2;

    @ApiModelProperty("分层过滤条件，key是层数，value是过滤条件")
    private Map<Integer, KnowledgeRecommendCommonFilterReq> layerFilter;


}
