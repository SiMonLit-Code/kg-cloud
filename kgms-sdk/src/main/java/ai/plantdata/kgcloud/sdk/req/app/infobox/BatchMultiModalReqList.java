package ai.plantdata.kgcloud.sdk.req.app.infobox;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 13:45
 **/
@ApiModel("知识卡片多模态文件-参数(批量)")
@Getter
@Setter
public class BatchMultiModalReqList {

    @ApiModelProperty("实体id、概念id")
    private List<Long> ids;
    @ApiModelProperty("实体名称,当id为空时生效")
    private List<String> kws;
}
