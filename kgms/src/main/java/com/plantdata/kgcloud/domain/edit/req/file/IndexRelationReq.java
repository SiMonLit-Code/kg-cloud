package com.plantdata.kgcloud.domain.edit.req.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lp
 */
@Data
@ApiModel("标引实体关系")
public class IndexRelationReq {

    @ApiModelProperty("实体ids")
    private List<Long> entityIds;

    @NotNull(message = "标引文件ID不能为空")
    @ApiModelProperty("标引文件ID")
    private String dwFileId;

}
