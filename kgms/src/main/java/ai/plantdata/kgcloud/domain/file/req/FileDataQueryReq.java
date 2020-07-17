package ai.plantdata.kgcloud.domain.file.req;

import ai.plantdata.cloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 17:25
 */
@Data
@ApiModel("分页查询参数")
public class FileDataQueryReq extends BaseReq {

    @ApiModelProperty(value = "文件名称")
    private String name;

}
