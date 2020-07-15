package ai.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 */
@Data
@ApiModel("标引实体关系")
public class IndexRelationReq {

    @ApiModelProperty("实体ids")
    private List<Long> entityIds;

    @ApiModelProperty("关系ID")
    private String relationId;

}
