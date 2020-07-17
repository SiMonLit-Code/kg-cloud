package ai.plantdata.kgcloud.domain.graph.log.entity;

import com.plantdata.graph.logging.core.ServiceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiezhenxiang 2020/1/15
 */
@Data
@ApiModel
public class ServiceLogRsp {

    private String id;
    @ApiModelProperty("批次号")
    private String batch;
    private String userId;
    @ApiModelProperty("日志")
    private String message;
    @ApiModelProperty("业务功能模块")
    private ServiceEnum serviceEnum;
    @ApiModelProperty("批量操作")
    private Boolean isBatch;
    private Long createTime;
}
