package ai.plantdata.kgcloud.sdk.req.app.function;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * 推理功能
 *
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 15:19
 */
public interface ReasoningReqInterface {
    /**
     * 获取实体id
     *
     * @return 实体id
     */
    List<Long> fetchEntityIdList();

    /**
     * 层数
     *
     * @return 。
     */
    Integer fetchDistance();

    /**
     * 推理配置
     *
     * @return 。
     */
    Map<Long, Object> fetchReasonConfig();
}
