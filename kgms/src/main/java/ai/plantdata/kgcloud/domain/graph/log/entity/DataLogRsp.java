package ai.plantdata.kgcloud.domain.graph.log.entity;

import com.plantdata.graph.logging.core.GraphLogOperation;
import com.plantdata.graph.logging.core.GraphLogScope;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiezhenxiang 2020/1/15
 */
@Data
@ApiModel
public class DataLogRsp {

    private String id;
    private GraphLogScope scope;
    private GraphLogOperation operation;
    private String message;
    private String batch;
    private Long createTime;
}
