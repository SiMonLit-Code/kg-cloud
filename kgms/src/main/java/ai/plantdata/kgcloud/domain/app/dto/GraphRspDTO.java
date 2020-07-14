package ai.plantdata.kgcloud.domain.app.dto;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 9:53
 */
@Getter
@Setter
public class GraphRspDTO {
    /**
     * 图探索直接结果
     */
    private GraphVO graphVo;
    /**
     * 图探索需要后置处理直接使用的页面参数
     */
    private GraphReqAfterInterface graphReq;
    /**
     * 推理结果及所需数据
     */
    private GraphReasoningDTO reasoningDTO;

    public GraphRspDTO(GraphVO graphVo, GraphReqAfterInterface graphReq, GraphReasoningDTO reasoningDTO) {
        this.graphVo = graphVo;
        this.graphReq = graphReq;
        this.reasoningDTO = reasoningDTO;
    }

    public GraphRspDTO(GraphVO graphVo, GraphReqAfterInterface graphReq) {
        this.graphVo = graphVo;
        this.graphReq = graphReq;
    }
}
