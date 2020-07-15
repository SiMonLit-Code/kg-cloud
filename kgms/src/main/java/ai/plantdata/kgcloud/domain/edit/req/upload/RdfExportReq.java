package ai.plantdata.kgcloud.domain.edit.req.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/12/12 21:12
 * @Description:
 */
@Getter
@Setter
@ApiModel("rdf导出模型")
public class RdfExportReq {

    @ApiModelProperty(value = "rdf导入格式,rdf,nt,ttl,n3")
    private String format;

    @ApiModelProperty(value = "scope")
    private Integer scope;
}
