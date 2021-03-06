package ai.plantdata.kgcloud.domain.prebuilder.req;

import ai.plantdata.kgcloud.domain.prebuilder.aop.DefaultHandlerReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-24 22:05
 **/
@Data
public class PreBuilderCreateReq extends DefaultHandlerReq {

    @ApiModelProperty("行业类别")
    private String modelType;

    @ApiModelProperty("模式名称")
    private String name;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("filePath")
    private String filePath;
}
