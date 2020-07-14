package ai.plantdata.kgcloud.domain.edit.req.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2019/12/12 21:07
 * @Description:
 */
@Getter
@Setter
@ApiModel("rdf导入模型")
public class RdfReq {

    @ApiModelProperty(value = "rdf导入格式,rdf,nt,ttl,n3")
    private String format;
}
