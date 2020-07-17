package ai.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 14:44
 * @Description:
 */
@Data
@ApiModel("来源,批次号条件删除实体模型")
public class EntityMetaDeleteReq {

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "批次号")
    private String batchNo;
}
