package ai.plantdata.kgcloud.sdk.req.app.infobox;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 13:38
 **/
@Getter
@Setter
@ApiModel("知识卡片多模态文件-参数(单个)")
public class InfoboxMultiModalReq {

    @ApiModelProperty("实体id")
    private Long id;
    @ApiModelProperty("实体名称,当id为空时生效")
    private String kw;
}
