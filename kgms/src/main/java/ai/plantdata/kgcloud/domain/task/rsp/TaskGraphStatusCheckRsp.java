package ai.plantdata.kgcloud.domain.task.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:27
 * @Description:
 */
@Getter
@Setter
@ApiModel("异步任务结果模型")
public class TaskGraphStatusCheckRsp {
    @ApiModelProperty(value = "任务id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "任务类型")
    private String type;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "任务参数")
    private Map<String, Object> params;

    @ApiModelProperty(value = "是否可以运行下一个异步任务")
    private Boolean executed;

    public TaskGraphStatusCheckRsp() {
    }

    public TaskGraphStatusCheckRsp(Boolean executed) {
        this.executed = executed;
    }

    public TaskGraphStatusCheckRsp(Long id, String kgName, String type, String status, Map<String, Object> params,
                                   Boolean executed) {
        this.id = id;
        this.kgName = kgName;
        this.type = type;
        this.status = status;
        this.params = params;
        this.executed = executed;
    }
}
