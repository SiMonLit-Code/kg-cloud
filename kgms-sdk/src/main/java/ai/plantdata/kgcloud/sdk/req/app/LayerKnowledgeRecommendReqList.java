package ai.plantdata.kgcloud.sdk.req.app;

import ai.plantdata.kgcloud.sdk.req.app.function.EntityName2Id;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 14:32
 */
@ApiModel("多层知识推荐参数")
@Data
public class LayerKnowledgeRecommendReqList implements EntityName2Id {

    @ApiModelProperty("实例id")
    private Long entityId;
    @ApiModelProperty("实例名称")
    private String kw;

    @ApiModelProperty("分页")
    private PageReq page;

    @ApiModelProperty("分层过滤条件，key是层数，value是过滤条件")
    private Map<Integer, KnowledgeRecommendCommonFilterReq> layerFilter;


}
